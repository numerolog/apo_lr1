package apo.managers.session.impl.dto;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Integer> 
{
	
	@Query("SELECT u FROM User u WHERE u.login = ?1")
	User findByLogin(String login);

	@Query("SELECT u FROM User u WHERE u.login LIKE %?1%")
	List<User> search(String term);
	  
}

