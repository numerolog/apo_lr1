package apo.managers.conversation;

import java.util.Set;

public interface IConversation 
{

	int getId();
	int getOwnerId();
	Set<Integer> getMembersId();
	
	String getName();

}
