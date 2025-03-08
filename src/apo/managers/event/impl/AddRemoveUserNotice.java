package apo.managers.event.impl;

public class AddRemoveUserNotice 
{

	private int chat_id;
	private int user_id;
	private boolean removed;

	public AddRemoveUserNotice(int chat_id, int user_id, boolean removed) 
	{
		this.chat_id = chat_id;
		this.user_id = user_id;
		this.removed = removed;
	}

	public int getChat_id() 
	{
		return chat_id;
	}

	public void setChat_id(int chat_id) 
	{
		this.chat_id = chat_id;
	}

	public int getUser_id() 
	{
		return user_id;
	}

	public void setUser_id(int user_id) 
	{
		this.user_id = user_id;
	}

	public boolean isRemoved() 
	{
		return removed;
	}

	public void setRemoved(boolean removed) 
	{
		this.removed = removed;
	}
	
}
