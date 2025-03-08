package apo.managers.conversation.impl.dto;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ConversationRepository extends JpaRepository<Conversation, Integer> 
{

	@Query("SELECT c FROM Conversation c WHERE c.owner_user_id = ?1")
	public Conversation findByOwner(int owner_id);
	public Conversation findByIdEquals(int id);
	
	@Query("SELECT c FROM Conversation c LEFT JOIN c.members m WHERE c.owner_user_id = ?1 OR m.user_id = ?1")
	public List<Conversation> findJoined(int user_id);

//	@Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false FROM Conversation c LEFT JOIN c.members m WHERE c.id = ?1 AND (c.owner_user_id = ?2 OR m.user_id = ?2)")
	//@Query("SELECT COUNT(c) FROM Conversation c LEFT JOIN c.members m WHERE c.id = ?1 AND (c.owner_user_id = ?2 OR m.user_id = ?2)")
	@Query("SELECT COUNT(c) FROM Conversation c LEFT JOIN c.members m WHERE c.id = ?1 AND m.user_id = ?2")
	public long inConversation0(int chat_id, int user_id);
	
	default boolean inConversation(int chat_id, int user_id)
	{
		return inConversation0(chat_id, user_id) > 0;
	}

}

