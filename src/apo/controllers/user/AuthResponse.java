package apo.controllers.user;

import apo.controllers.BaseBody;

public class AuthResponse extends BaseBody
{
	private int id;
	private String token;
	
	public AuthResponse(BaseBody request, int id, String token) 
	{
		super(request);
		this.id = id;
		this.token = token;
	}
		
	public int getId() 
	{
		return id;
	}

	public void setId(int id) 
	{
		this.id = id;
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
