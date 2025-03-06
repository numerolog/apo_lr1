package apo.managers.command.impl.commands;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import apo.managers.command.ICommandHandler;
import apo.managers.conversation.IConversationManager;
import apo.server.ConnectionContext;

@Component
public class AddRemoveUserToConversationCommand implements ICommandHandler
{

	@Autowired
	private IConversationManager conversation_manager;

	@Override
	public String getType() 
	{
		return "ADDU";
	}

	@Override
	public List<Object> handle(ConnectionContext ctx, String type, String... args) throws Throwable 
	{
		if (args.length < 3)
			return formError("No args");
	
		int chat_id;
		try 
		{
			chat_id = Integer.parseInt(args[0]);
		} catch (Throwable t)
		{
			return formError("bad chat_id");
		}
	
		int user_id;
		try 
		{
			user_id = Integer.parseInt(args[1]);
		} catch (Throwable t)
		{
			return formError("bad chat_id");
		}
	
		boolean remove = "remove".equals(args[2]);
		
		try 
		{
			conversation_manager.addRemoveUser(ctx.session.getUserId(), chat_id, remove, user_id);
	
			return List.of(this, "OK");
		} catch (Exception ex)
		{
			ex.printStackTrace();
			return formError("failed to add/remove user in chat " + chat_id);
		}
		
	}

}
