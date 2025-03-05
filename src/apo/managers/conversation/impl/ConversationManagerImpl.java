package apo.managers.conversation.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import apo.managers.ManagerException;
import apo.managers.conversation.IConversation;
import apo.managers.conversation.IConversationManager;
import apo.managers.conversation.IMessage;
import apo.managers.conversation.impl.ConversationManagerImpl.ConversationImpl.DataOf_SYSTEM;
import apo.managers.event.IEventManager;
import jakarta.annotation.PostConstruct;

@Component
public class ConversationManagerImpl implements IConversationManager
{

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
		
	}
	
	Map<Integer, ConversationImpl> conversations = new ConcurrentHashMap<>();
	private ConversationImpl getOrLoad(int chat_id) 
	{
		return conversations.get(chat_id);
	}

	private void commit(ConversationImpl conv) 
	{
		
	}
	
	@Override
	public Collection<IConversation> getList(int user_id) 
	{
		
		
		return null;
	}

	@Override
	public Collection<IMessage> getList(int user_id, int chat_id, int offset_from, int offset_size) 
	{
		return null;
	}

	@Override
	public IMessage putMessage(int user_id, int chat_id, String message_text) 
	{
		return null;
	}

    @Autowired
    private IEventManager event_manager;
        
	@Override
	public void addRemoveUser(int user_id, int chat_id, boolean remove, int target_user_id) throws ManagerException 
	{
		ConversationImpl conv = getOrLoad(chat_id);
		if (conv == null)
			throw new ManagerException("failed to get conversation");
		
		boolean removeSelf = user_id == target_user_id && remove;
		
		if (conv.getOwnerUserId() != user_id)
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
			conv.data.members.remove(target_user_id);
		}
		else
		{
			conv.data.members.add(target_user_id);
		}
				
		commit(conv);
		
		event_manager.noticeAddRemoveConversation(target_user_id, chat_id, target_user_id, removeSelf);
	}
	
	AtomicInteger id_counter = new AtomicInteger();
	
	@Override
	public IConversation create(int user_id) throws ManagerException 
	{
		ConversationImpl conv = new ConversationImpl();
		
		conv.data = new DataOf_SYSTEM();
		conv.data.id = id_counter.incrementAndGet();
		conv.data.owner_user_id = user_id;
		
		conversations.put(conv.data.id, conv);
		commit(conv);
		
		// Добавляем самих себя
		addRemoveUser(user_id, conv.getId(), false, user_id);
		
		return conv;
	}
	
	@PostConstruct
	void postInit()
	{
		try {
			create(1);
		} catch (ManagerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
