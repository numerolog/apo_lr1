package apo.managers.conversation;

import java.util.Collection;

import apo.managers.ManagerException;

public interface IConversationManager 
{

	Collection<IConversation> getList(int user_id) throws ManagerException;

	Collection<IMessage> getList(int user_id, int chat_id, int offset_from, int offset_size) throws ManagerException;

	IMessage putMessage(int user_id, int chat_id, String message_text) throws ManagerException;

	void addRemoveUser(int user_id, int chat_id, boolean remove, int target_user_id) throws ManagerException;

	IConversation create(int user_id) throws ManagerException;

}
