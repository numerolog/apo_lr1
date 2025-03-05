package apo.managers.command.impl.commands;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import apo.managers.command.ICommandHandler;
import apo.managers.conversation.IConversation;
import apo.managers.conversation.IConversationManager;
import apo.server.ServerHandler.ConnectionContext;
import apo.utils.Utils;
import apo.utils.dataof.GSONUtils;

@Component
public class LoadConversationsCommand implements ICommandHandler
{

	@Autowired
	private IConversationManager conversation_manager;

	@Override
	public String getType() 
	{
		return "CNVS";
	}

	@Override
	public List<Object> handle(ConnectionContext ctx, String type, String... args) throws Throwable 
	{
		try 
		{
			Collection<IConversation> conversations = conversation_manager.getList(ctx.session.getUserId());
			List<Object> resp = new ArrayList<>();
			resp.add(type);
			
			for (var conversation : conversations)
				resp.add(Utils.base64encode(GSONUtils.objectToString(conversation.getScopeData(IConversation.SCOPE_USER))));
			
			return resp;
		} catch (Exception ex)
		{
			ex.printStackTrace();
			return formError("failed list conversations");
		}
	}

}
