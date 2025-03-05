package apo.managers.session.impl.dto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Integer> {
//    List<Person> findByAgeGreaterThan(int age);
	
	@Query("SELECT u FROM User u WHERE u.login = ?1")
	User findByLogin(String login);
	  
}

