package apo.managers.conversation;

import java.util.Collection;

public interface IConversationManager 
{

	Collection<IConversation> getList(Integer userid);

	Collection<IMessage> getList(Integer userId, int chat_id, int offset_from, int offset_size);

	IMessage putMessage(Integer userId, int chat_id, String message_text);

	void addRemoveUser(Integer userId, int chat_id, boolean remove, int user_id);

	IConversation create(Integer userId);

}
