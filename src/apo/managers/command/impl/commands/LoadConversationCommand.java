package apo.managers.command.impl.commands;

import java.util.ArrayList;
import java.util.Collection;
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
public class LoadConversationCommand implements ICommandHandler
{

	@Autowired
	private IConversationManager conversation_manager;

	@Override
	public String getType() 
	{
		return "CHAT";
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
		
		// начиная с какого сообщения
		int offset_from;
		try 
		{
			offset_from = Integer.parseInt(args[1]);
		} catch (Throwable t)
		{
			return formError("bad offset_from");
		}
		
		// сколько сообщений загружать
		int offset_size;
		try 
		{
			offset_size = Integer.parseInt(args[2]);
		} catch (Throwable t)
		{
			return formError("bad offset_size");
		}
			
		try 
		{
			Collection<IMessage> messages = conversation_manager.getList(ctx.session.getUserId(), chat_id, offset_from, offset_size);
			List<Object> resp = new ArrayList<>();
			resp.add(type);
			resp.add(chat_id);
			resp.add(offset_from);
			resp.add(offset_size);
			
			for (var message : messages)
				resp.add(Utils.base64encode(GSONUtils.objectToString(((IMessage) message).getScopeData(IMessage.SCOPE_USER))));
			
			return resp;
		} catch (Exception ex)
		{
			ex.printStackTrace();
			return formError("failed list messages in chat " + chat_id);
		}
	}

}
