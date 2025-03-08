package apo.server;

import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.bind.annotation.ControllerAdvice;

import apo.controllers.PublicException;
import apo.managers.ManagerException;

@ControllerAdvice
public class GlobalExceptionHandler 
{
	
	@MessageExceptionHandler({Throwable.class})
	public void handleException(Throwable t, SimpMessageHeaderAccessor headers, ConnectionContext ctx) 
	{
		t.printStackTrace();
		System.err.println("Send error...");
		
		String message;
		switch (t)
		{
			case ManagerException e:
				message = "manager error";
			break;
			case PublicException e:
				message = e.getMessage();
			break;
			default:
				message = "unknown";
		};
		
		ctx.connection.send("/error", new ErrorResponse(headers.getFirstNativeHeader("request_id"), message));
	}
	
}
