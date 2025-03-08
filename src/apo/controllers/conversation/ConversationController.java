package apo.controllers.conversation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

import apo.controllers.BaseBody;
import apo.controllers.MultiResponse;
import apo.controllers.PublicException;
import apo.managers.conversation.IConversation;
import apo.managers.conversation.IConversationManager;
import apo.managers.conversation.IMessage;
import apo.managers.conversation.impl.dto.MessageFlag;
import apo.server.ConnectionContext;
import jakarta.transaction.Transactional;

@Controller
public class ConversationController 
{
	
	@Autowired
	private IConversationManager conversation_manager;	
	
	@MessageMapping("/conversation/create")
	@SendToUser(destinations="/conversation/create", broadcast=false)
	public ConversationResponse create(ConversationCreateRequest request, ConnectionContext ctx) throws PublicException 
	{
		try 
		{
			IConversation conversation = conversation_manager.create(ctx.session.getUserId(), request.getName());
			return new ConversationResponse(request, conversation);
		} catch (Exception ex)
		{
			throw new PublicException("failed to create conversation", ex);
		}
	}

	@MessageMapping("/conversation/list")
	@SendToUser(destinations="/conversation/list", broadcast=false)
	@Transactional()
	public MultiResponse<ConversationResponse> list(BaseBody request, ConnectionContext ctx) throws PublicException 
	{
		try 
		{
			return new MultiResponse<>(request, conversation_manager.getList(ctx.session.getUserId()).stream().map(conversation -> new ConversationResponse(request, conversation)).toList());
		} catch (Exception ex)
		{
			throw new PublicException("failed list conversations", ex);
		}
	}
	
	@MessageMapping("/conversation/load")
	@SendToUser(destinations="/conversation/load", broadcast=false)
	//@Transactional()
	public MultiResponse<MessageResponse> load(ConversationLoadRequest request, ConnectionContext ctx) throws PublicException 
	{
		try 
		{
			var l = conversation_manager.getList(ctx.session.getUserId(), request.getId(), request.getOffset_from(), request.getOffset_size()).stream().map(message -> new MessageResponse(request, message)).toList();

			for (var msg : l)
				conversation_manager.toggleMessageFlag(ctx.session.getUserId(), request.getId(), msg.getId(), MessageFlag.READEN_FLAG, false);
			return new MultiResponse<>(request, l);
		} catch (Exception ex)
		{
			throw new PublicException("failed list messages in chat " + request.getId(), ex);
		}
	}
	
	@MessageMapping("/conversation/send")
	@SendToUser(destinations="/conversation/send", broadcast=false)
	@Transactional()
	public MessageResponse load(SendMessageRequest request, ConnectionContext ctx) throws PublicException 
	{
		try 
		{
			IMessage message = conversation_manager.putMessage(ctx.session.getUserId(), request.getId(), request.getText());
			return new MessageResponse(request, message);
		} catch (Exception ex)
		{
			throw new PublicException("failed list messages in chat " + request.getId(), ex);
		}
	}	

	@MessageMapping("/conversation/add_remove_member")
	@SendToUser(destinations="/conversation/add_remove_member", broadcast=false)
	@Transactional()
	public BaseBody add_remove_user(AddRemoveMemberRequest request, ConnectionContext ctx) throws PublicException 
	{
		try 
		{
			conversation_manager.addRemoveUser(ctx.session.getUserId(), request.getId(), request.isRemove(), request.getUser_id());
			return new BaseBody(request);
		} catch (Exception ex)
		{
			throw new PublicException("failed to add/remove user in chat " + request.getId(), ex);
		}
	}	

}
