package apo.managers.command.impl.commands;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import apo.managers.command.ICommandHandler;
import apo.managers.conversation.IConversation;
import apo.managers.conversation.IConversationManager;
import apo.managers.conversation.IMessage;
import apo.server.ServerHandler.ConnectionContext;
import apo.utils.Utils;
import apo.utils.dataof.GSONUtils;

@Component
public class SendMessageCommand implements ICommandHandler
{

	@Autowired
	private IConversationManager conversation_manager;

	@Override
	public String getType() 
	{
		return "SEND";
	}

	@Override
	public List<Object> handle(ConnectionContext ctx, String type, String... args) throws Throwable 
	{
		if (args.length < 2)
			return formError("No args");
	
		int chat_id;
		try 
		{
			chat_id = Integer.parseInt(args[0]);
		} catch (Throwable t)
		{
			return formError("bad chat_id");
		}
		
		String message_text = Utils.base64decode(args[1]);
		try 
		{
			IMessage message = conversation_manager.putMessage(ctx.session.getUserId(), chat_id, message_text);
	
			return List.of(type, Utils.base64encode(GSONUtils.objectToString(((IConversation) message).getScopeData(IMessage.SCOPE_USER))));
		} catch (Exception ex)
		{
			ex.printStackTrace();
			return formError("failed to put message to chat " + chat_id);
		}
	}

}
