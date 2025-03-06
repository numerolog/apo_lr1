package apo.managers.conversation.impl.dto;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MessageRepository extends JpaRepository<Conversation, Integer> 
{

	@Query("SELECT m FROM Message m WHERE m.conversation.id = ?1 AND m.id >= ?2")
	List<Message> find(int chat_id, int offset_from, int offset_size);
	
	@Query("SELECT m FROM Message m WHERE m.conversation.id = ?1 AND m.id = ?2")
	Message findById(int chat_id, int message_id);

//	@Query("SELECT COUNT(m) > 0 THEN true ELSE false FROM MessageReadenBy m WHERE m.message.conversation.id = ?1 AND m.message.id = ?2 AND m.user_id = ?3 AND m.flag = ?4")
//	boolean hasFlag0(int chat_id, int message_id, int user_id, int flag);
	
	@Query("SELECT COUNT(m) FROM MessageFlag m WHERE m.message.conversation.id = ?1 AND m.message.id = ?2 AND m.user_id = ?3 AND m.flag = ?4")
	long hasFlag0(int chat_id, int message_id, int user_id, int flag);
		
	default boolean hasFlag(int chat_id, int message_id, int user_id, int flag)
	{
		return hasFlag0(chat_id, message_id, user_id, flag) > 0;
	}
	
	@Query("DELETE FROM MessageFlag m WHERE m.message.conversation.id = ?1 AND m.message.id = ?2 AND m.user_id = ?3 AND m.flag = ?4")
	boolean removeFlag(int chat_id, int message_id, int user_id, int flag) ;
	
}
