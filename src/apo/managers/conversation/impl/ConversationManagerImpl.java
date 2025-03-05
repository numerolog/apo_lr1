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
import apo.managers.conversation.impl.dto.MessageRepository;
import apo.managers.event.IEventManager;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

@Component
public class ConversationManagerImpl implements IConversationManager
{
/*
	static class ConversationImpl implements IConversation
	{
		DataOf_SYSTEM data;
		
		static class DataOf_USER
		{
			public int id;
			public int owner_user_id;
			public Set<Integer> members = new HashSet<>();
			
			public DataOf_USER() 
			{
				;
			}
			
			public DataOf_USER(ConversationImpl conversationImpl) 
			{
				this.id = conversationImpl.getId();
				this.owner_user_id = conversationImpl.getOwnerUserId();
			}
			
		}
		
		static class DataOf_SYSTEM extends DataOf_USER
		{
			;
		}

		@Override
		public Object getScopeData(int scope_id) 
		{
			switch (scope_id)
			{
				case SCOPE_USER:
					return new DataOf_USER(this);
			}
			return null;
		}

		@Override
		public int getId() 
		{
			return data.id;
		}
		
		public int getOwnerUserId() 
		{
			return data.owner_user_id;
		}
		
	}*/
	
	//Map<Integer, Conversation> conversations = new ConcurrentHashMap<>();
	
	@Autowired
	private ConversationRepository conversation_repository;
	
	@Autowired
	private MessageRepository message_repository;
	
	private Conversation getOrLoad(int chat_id) 
	{
		if (!conversation_repository.existsById(chat_id))
			return null;
		return conversation_repository.findByIdEquals(chat_id);// conversations.get(chat_id);
	}

	private Conversation commit(Conversation conv) 
	{
		return conversation_repository.save(conv);
	}
	
	@Override
	public Collection<IConversation> getList(int user_id) 
	{
		return (Collection<IConversation>)(Object)conversation_repository.findJoined(user_id);
	}

	@Override
	public Collection<IMessage> getList(int user_id, int chat_id, int offset_from, int offset_size) throws ManagerException 
	{
		if (!getList(user_id).stream().filter(c -> c.getId() == chat_id).findAny().isPresent())
			throw new ManagerException("user not in conversation");

		return (Collection<IMessage>)(Object)message_repository.find(chat_id, offset_from, offset_size);
	}

	@Override
	@Transactional
	public IMessage putMessage(int user_id, int chat_id, String message_text) throws ManagerException 
	{
		if (!getList(user_id).stream().filter(c -> c.getId() == chat_id).findAny().isPresent())
			throw new ManagerException("user not in conversation");

		var conv = getOrLoad(chat_id);
		var msg = new Message();
		msg.setConversation(conv);
		msg.setText(message_text);
		msg.setAuthor_id(user_id);
		
		conv.getMessages().add(msg);
		//commit(msg);
		
		return msg;
	}

    @Autowired
    private IEventManager event_manager;
        
	@Override
	public void addRemoveUser(int user_id, int chat_id, boolean remove, int target_user_id) throws ManagerException 
	{
		Conversation conv = getOrLoad(chat_id);
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
		
		// TODO: не потокобезопасно
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
				
		commit(conv);
		
		event_manager.noticeAddRemoveConversation(target_user_id, chat_id, target_user_id, removeSelf);
	}
	
	
	@Override
	@Transactional
	public IConversation create(int user_id) throws ManagerException 
	{
		Conversation conv = new Conversation();
		
		conv.owner_user_id = user_id;

		conv = commit(conv);
		
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
