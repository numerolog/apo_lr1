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
public class AuthCommand implements ICommandHandler
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
		return "AUTH";
	}

	@Override
	public List<Object> handle(ConnectionContext ctx, String type, String... args) throws Throwable 
	{
		if (args.length < 2)
    		return formError("No args");
		
		String login = args[0];
		String password = args[1];
		
		try 
		{
			IUserSession session = session_manager.auth(ctx.connection.getIp(), login, password);
			Asserts.notNull(session, "session");
			ctx.session = session;
			//TODO: тут надо бы отдавать токен
			return List.of(type, "OK");
		} catch (Exception ex)
		{
			ex.printStackTrace();
			return formError("failed to auth");
		}
	}

}
