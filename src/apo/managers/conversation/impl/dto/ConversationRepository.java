package apo.managers.conversation.impl.dto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ConversationRepository extends JpaRepository<Conversation, Integer> {

	@Query("select u from Conversation u where u.owner_user_id = ?1")
	Conversation findByOwner(int owner_id);
	Conversation findByIdEquals(int id);
	
	  
}

