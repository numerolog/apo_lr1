package apo.managers.session.impl;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

import apo.managers.ManagerException;
import apo.managers.session.ISessionManager;
import apo.managers.session.IUserSession;
import apo.managers.session.impl.dto.User;
import apo.managers.session.impl.dto.UserRepository;

@Component
public class SessionManagerImpl implements ISessionManager
{

	static class SessionImpl implements IUserSession
	{
		int user_id;
		String user_name;
		boolean valid;
		String ip;
		String token;
		
		@Override
		public int getUserId() 
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
	public IUserSession register(String ip, String login, String password) throws ManagerException 
	{
		{
			User user = user_repository.findByLogin(login);
			if (user != null)
				throw new ManagerException("user already exists");
		}
		
		User user = new User();
		user.setLogin(login);
		user.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
		
		user_repository.save(user);
		
		return auth(ip, login, password);
	}
	
	@Override
	public IUserSession auth(String ip, String login, String password) throws ManagerException 
	{
		User user = user_repository.findByLogin(login);
		if (user == null)
			throw new ManagerException("user not found");
		
		if (!BCrypt.checkpw(password, user.getPassword()))
			throw new ManagerException("incorrect password");
		
		SessionImpl r = new SessionImpl();

		r.ip = ip;
		r.token = UUID.randomUUID().toString();
		
		r.valid = true;
		r.user_id = user.getId();
		r.user_name = user.getLogin();
		
		sessions.put(r.token, r);
		return r;
	}

	@Override
	public IUserSession auth(String ip, String token) throws ManagerException 
	{
		System.err.println(sessions);
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

	@Override
	public User getUser(int user_id) throws ManagerException 
	{
		var user = user_repository.findById(user_id);
		if (user.isEmpty())
			throw new ManagerException("user not found");
			
		return user.get();
	}

	@Override
	public List<User> search(String term) 
	{
		return user_repository.search(term);
	}

}
