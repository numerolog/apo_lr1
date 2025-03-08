package apo.controllers.user;

import apo.controllers.BaseBody;

public class NameRequest extends BaseBody
{

	private int id;
	
	public NameRequest()
	{
		;
	}

	public int getId() 
	{
		return id;
	}

	public void setId(int id) 
	{
		this.id = id;
	}
	
}
