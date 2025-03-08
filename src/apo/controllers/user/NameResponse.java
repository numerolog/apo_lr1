package apo.controllers.user;

import apo.controllers.BaseBody;
import apo.managers.session.impl.dto.User;

public class NameResponse extends BaseBody
{

	private int id;
	private String name;
	
	public NameResponse(BaseBody request, User user) 
	{
		super(request);
		this.id = user.getId();
		this.name = user.getLogin();
	}
	
	public int getId() 
	{
		return id;
	}

	public void setId(int id) 
	{
		this.id = id;
	}

	public String getName() 
	{
		return name;
	}

	public void setName(String name) 
	{
		this.name = name;
	}
	
}
