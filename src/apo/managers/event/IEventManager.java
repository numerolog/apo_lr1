package apo.managers.event;

public interface IEventManager 
{

	void noticeNewMessage(int target_user_id, int chat_id, int message_id);
	void noticeAddRemoveConversation(int target_user_id, int chat_id, int user_id, boolean removed);
	
}
