package apo.managers.session.impl;

import org.springframework.stereotype.Component;

import apo.managers.session.ISessionManager;
import apo.managers.session.IUserSession;

@Component
public class SessionManagerImpl implements ISessionManager
{

	@Override
	public IUserSession auth(String ip, String login, String password)
	{
		return null;
	}

	@Override
	public boolean isValid(IUserSession session) 
	{
		return false;
	}

	@Override
	public void logout(IUserSession session) 
	{
		
	}

}
