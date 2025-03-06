package apo.managers.session;

import apo.managers.ManagerException;

public interface ISessionManager 
{

	IUserSession register(String ip, String login, String password) throws ManagerException;
	
	IUserSession auth(String ip, String login, String password) throws ManagerException;
	IUserSession auth(String ip, String token) throws ManagerException;

	boolean isValid(IUserSession session);

	void logout(IUserSession session);


}
