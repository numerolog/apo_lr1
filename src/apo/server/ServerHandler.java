package apo.server;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import apo.managers.command.ICommandManager;
import apo.managers.command.IConnection;
import apo.managers.session.IUserSession;

@Component
public class ServerHandler extends TextWebSocketHandler 
{

	public static class ConnectionContext
	{
		public IConnection connection;
		public IUserSession session;
	}
	
	public final Map<WebSocketSession, ConnectionContext> connections = new ConcurrentHashMap<>();

    @Autowired
    private ICommandManager command_manager;
    
    public static class WSConnection implements IConnection
    {

		private WebSocketSession s;

		public WSConnection(WebSocketSession s) 
		{
			this.s = s;
		}

		@Override
		public void sendText(String text) throws IOException 
		{
			s.sendMessage(new TextMessage(text));
		}

		@Override
		public String getIp() 
		{
			return s.getRemoteAddress().getHostName();
		}

    }
    
    @Override
    public void afterConnectionEstablished(WebSocketSession s) throws Exception 
    {
    	var ctx = new ConnectionContext();
    	ctx.connection = new WSConnection(s);
    	connections.put(s, ctx);
        System.out.println("Connection established");
        ctx.connection.sendCommand(ICommandManager.CONNECTED_COMMAND_TYPE);
    }

    @Override
    protected void handleTextMessage(WebSocketSession s, TextMessage textMessage) throws Exception 
    {
    	ConnectionContext ctx = connections.getOrDefault(s, null);
    	if (ctx == null)
    	{
    		return;
    	}
    	
    	List<Object> resp = command_manager.handleRaw(ctx, textMessage.getPayload());
    	System.err.println("send " + resp);
    	ctx.connection.sendCommand(resp);
    }


	@Override
    public void afterConnectionClosed(WebSocketSession s, CloseStatus status) throws Exception 
	{
    	ConnectionContext ctx = connections.getOrDefault(s, null);
    	/*try {
			command_manager.handle(ctx, ICommandManager.FREE_COMMAND_TYPE);
		} catch (Throwable e) 
    	{
			e.printStackTrace();
		}*/
    	connections.remove(s);
        System.out.println("Connection closed");
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception 
    {
        System.out.println("Transport error");
    }

    @Override
    public boolean supportsPartialMessages() 
    {
        return false;
    }
    
}
