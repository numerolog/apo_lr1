package apo.managers.command.impl.commands;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import apo.managers.command.ICommandHandler;
import apo.managers.command.ICommandManager;
import apo.managers.session.ISessionManager;
import apo.server.ServerHandler.ConnectionContext;

@Component
public class ExitCommand implements ICommandHandler
{

	@Autowired
	private ISessionManager session_manager;
	
	@Override
	public String getType() 
	{
		return ICommandManager.EXIT_COMMAND_TYPE;
	}

	@Override
	public List<Object> handle(ConnectionContext ctx, String type, String... args) 
	{
		session_manager.logout(ctx.session);
		ctx.session = null;
		return List.of(type, "OK");
	}

}
