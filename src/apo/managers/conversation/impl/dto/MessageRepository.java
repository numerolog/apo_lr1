package apo.managers.conversation.impl.dto;

import java.util.List;

import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
/*
public interface MessageRepository extends JpaRepository<Message, Integer> 
{

	@Query("select m from Message m where m.conversation = ?1 and m.id >= ?2")
	List<Message> find(int chat_id, int offset_from);

//	@Query("select m from Message m where m.conversation = ?1 and m.id >= ?2 LIMIT ?3")
//	List<Message> find(int chat_id, int offset_from, int offset_size);
}
*/
import jakarta.persistence.PersistenceContext;

@Repository
public class MessageRepository
{
	@PersistenceContext
	private EntityManager em;
	
	public List<Message> find(int chat_id, int offset_from, int offset_size)
	{
		return em.createQuery("select m from Message m where m.conversation.id = ?1 and m.id >= ?2", Message.class).setMaxResults(offset_size).setParameter(1, chat_id).setParameter(2, offset_from).getResultList();
	}
}


