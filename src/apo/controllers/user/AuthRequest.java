package apo.controllers.user;

import apo.controllers.BaseBody;

public class AuthRequest extends BaseBody
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
