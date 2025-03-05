package apo.managers.event.impl;

import org.springframework.stereotype.Component;

import apo.managers.event.IEventManager;

@Component
public class EventManagerImpl implements IEventManager
{

//    @Autowired
//    private ServerHandler server_handler;
    
	@Override
	public void noticeNewMessage(int target_user_id, int chat_id, int message_id) 
	{
//		server_handler.connections.entrySet().stream().filter(pair -> pair.getValue().session != null && pair.getValue().session.getUserId() == target_user_id).forEach(pair -> {
//			try 
//			{
//				pair.getValue().connection.sendCommand("NNMS", chat_id, message_id);
//			} catch (IOException e) 
//			{
//				e.printStackTrace();
//			}
//		});
	}

	@Override
	public void noticeAddRemoveConversation(int target_user_id, int chat_id, int user_id, boolean removed) 
	{
//		server_handler.connections.entrySet().stream().filter(pair -> pair.getValue().session != null && pair.getValue().session.getUserId() == target_user_id).forEach(pair -> {
//			try 
//			{
//				pair.getValue().connection.sendCommand("NACU", chat_id, user_id, (removed ? "removed" : "added"));
//			} catch (IOException e) 
//			{
//				e.printStackTrace();
//			}
//		});
	}

	@Override
	public void noticeAddRemoveMessageFlag(int user_id, int chat_id, int message_id, int flag, boolean remove) 
	{
		// TODO: impl NAMF
	}
	
}
