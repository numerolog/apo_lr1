package apo.managers.command.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import apo.managers.command.ICommandHandler;
import apo.managers.command.ICommandManager;
import apo.managers.command.impl.commands.AddRemoveUserToConversationCommand;
import apo.managers.command.impl.commands.AuthByCredentialsCommand;
import apo.managers.command.impl.commands.AuthByTokenCommand;
import apo.managers.command.impl.commands.ExitCommand;
import apo.managers.command.impl.commands.LoadConversationCommand;
import apo.managers.command.impl.commands.LoadConversationsCommand;
import apo.managers.command.impl.commands.MakeConversationCommand;
import apo.managers.command.impl.commands.SendMessageCommand;
import apo.managers.session.ISessionManager;
import apo.server.ServerHandler.ConnectionContext;
import jakarta.annotation.PostConstruct;

@Component
public class CommandManagerImpl implements ICommandManager
{
	@Autowired
	private ApplicationContext appContext;

	@Autowired
	private ISessionManager session_manager;
	
	Map<String, ICommandHandler> handlers = new HashMap<>();
	@SuppressWarnings({ "unchecked" })
	@PostConstruct
	void postInit()
	{
		System.err.println("Add commands...");
		//TODO: глянуть в DI уже наверное есть такое, лел
		for (Class<? extends ICommandHandler> clz : new Class[]
			{
				ExitCommand.class,
				AuthByCredentialsCommand.class,
				AuthByTokenCommand.class,
				LoadConversationsCommand.class,
				MakeConversationCommand.class,
				LoadConversationCommand.class,
				SendMessageCommand.class,
				AddRemoveUserToConversationCommand.class,
			}) 
		{	
				System.err.println("Add command " + clz + "...");
				ICommandHandler handler = appContext.getBean(clz);
				if (handler == null)
					throw new RuntimeException();
				handlers.put(handler.getType(), handler);
		}
		System.err.println("Commands added!");
	}
	
	@Override
	public List<Object> handleRaw(ConnectionContext ctx, String command) 
	{
    	if (command.length()<8)
    	{
    		return formError("bad command length");
    	}
    	
    	if (!">>".equals(command.substring(0, 2)))
    	{
    		return formError("bad command header");
    	}
    	
    	if (!"<<".equals(command.substring(command.length() - 2, command.length())))
    	{
    		return formError("bad command footer");
    	}
    	
    	String type = command.substring(2, 6);
    	String argss = command.length() > 8 ? command.substring(6, command.length() - 2) : "";
    	
    	System.err.println("recv " + type + " : " + argss);
    	
    	String[] args = argss.split(Pattern.quote(";"));
    	
		var r = handle(ctx, type, args);
		
		if (r == null)
    		return formError("command no result");
		
		return r;
	}

	@Override
	public List<Object> handle(ConnectionContext ctx, String type, String... args) 
	{
		var handler = handlers.get(type);
		if (handler == null)
		{
			return formError("unknown command");
		}
		
		if (handler.sessionRequired() && !session_manager.isValid(ctx.session))
			return formError("auth requred");
			
		try 
		{
			return handler.handle(ctx, type, args);
		} catch (Throwable e) 
		{
			e.printStackTrace();
			return formError("failed to perform");
		}
	}
    
}
