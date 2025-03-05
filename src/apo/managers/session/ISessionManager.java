package apo.managers.session;

public interface ISessionManager 
{

	IUserSession auth(String ip, String login, String password);

	boolean isValid(IUserSession session);

	void logout(IUserSession session);

}
