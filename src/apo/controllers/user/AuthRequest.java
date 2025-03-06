package apo.controllers.user;

import apo.controllers.BaseRequest;

public class AuthRequest extends BaseRequest
{

	private String login;
	private String password;
	
	public AuthRequest()
	{
		;
	}
	
	public String getLogin() 
	{
		return login;
	}
	
	public void setLogin(String login) 
	{
		this.login = login;
	}
	
	public String getPassword() 
	{
		return password;
	}
	
	public void setPassword(String password) 
	{
		this.password = password;
	}
	
}
