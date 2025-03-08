package apo.managers.conversation.impl.dto;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MessageRepository extends JpaRepository<Message, Integer> 
{

	@Query("SELECT m FROM Message m WHERE m.conversation.id = ?1 ORDER BY m.id DESC")
	List<Message> find0(int chat_id, Pageable p);

	@Query("SELECT m FROM Message m WHERE m.conversation.id = ?1 AND m.id <= ?2 ORDER BY m.id DESC")
	List<Message> find1(int chat_id, int offset_from, Pageable p);
	
	default List<Message> find(int chat_id, int offset_from, int offset_size)
	{
		Pageable limit = PageRequest.of(0, offset_size);
		return offset_from == -1 ? find0(chat_id, limit) : find1(chat_id, offset_from, limit);
	}
	
	@Query("SELECT m FROM Message m WHERE m.conversation.id = ?1 AND m.id = ?2")
	Message findById(int chat_id, int message_id);

//	@Query("SELECT COUNT(m) > 0 THEN true ELSE false FROM MessageReadenBy m WHERE m.message.conversation.id = ?1 AND m.message.id = ?2 AND m.user_id = ?3 AND m.flag = ?4")
	@Query("SELECT COUNT(m) FROM MessageFlag m WHERE m.message.conversation.id = ?1 AND m.message.id = ?2 AND m.user_id = ?3 AND m.flag = ?4")
	long hasFlag0(int chat_id, int message_id, int user_id, int flag);
		
	default boolean hasFlag(int chat_id, int message_id, int user_id, int flag)
	{
		long c = hasFlag0(chat_id, message_id, user_id, flag) ;
		System.err.println(c);
		return c > 0;
	}
	
	@Query("DELETE FROM MessageFlag m WHERE m.message.conversation.id = ?1 AND m.message.id = ?2 AND m.user_id = ?3 AND m.flag = ?4")
	boolean removeFlag(int chat_id, int message_id, int user_id, int flag) ;
	
}
