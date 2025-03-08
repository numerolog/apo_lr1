package apo.managers.conversation;

import java.util.Collection;
import java.util.List;

import apo.managers.ManagerException;

public interface IConversationManager 
{

	List<? extends IConversation> getList(int user_id) throws ManagerException;

	Collection<? extends IMessage> getList(int user_id, int chat_id, int offset_from, int offset_size) throws ManagerException;

	IMessage putMessage(int user_id, int chat_id, String message_text) throws ManagerException;

	void toggleMessageFlag(int user_id, int chat_id, int message_id, int flag, boolean remove) throws ManagerException;
	
	void addRemoveUser(int user_id, int chat_id, boolean remove, int target_user_id) throws ManagerException;

	IConversation create(int user_id, String name) throws ManagerException;

}
