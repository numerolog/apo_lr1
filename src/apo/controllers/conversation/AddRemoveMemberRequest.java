package apo.controllers.conversation;

import apo.controllers.BaseBody;

public class AddRemoveMemberRequest extends BaseBody
{

	private int id;
	private boolean remove;
	private int user_id;
	
	public AddRemoveMemberRequest()
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

	public boolean isRemove() 
	{
		return remove;
	}

	public void setRemove(boolean remove) 
	{
		this.remove = remove;
	}

	public int getUser_id() 
	{
		return user_id;
	}

	public void setUser_id(int user_id) 
	{
		this.user_id = user_id;
	}

	
}
