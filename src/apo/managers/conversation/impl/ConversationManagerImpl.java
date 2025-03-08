package apo.managers.conversation.impl;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import apo.managers.ManagerException;
import apo.managers.conversation.IConversation;
import apo.managers.conversation.IConversationManager;
import apo.managers.conversation.IMessage;
import apo.managers.conversation.impl.dto.Conversation;
import apo.managers.conversation.impl.dto.ConversationMember;
import apo.managers.conversation.impl.dto.ConversationRepository;
import apo.managers.conversation.impl.dto.Message;
import apo.managers.conversation.impl.dto.MessageFlag;
import apo.managers.conversation.impl.dto.MessageRepository;
import apo.managers.event.IEventManager;
import jakarta.transaction.Transactional;

@Component
public class ConversationManagerImpl implements IConversationManager
{
	
	@Autowired
	private ConversationRepository conversation_repository;
	
	@Autowired
	private MessageRepository message_repository;

    @Autowired
    private IEventManager event_manager;
	
	@Override
	public List<? extends IConversation> getList(int user_id) 
	{
		return conversation_repository.findJoined(user_id);
	}

	@Override
	@Transactional
	public Collection<? extends IMessage> getList(int user_id, int chat_id, int offset_from, int offset_size) throws ManagerException 
	{
		if (!conversation_repository.inConversation(chat_id, user_id))
			throw new ManagerException("user not in conversation");

		return message_repository.find(chat_id, offset_from, offset_size);
	}

	@Transactional()
	public IMessage putMessage0(int user_id, int chat_id, String message_text) throws ManagerException 
	{
		if (!conversation_repository.inConversation(chat_id, user_id))
			throw new ManagerException("user not in conversation");

		var conv = conversation_repository.findByIdEquals(chat_id);
		
		var msg = new Message();
		msg.setConversation(conv);
		msg.setText(message_text);
		msg.setAuthor_id(user_id);
		
		conv.getMessages().add(msg);
		
		return msg;
	}
	
	@Transactional()
	public IMessage putMessage(int user_id, int chat_id, String message_text) throws ManagerException 
	{
		var msg = putMessage0(user_id, chat_id, message_text);
		var conv = conversation_repository.findByIdEquals(chat_id);
		for (var member : conv.getMembersId())
			event_manager.noticeNewMessage(member, chat_id, msg.getId());
		return msg;
	}
	
	@Transactional
	public boolean toggleMessageFlag0(int user_id, int chat_id, int message_id, int flag, boolean remove) throws ManagerException
	{
		if (!remove)
		{
			if (message_repository.hasFlag(chat_id, message_id, user_id, flag))
			{
				System.err.println("has flag on " + chat_id + " " + message_id + " " + user_id + " " + flag);
				return false;
			}
		}
		else
		{
			if (flag == MessageFlag.READEN_FLAG)
				throw new ManagerException("readen flag unremovable");
		}
		
		if (!conversation_repository.inConversation(chat_id, user_id))
			throw new ManagerException("user not in conversation");
	  
		var msg = message_repository.findById(chat_id, message_id);

		if (flag == MessageFlag.READEN_FLAG && msg.getAuthor_id() == user_id)
			return false;

		//TODO: спам сообщениями всё ломает ибо транзакции везде наставлены абыкак
		// Record has changed since last read in table 'message_flag'
		if (remove)
		{
			if (!message_repository.removeFlag(chat_id, message_id, user_id, flag))
				throw new ManagerException("not removed");
		}
		else
		{
			var readen = new MessageFlag();
			readen.message = msg;
			readen.user_id = user_id;
			readen.flag = flag;
			msg.getFlags().add(readen);
		}
		return true;
	}
	
	@Transactional()
	public void toggleMessageFlag(int user_id, int chat_id, int message_id, int flag, boolean remove) throws ManagerException
	{
		if (!toggleMessageFlag0(user_id, chat_id, message_id, flag, remove))
			return;
		var conv = conversation_repository.findByIdEquals(chat_id);
		for (var member : conv.getMembersId())
			event_manager.noticeAddRemoveMessageFlag(member, chat_id, message_id, flag, remove);
	}
	
	@Override
	@Transactional
	public void addRemoveUser(int user_id, int chat_id, boolean remove, int target_user_id) throws ManagerException 
	{
		var conv = conversation_repository.findByIdEquals(chat_id);
		if (conv == null)
			throw new ManagerException("failed to get conversation");

		boolean removeSelf = user_id == target_user_id && remove;
		
		if (conv.getOwner_user_id() != user_id)
		{
			if (removeSelf)
			{
				;
			}
			else
			{
				throw new ManagerException("conversation not owned");
			}
		}
		
		if (remove)
		{
			conv.getMembers().removeIf(member -> member.getUser_id() == target_user_id);
		}
		else
		{
			var member = new ConversationMember(target_user_id);
			conv.getMembers().add(member);
			member.conversation = conv;
		}
				
		event_manager.noticeAddRemoveConversation(target_user_id, chat_id, target_user_id, remove);
	}
	
	
	@Override
	@Transactional
	public IConversation create(int user_id, String name) throws ManagerException 
	{
		var conv = new Conversation();
		conv.name = name;
		conv.owner_user_id = user_id;

		conversation_repository.save(conv);
		
		// Добавляем самих себя
		addRemoveUser(user_id, conv.getId(), false, user_id);
		
		return conv;
	}
	
}
