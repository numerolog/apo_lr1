package apo.controllers.conversation;

import apo.controllers.BaseBody;

public class SendMessageRequest extends BaseBody
{

	private int id;
	private String text;
	
	public SendMessageRequest()
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

	public String getText() 
	{
		return text;
	}

	public void setText(String text) 
	{
		this.text = text;
	}
	
}
