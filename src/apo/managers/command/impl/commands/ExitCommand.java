package apo.managers.command.impl.commands;

import java.util.List;

import org.springframework.stereotype.Component;

import apo.managers.command.ICommandHandler;
import apo.managers.command.ICommandManager;
import apo.server.ServerHandler.ConnectionContext;

@Component
public class ExitCommand implements ICommandHandler
{

	@Override
	public String getType() 
	{
		return ICommandManager.EXIT_COMMAND_TYPE;
	}

	@Override
	public List<String> handle(ConnectionContext ctx, String type, String... args) {
		return null;
	}

}
