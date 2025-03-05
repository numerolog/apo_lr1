package apo.managers.command;

import java.util.List;

import apo.server.ServerHandler.ConnectionContext;

public interface ICommandHandler extends ICommandFormer
{

	String getType();

	default boolean sessionRequired() 
	{
		return true;
	}
	
	List<Object> handle(ConnectionContext ctx, String type, String...args) throws Throwable;
	
}
