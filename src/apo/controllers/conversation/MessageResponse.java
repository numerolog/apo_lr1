package apo.controllers.conversation;

import apo.controllers.BaseBody;
import apo.managers.conversation.IMessage;
import apo.managers.conversation.impl.dto.MessageFlag;

public class MessageResponse extends BaseBody
{

	private int id;
	private int author_id;
	private String text;
	private boolean readen;

	public MessageResponse(BaseBody request, IMessage message)
	{
		super(request);
		this.id = message.getId();
		this.author_id = message.getAuthor_id();
		this.text = message.getText();
		this.readen = message.getFlags().stream().filter(flag -> flag.flag == MessageFlag.READEN_FLAG).findAny()
				.isPresent();
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public int getAuthor_id()
	{
		return author_id;
	}

	public void setAuthor_id(int author_id)
	{
		this.author_id = author_id;
	}

	public String getText()
	{
		return text;
	}

	public void setText(String text)
	{
		this.text = text;
	}

	public boolean isReaden()
	{
		return readen;
	}

	public void setReaden(boolean readen)
	{
		this.readen = readen;
	}

}
