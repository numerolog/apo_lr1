package apo.managers.session;

import java.util.List;

import apo.managers.ManagerException;
import apo.managers.session.impl.dto.User;

public interface ISessionManager 
{

	IUserSession register(String ip, String login, String password) throws ManagerException;
	
	IUserSession auth(String ip, String login, String password) throws ManagerException;
	IUserSession auth(String ip, String token) throws ManagerException;

	boolean isValid(IUserSession session);

	void logout(IUserSession session);
	
	User getUser(int user_id) throws ManagerException;

	List<User> search(String term);

}
