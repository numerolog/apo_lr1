package apo.managers.conversation;

import java.util.Set;

import apo.managers.conversation.impl.dto.MessageFlag;

public interface IMessage 
{

	int getId();
	int getAuthor_id();
	void setAuthor_id(int author_id);
	String getText();
	Set<MessageFlag> getFlags();
	
}
