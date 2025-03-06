package apo.controllers.user;

import apo.controllers.BaseRequest;

public class BackRequest extends BaseRequest
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
