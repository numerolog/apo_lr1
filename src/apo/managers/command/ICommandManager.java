package apo.managers.command;

import java.util.List;

import apo.server.ServerHandler.ConnectionContext;

public interface ICommandManager extends ICommandHandler
{

	String EXIT_COMMAND_TYPE = "EXIT";
	String CONNECTED_COMMAND_TYPE = "CONN";
	
	List<Object> handleRaw(ConnectionContext ctx, String rawPacket);

	@Override
	default String getType()
	{
		return null;
	}
	
}
