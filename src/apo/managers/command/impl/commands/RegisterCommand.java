package apo.managers.command.impl.commands;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import apo.managers.command.ICommandHandler;
import apo.managers.session.ISessionManager;
import apo.managers.session.IUserSession;
import apo.server.ConnectionContext;

@Component
public class RegisterCommand implements ICommandHandler
{

	@Autowired
	private ISessionManager session_manager;

	@Override
	public boolean sessionRequired() 
	{
		return false;
	}
	
	@Override
	public String getType() 
	{
		return "REGU";
	}

	@Override
	public List<Object> handle(ConnectionContext ctx, String type, String... args) throws Throwable 
	{
		if (ctx.session != null)
			return formError("Already authed");
		if (args.length < 2)
    		return formError("No args");
		
		String login = args[0];
		String password = args[1];
		
		try 
		{
			IUserSession session = session_manager.register(ctx.connection.getIp(), login, password);
			Assert.notNull(session, "session");
			ctx.session = session;
			return List.of(type, session.getToken());
		} catch (Exception ex)
		{
			ex.printStackTrace();
			return formError("failed to register");
		}
	}

}
