package apo.server;

import apo.controllers.BaseRequest;

public class ErrorResponse extends BaseRequest
{

	private String message;
		
//	public ErrorResponse(String message) 
//	{
//		super();
//		this.message = message;
//	}

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
