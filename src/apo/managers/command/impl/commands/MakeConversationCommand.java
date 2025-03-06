package apo.managers.command.impl.commands;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import apo.managers.command.ICommandHandler;
import apo.managers.conversation.IConversation;
import apo.managers.conversation.IConversationManager;
import apo.managers.conversation.IMessage;
import apo.server.ConnectionContext;
import apo.utils.Utils;
import apo.utils.dataof.GSONUtils;

@Component
public class MakeConversationCommand implements ICommandHandler
{

	@Autowired
	private IConversationManager conversation_manager;

	@Override
	public String getType() 
	{
		return "MAKE";
	}

	@Override
	public List<Object> handle(ConnectionContext ctx, String type, String... args) throws Throwable 
	{
		try 
		{
			IConversation conversation = conversation_manager.create(ctx.session.getUserId());
			return List.of(type, Utils.base64encode(GSONUtils.objectToString(((IConversation) conversation).getScopeData(IMessage.SCOPE_USER))));
		} catch (Exception ex)
		{
			ex.printStackTrace();
			return formError("failed to create conversation");
		}
	}

}
