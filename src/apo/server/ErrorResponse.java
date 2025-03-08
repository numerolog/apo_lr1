package apo.server;

import apo.controllers.BaseBody;

public class ErrorResponse extends BaseBody
{

	private String message;
		
	public ErrorResponse(String request_id, String message) 
	{
		super(request_id);
		this.message = message;
	}

	public String getMessage() 
	{
		return message;
	}

	public void setMessage(String message) 
	{
		this.message = message;
	}
		
}
