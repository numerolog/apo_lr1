package apo.managers.command.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import apo.managers.command.ICommandFormer;
import apo.managers.command.ICommandHandler;
import apo.managers.command.ICommandManager;
import apo.managers.command.impl.commands.ExitCommand;
import apo.managers.session.ISessionManager;
import apo.server.ServerHandler.ConnectionContext;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;

@Component
public class CommandManagerImpl implements ICommandManager, ICommandFormer
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
		/*
		new ArrayList<Class<? extends ICommandHandler>>() {
			{
				add(ExitCommand.class);
			}
		}.*/
		for(Class<? extends ICommandHandler> clz : new Class[]{
				ExitCommand.class
				}) {	
			//forEach(clz -> {
				System.err.println("Add command " + clz + "...");
				ICommandHandler handler = appContext.getBean(clz);
				if (handler == null)
					throw new RuntimeException();
				handlers.put(handler.getType(), handler);
			//});
		}
		System.err.println("Commands added!");
	}
	
	@Override
	public List<String> handleRaw(ConnectionContext ctx, String command) 
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
    		return formError("bad comand footer");
    	}
    	
    	String type = command.substring(2, 6);
    	String argss = command.length() > 8 ? command.substring(6, command.length() - 2) : "";
    	String[] args = argss.split(Pattern.quote(";"));
    	
		return handle(ctx, type, args);
	}

	@Override
	public List<String> handle(ConnectionContext ctx, String type, String... args) 
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
	
	
//    private ISessionManager session_manager;
//    private IConversationManager conversation_manager;
//    
//	@Override
//	public String handle(ConnectionContext ctx, TextMessage textMessage) {
//		String resp;
//    	try 
//    	{
//    		resp = handleTextMessage0(s, ctx, textMessage);
//    	} catch (Throwable t)
//    	{
//    		t.printStackTrace();
//    		resp = formError("failed to process command");
//    	}
//	}
//	
//
//    protected String handleTextMessage0(WebSocketSession s, ConnectionContext ctx, TextMessage textMessage) throws Exception 
//    {
//    	String command = textMessage.getPayload();

//    	System.err.println("recv " + type + " : " + argss);
//    	
//    	switch (type)
//    	{
//    		case "AUTH":
//    		{
//				if (args.length < 2)
//		    		return formError("No args");
//				String login = args[0];
//				String password = args[1];
//				
//				try 
//				{
//					IUserSession session = session_manager.auth(s.getRemoteAddress().getHostName(), login, password);
//					ctx.session = session;
//					return formCommand("AUTH", "OK");
//				} catch (Exception ex)
//				{
//					ex.printStackTrace();
//					return formError("failed to auth");
//				}
//    		}
//    	}
//
//		if (!session_manager.isValid(ctx.session))
//			return formError("invalid session");
//		
//		switch (type)
//		{
//    		// загрузить список чатов
//    		case "CNVS":
//    		{
//    			try 
//    			{
//					Collection<IConversation> conversations = conversation_manager.getList(ctx.session.getUserId());
//					String resp = "";
//					for (var conversation : conversations)
//					{
//						resp += Utils.base64encode(GSONUtils.objectToString(conversation.getScopeData(IConversation.SCOPE_USER))) + ",";
//					}
//					
//					return formCommand("CNVS", resp);
//				} catch (Exception ex)
//				{
//					ex.printStackTrace();
//					return formError("failed list conversations");
//				}
//    		}
//
//    		// создать чат
//    		case "MAKE":
//    		{
//    			try 
//    			{
//    				IConversation conversation = conversation_manager.create(ctx.session.getUserId());
//					return formCommand("MAKE", Utils.base64encode(GSONUtils.objectToString(((IConversation) conversation).getScopeData(IMessage.SCOPE_USER))));
//				} catch (Exception ex)
//				{
//					ex.printStackTrace();
//					return formError("failed to create conversation");
//				}
//    		}
//    		
//			// Загрузить выбранный чат
//    		case "CHAT":
//    		{
//				if (args.length < 3)
//		    		return formError("No args");
//				
//				int chat_id;
//				try 
//				{
//					chat_id = Integer.parseInt(args[0]);
//				} catch (Throwable t)
//				{
//		    		return formError("bad chat_id");
//				}
//				
//				// начиная с какого сообщения
//				int offset_from;
//				try 
//				{
//					offset_from = Integer.parseInt(args[1]);
//				} catch (Throwable t)
//				{
//		    		return formError("bad offset_from");
//				}
//				
//				// сколько сообщений загружать
//				int offset_size;
//				try 
//				{
//					offset_size = Integer.parseInt(args[2]);
//				} catch (Throwable t)
//				{
//		    		return formError("bad offset_size");
//				}
//					
//    			try 
//    			{
//					Collection<IMessage> messages = conversation_manager.getList(ctx.session.getUserId(), chat_id, offset_from, offset_size);
//					String resp = "";
//					resp += chat_id + "," + offset_from + "," + offset_size + ",";
//					for (var message : messages)
//					{
//						resp += Utils.base64encode(GSONUtils.objectToString(((IConversation) message).getScopeData(IMessage.SCOPE_USER))) + ",";
//					}
//					
//					return formCommand("CHAT", resp);
//				} catch (Exception ex)
//				{
//					ex.printStackTrace();
//					return formError("failed list messages in chat " + chat_id);
//				}
//    		}
//			
//			// Отправить в выбранный чат
//    		case "SEND":
//    		{
//				if (args.length < 2)
//		    		return formError("No args");
//
//				int chat_id;
//				try 
//				{
//					chat_id = Integer.parseInt(args[0]);
//				} catch (Throwable t)
//				{
//		    		return formError("bad chat_id");
//				}
//				
//				String message_text = Utils.base64decode(args[1]);
//    			try 
//    			{
//					IMessage message = conversation_manager.putMessage(ctx.session.getUserId(), chat_id, message_text);
//
//					return formCommand("SEND", Utils.base64encode(GSONUtils.objectToString(((IConversation) message).getScopeData(IMessage.SCOPE_USER))));
//				} catch (Exception ex)
//				{
//					ex.printStackTrace();
//					return formError("failed to put message to chat " + chat_id);
//				}
//				
//    		}
//
//			// Добавить/удалить участника в чат
//    		case "ADDU":
//    		{
//				if (args.length < 3)
//		    		return formError("No args");
//
//				int chat_id;
//				try 
//				{
//					chat_id = Integer.parseInt(args[0]);
//				} catch (Throwable t)
//				{
//		    		return formError("bad chat_id");
//				}
//
//				int user_id;
//				try 
//				{
//					user_id = Integer.parseInt(args[1]);
//				} catch (Throwable t)
//				{
//		    		return formError("bad chat_id");
//				}
//
//				boolean remove = "remove".equals(args[2]);
//				
//    			try 
//    			{
//					conversation_manager.addRemoveUser(ctx.session.getUserId(), chat_id, remove, user_id);
//
//					return formCommand("ADDU", "OK");
//				} catch (Exception ex)
//				{
//					ex.printStackTrace();
//					return formError("failed to add/remove user in chat " + chat_id);
//				}
//				
//    		}
//    		
//    		default:
//	    		return formError("No supported type " + type);
//    	}
//    }

    
}
