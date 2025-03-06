package apo.controllers;

public class BaseRequest 
{

	private String request_id;
	
	public BaseRequest()
	{
		;
	}
	
	public BaseRequest(String request_id) 
	{
		super();
		this.request_id = request_id;
	}

	public BaseRequest(BaseRequest request) 
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
