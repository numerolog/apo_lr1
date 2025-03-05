package apo.managers.conversation.impl;

import java.util.Collection;

import org.springframework.stereotype.Component;

import apo.managers.conversation.IConversation;
import apo.managers.conversation.IConversationManager;
import apo.managers.conversation.IMessage;

@Component
public class ConversationManagerImpl implements IConversationManager
{

	@Override
	public Collection<IConversation> getList(Integer userid) 
	{
		return null;
	}

	@Override
	public Collection<IMessage> getList(Integer userId, int chat_id, int offset_from, int offset_size) 
	{
		return null;
	}

	@Override
	public IMessage putMessage(Integer userId, int chat_id, String message_text) 
	{
		return null;
	}

	@Override
	public void addRemoveUser(Integer userId, int chat_id, boolean remove, int user_id) 
	{
		
	}

	@Override
	public IConversation create(Integer userId) 
	{
		return null;
	}

}
