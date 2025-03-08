package apo.controllers.conversation;

import apo.controllers.BaseBody;

public class ConversationCreateRequest extends BaseBody
{

	private String name;
	
	public ConversationCreateRequest()
	{
		;
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
