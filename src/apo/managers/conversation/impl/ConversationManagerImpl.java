package apo.managers.conversation.impl;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
import jakarta.annotation.PostConstruct;
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
    
//	private Conversation getOrLoad(int chat_id) 
//	{
//		if (!conversation_repository.existsById(chat_id))
//			return null;
//		return conversation_repository.findByIdEquals(chat_id);// conversations.get(chat_id);
//	}

//	private Conversation commit(Conversation conv) 
//	{
//		return conversation_repository.save(conv);
//	}
	
	@Override
	public Collection<IConversation> getList(int user_id) 
	{
		return (Collection<IConversation>)(Object)conversation_repository.findJoined(user_id);
	}

	@Override
	@Transactional
	public Collection<IMessage> getList(int user_id, int chat_id, int offset_from, int offset_size) throws ManagerException 
	{
//		if (!getList(user_id).stream().filter(c -> c.getId() == chat_id).findAny().isPresent())
		if (!conversation_repository.inConversation(chat_id, user_id))
			throw new ManagerException("user not in conversation");

		var r = (Collection<IMessage>)(Object)message_repository.find(chat_id, offset_from, offset_size);
		
		for (var msg : r)
			toggleMessageFlag(user_id, chat_id, msg.getId(), MessageFlag.READEN_FLAG, false);
		return r;
	}

	@Override
	@Transactional
	public IMessage putMessage(int user_id, int chat_id, String message_text) throws ManagerException 
	{
//		if (!getList(user_id).stream().filter(c -> c.getId() == chat_id).findAny().isPresent())
		if (!conversation_repository.inConversation(chat_id, user_id))
			throw new ManagerException("user not in conversation");

		var conv = conversation_repository.findByIdEquals(chat_id);
		
		var msg = new Message();
		msg.setConversation(conv);
		msg.setText(message_text);
		msg.setAuthor_id(user_id);
		
		conv.getMessages().add(msg);
		
		event_manager.noticeNewMessage(user_id, chat_id, msg.getId());
		
		return msg;
	}

	@Override
	@Transactional
//	public void markReaden(int user_id, int chat_id, int message_id) throws ManagerException
	public void toggleMessageFlag(int user_id, int chat_id, int message_id, int flag, boolean remove) throws ManagerException
	{
		if (!remove)
		{
			if (message_repository.hasFlag(chat_id, message_id, user_id, flag))
				return;
		}
		else
		{
			if (flag == MessageFlag.READEN_FLAG)
				throw new ManagerException("readen flag unremovable");
		}
		
//		if (!getList(user_id).stream().filter(c -> c.getId() == chat_id).findAny().isPresent())
		if (!conversation_repository.inConversation(chat_id, user_id))
			throw new ManagerException("user not in conversation");
		
//		var conv = getOrLoad(chat_id);
		var msg = message_repository.findById(chat_id, message_id);
		
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
		
		event_manager.noticeAddRemoveMessageFlag(user_id, chat_id, message_id, flag, remove);
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
				
//		commit(conv);
		
		event_manager.noticeAddRemoveConversation(target_user_id, chat_id, target_user_id, removeSelf);
	}
	
	
	@Override
	@Transactional
	public IConversation create(int user_id) throws ManagerException 
	{
		var conv = new Conversation();
		
		conv.owner_user_id = user_id;

		conversation_repository.save(conv);
		
		// Добавляем самих себя
		addRemoveUser(user_id, conv.getId(), false, user_id);
		
		return conv;
	}
	
	@PostConstruct
	void postInit()
	{
//		try {
//			if (getOrLoad(1) == null)
//				create(1);
//		} catch (ManagerException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
	}

}
