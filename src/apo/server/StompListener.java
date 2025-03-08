package apo.server;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class StompListener 
{
	private final Map<String, ConnectionContext> connections = new ConcurrentHashMap<>();

	@Autowired
	@Lazy
	private SimpMessagingTemplate messagingTemplate;
	
	@EventListener
    private void handleSessionConnected(SessionConnectEvent event) 
	{
		SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
		System.err.println(headers);
		String ip = String.valueOf(headers.getSessionAttributes().get(ConnectionIpInterceptor.IP_SESSION_ATTRIBUTE_KEY));
		String session_id = headers.getSessionId();
    	var ctx = new ConnectionContext();
    	ctx.connection = new IConnection() 
    	{
			
			@Override
			public String getIp() 
			{
				return ip;
			}

			@Override
			public void send(String path, Object object) 
			{
				SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
				headerAccessor.setSessionId(session_id);
				headerAccessor.setLeaveMutable(true);
				
				messagingTemplate.convertAndSendToUser(session_id, path, object, headerAccessor.getMessageHeaders());
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

	public List<ConnectionContext> getContextsByUserId(int user_id) 
	{
		return connections.entrySet().stream().filter(pair -> pair.getValue().connection != null && pair.getValue().session != null && pair.getValue().session.getUserId() == user_id).map(Entry::getValue).toList();
	}
    
}
