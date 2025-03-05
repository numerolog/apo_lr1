package apo.managers.session.impl;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import apo.managers.ManagerException;
import apo.managers.session.ISessionManager;
import apo.managers.session.IUserSession;

@Component
public class SessionManagerImpl implements ISessionManager
{

	static class SessionImpl implements IUserSession
	{
		Integer user_id;
		boolean valid;
		String ip;
		String token;
		
		@Override
		public Integer getUserId() 
		{
			return user_id;
		}

		public boolean isValid() 
		{
			return valid;
		}

		public void invalidate() 
		{
			valid = false;
		}

		@Override
		public String getToken() 
		{
			return token;
		}
		
	}
	
	Map<String, SessionImpl> sessions = new ConcurrentHashMap<>();
	
	@Override
	public IUserSession auth(String ip, String login, String password) throws ManagerException 
	{
		SessionImpl r = new SessionImpl();

		r.ip = ip;
		r.token = UUID.randomUUID().toString();
		
		r.valid = true;
		r.user_id = 1;
		
		sessions.put(r.token, r);
		return r;
	}

	@Override
	public IUserSession auth(String ip, String token) throws ManagerException 
	{
		SessionImpl r = sessions.get(token);
		if (r == null)
			return r;
		
		if (!r.ip.equals(ip))
			throw new ManagerException("bad ip");
						
		return r;
	}
	
	@Override
	public boolean isValid(IUserSession session) 
	{
		return session != null && session instanceof SessionImpl && ((SessionImpl) session).isValid();
	}

	@Override
	public void logout(IUserSession session) 
	{
		((SessionImpl) session).invalidate();
	}

}
