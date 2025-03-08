package apo.controllers.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;

import apo.controllers.BaseBody;
import apo.controllers.MultiResponse;
import apo.controllers.PublicException;
import apo.managers.session.ISessionManager;
import apo.managers.session.IUserSession;
import apo.server.ConnectionContext;

@Controller
public class UserController 
{

	@Autowired
	private ISessionManager session_manager;

	// тут лучше /userctl не писать, иначе путаница с "гениальным" stomp'овским "/user/"
	@MessageMapping("/uctl/register")
	@SendToUser(destinations="/uctl/register", broadcast=false)
	public AuthResponse register(AuthRequest request, ConnectionContext ctx) throws PublicException 
	{
		try 
		{
			IUserSession session = session_manager.register(ctx.connection.getIp(), request.getLogin(), request.getPassword());
			Assert.notNull(session, "session");
			ctx.session = session;
			return new AuthResponse(request, session.getUserId(), session.getToken());
		} catch (Throwable t)
		{
			throw new PublicException("Failed to register", t);
		}
	}

	@MessageMapping("/uctl/login")
	@SendToUser(destinations="/uctl/login", broadcast=false)
	public AuthResponse login(AuthRequest request, ConnectionContext ctx) throws PublicException 
	{
		try 
		{
			IUserSession session = session_manager.auth(ctx.connection.getIp(), request.getLogin(), request.getPassword());
			Assert.notNull(session, "session");
			ctx.session = session;
			var r = new AuthResponse(request, session.getUserId(), session.getToken());
			System.err.println("reps " + request);
			return r;
		} catch (Throwable t)
		{
			throw new PublicException("Failed to auth", t);
		}
	}

	@MessageMapping("/uctl/back")
	@SendToUser(destinations="/uctl/back", broadcast=false)
	public AuthResponse back(BackRequest request, ConnectionContext ctx) throws PublicException 
	{
		try 
		{
			IUserSession session = session_manager.auth(ctx.connection.getIp(), request.getToken());
			Assert.notNull(session, "session");
			ctx.session = session;
			return new AuthResponse(request, session.getUserId(), session.getToken());
		} catch (Throwable t)
		{
			throw new PublicException("Failed to back", t);
		}
	}
	
	@MessageMapping("/uctl/logout")
	@SendToUser(destinations="/uctl/logout", broadcast=false)
	public BaseBody logout(BaseBody request, ConnectionContext ctx) throws PublicException 
	{
		try 
		{
			session_manager.logout(ctx.session);
			ctx.session = null;
			return new BaseBody(request);
		} catch (Throwable t)
		{
			throw new PublicException("Failed to logout", t);
		}
	}

	@MessageMapping("/uctl/name")
	@SendToUser(destinations="/uctl/name", broadcast=false)
	public NameResponse getName(NameRequest request, ConnectionContext ctx) throws PublicException 
	{
		try 
		{
			return new NameResponse(request, session_manager.getUser(request.getId()));
		} catch (Throwable t)
		{
			throw new PublicException("Failed to logout", t);
		}
	}
	
	@MessageMapping("/uctl/search")
	@SendToUser(destinations="/uctl/search", broadcast=false)
	public MultiResponse<NameResponse> search(SearchRequest request, ConnectionContext ctx) throws PublicException 
	{
		try 
		{
			return new MultiResponse<>(request, session_manager.search(request.getTerm()).stream().map(user -> new NameResponse(request, user)).toList());
		} catch (Throwable t)
		{
			throw new PublicException("Failed to search", t);
		}
	}


}
