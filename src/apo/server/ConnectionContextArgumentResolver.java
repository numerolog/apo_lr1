package apo.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component
public class ConnectionContextArgumentResolver implements HandlerMethodArgumentResolver 
{

	@Autowired
	private StompListener listener;
	
	@Override
	public boolean supportsParameter(MethodParameter parameter) 
	{
		return ConnectionContext.class.isAssignableFrom(parameter.getParameterType());
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, Message<?> message) throws Exception 
	{
		SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(message);
		
		var ctx = listener.getContext(headers.getSessionId());
		Assert.notNull(ctx, "ctx");
		
		return ctx;
	}

}
