package apo.managers.session.impl;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import apo.managers.ManagerException;
import apo.managers.session.ISessionManager;
import apo.managers.session.IUserSession;
import apo.managers.session.impl.dto.User;
import apo.managers.session.impl.dto.UserRepository;
import jakarta.annotation.PostConstruct;

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

	@Autowired
	private UserRepository user_repository;
	
	Map<String, SessionImpl> sessions = new ConcurrentHashMap<>();

	@Override
	public IUserSession register(String ip, String login, String password) 
	{
		return null;
	}
	
	@Override
	public IUserSession auth(String ip, String login, String password) throws ManagerException 
	{
		User user = user_repository.findByLogin(login);
		if (user == null)
			throw new ManagerException("user not found");
		if (!user.getPassword().equals(password))
			throw new ManagerException("incorrect password");
		
		SessionImpl r = new SessionImpl();

		r.ip = ip;
		r.token = UUID.randomUUID().toString();
		
		r.valid = true;
		r.user_id = user.getId();
		
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
	
	@PostConstruct
	void postInit()
	{
		var found = user_repository.findByLogin("test");
		if (found == null)
		{
			found = new User();
			found.setLogin("test");
			found.setPassword("password");
			user_repository.save(found);
		}
	}
	
}
