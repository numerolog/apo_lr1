package apo.server;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import apo.managers.command.IConnection;

@Component
public class StompListener 
{
	private final Map<String, ConnectionContext> connections = new ConcurrentHashMap<>();

//	public static final String HEADER_CLIENT_ID_KEY = "client_id";
//
//	@Autowired
//	private SimpMessagingTemplate messagingTemplate;
	
	@EventListener
    private void handleSessionConnected(SessionConnectEvent event) 
	{
		SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
		System.err.println(headers);
		String ip = String.valueOf(headers.getSessionAttributes().get(ConnectionIpInterceptor.IP_SESSION_ATTRIBUTE_KEY));
//		String client_id = String.valueOf(headers.getFirstNativeHeader(HEADER_CLIENT_ID_KEY));
    	var ctx = new ConnectionContext();
    	ctx.connection = new IConnection() {
			
//			@Override
//			public void sendText(String text) throws IOException 
//			{
//				throw new IOException();
////				messagingTemplate.convertAndSendToUser(text, ip, client_id);
//			}
			
			@Override
			public String getIp() 
			{
				return ip;
			}
		};
		
    	connections.put(headers.getSessionId(), ctx);
    }

    @EventListener
    private void handleSessionDisconnect(SessionDisconnectEvent event) 
    {
		SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
    	ConnectionContext ctx = connections.getOrDefault(headers.getSessionId(), null);
    	
    	connections.remove(headers.getSessionId());
    }
 
    public ConnectionContext getContext(String sessionId)
    {
    	return connections.get(sessionId);
    }
    
}
