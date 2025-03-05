package apo.managers.event;

public interface IEventManager 
{
	
	// Кто-то написал новое-сообщение
	void noticeNewMessage(int target_user_id, int chat_id, int message_id);
	
	// Кто-то добавил/удалил ConversationMember'ра
	void noticeAddRemoveConversation(int target_user_id, int chat_id, int user_id, boolean removed);
	
	// Кто-то поставил/удалил MessageFlag
	void noticeAddRemoveMessageFlag(int user_id, int chat_id, int message_id, int flag, boolean remove);
	
}
