package apo.controllers.conversation;

import apo.controllers.BaseBody;

public class ConversationLoadRequest extends BaseBody
{

	private int id;
	private int offset_from;
	private int offset_size;
	
	public ConversationLoadRequest()
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

	public int getOffset_from() 
	{
		return offset_from;
	}

	public void setOffset_from(int offset_from) 
	{
		this.offset_from = offset_from;
	}

	public int getOffset_size() 
	{
		return offset_size;
	}

	public void setOffset_size(int offset_size) 
	{
		this.offset_size = offset_size;
	}

}
