package apo.controllers.user;

import apo.controllers.BaseRequest;

public class AuthResponse extends BaseRequest
{

	private String token;
	
	public AuthResponse(BaseRequest request, String token) 
	{
		super(request);
		this.token = token;
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
