package apo.controllers;

public class BaseBody 
{

	private String request_id;
	
	public BaseBody()
	{
		;
	}
	
	public BaseBody(String request_id) 
	{
		super();
		this.request_id = request_id;
	}

	public BaseBody(BaseBody request) 
	{
		this(request != null ? request.request_id : "__NO_REQUEST__");
	}

	public String getRequest_id() 
	{
		return request_id;
	}

	public void setRequest_id(String request_id) 
	{
		this.request_id = request_id;
	}

	@Override
	public String toString() 
	{
		return "BaseRequest [request_id=" + request_id + "]";
	}
	
}
