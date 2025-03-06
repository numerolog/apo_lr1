package apo.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.ControllerAdvice;

import apo.controllers.BaseRequest;
import apo.controllers.PublicException;
import apo.managers.ManagerException;

@ControllerAdvice
public class GlobalExceptionHandler 
{

	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	
	@MessageExceptionHandler({Throwable.class})
	public void handleException(Throwable t,/* BaseRequest request,*/ SimpMessageHeaderAccessor headers) 
	{
		t.printStackTrace();
		System.err.println("Send error...");
		//System.err.println("request=" + request);
		SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
		headerAccessor.setSessionId(headers.getSessionId());
		headerAccessor.setLeaveMutable(true);
		
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
		System.err.println(headers);
		messagingTemplate.convertAndSendToUser(headers.getSessionId(), "/error", new ErrorResponse(headers.getFirstNativeHeader("request_id"), message), headerAccessor.getMessageHeaders());
	}
	
}
