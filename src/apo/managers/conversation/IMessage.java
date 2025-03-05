package apo.managers.conversation;

public interface IMessage 
{

	int SCOPE_USER = 1;

	Object getScopeData(int scope_id);

	int getId();

}
