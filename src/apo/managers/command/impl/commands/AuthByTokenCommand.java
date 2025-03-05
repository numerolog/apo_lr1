package apo.managers.command.impl.commands;

import java.util.List;

import org.apache.hc.core5.util.Asserts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import apo.managers.command.ICommandHandler;
import apo.managers.session.ISessionManager;
import apo.managers.session.IUserSession;
import apo.server.ServerHandler.ConnectionContext;

@Component
public class AuthByTokenCommand implements ICommandHandler
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
		return "BACK";
	}

	@Override
	public List<Object> handle(ConnectionContext ctx, String type, String... args) throws Throwable 
	{
		if (ctx.session != null)
			return formError("Already authed");
		if (args.length < 1)
    		return formError("No args");
		
		String token = args[0];
		
		try 
		{
			IUserSession session = session_manager.auth(ctx.connection.getIp(), token);
			Asserts.notNull(session, "session");
			ctx.session = session;
			return List.of(type, session.getToken());
		} catch (Exception ex)
		{
			ex.printStackTrace();
			return formError("failed to auth");
		}
	}

}
