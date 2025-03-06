package apo.server;

import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

public class ConnectionIpInterceptor implements HandshakeInterceptor
{

	public static final String IP_SESSION_ATTRIBUTE_KEY = "ip";

	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
			WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception 
	{
		attributes.put(IP_SESSION_ATTRIBUTE_KEY, request.getRemoteAddress().getAddress().getHostAddress());
		return true;
	}

	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
			WebSocketHandler wsHandler, Exception exception) 
	{
		;
	}
	
}
