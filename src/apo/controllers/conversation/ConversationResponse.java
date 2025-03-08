package apo.controllers.conversation;

import java.util.Set;

import apo.controllers.BaseBody;
import apo.managers.conversation.IConversation;

public class ConversationResponse extends BaseBody
{

	private int id;
	private String name;
	private int ownerId;
	private Set<Integer> members;

	public ConversationResponse(BaseBody request, IConversation conversation)
	{
		super(request);
		this.id = conversation.getId();
		this.name = conversation.getName();
		this.ownerId = conversation.getOwnerId();
		this.members = conversation.getMembersId();
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

	public int getOwnerId()
	{
		return ownerId;
	}

	public void setOwnerId(int ownerId)
	{
		this.ownerId = ownerId;
	}

	public Set<Integer> getMembers()
	{
		return members;
	}

	public void setMembers(Set<Integer> members)
	{
		this.members = members;
	}

}
