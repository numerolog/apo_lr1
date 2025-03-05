package apo.managers.conversation.impl.dto;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ConversationRepository extends JpaRepository<Conversation, Integer> {

	@Query("select c from Conversation c where c.owner_user_id = ?1")
	Conversation findByOwner(int owner_id);
	Conversation findByIdEquals(int id);
	
	@Query("select c from Conversation c  LEFT JOIN c.members m where c.owner_user_id = ?1 or m.user_id = ?1")
	List<Conversation> findJoined(int user_id);
	
}

