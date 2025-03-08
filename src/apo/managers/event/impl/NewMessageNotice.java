package apo.managers.event.impl;

public class NewMessageNotice 
{

	private int chat_id;
	private int message_id;

	public NewMessageNotice(int chat_id, int message_id) 
	{
		this.chat_id = chat_id;
		this.message_id = message_id;
	}

	public int getChat_id() 
	{
		return chat_id;
	}

	public void setChat_id(int chat_id) 
	{
		this.chat_id = chat_id;
	}

	public int getMessage_id() 
	{
		return message_id;
	}

	public void setMessage_id(int message_id) 
	{
		this.message_id = message_id;
	}

}
