package apo.controllers.user;

import apo.controllers.BaseBody;

public class BackRequest extends BaseBody
{

	private String token;
	
	public BackRequest()
	{
		;
	}

	public String getToken() 
	{
		return token;
	}

	public void setToken(String token) 
	{
		this.token = token;
	}
		
}
