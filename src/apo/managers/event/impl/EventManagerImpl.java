package apo.managers.event.impl;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import apo.managers.event.IEventManager;
import apo.server.StompListener;

@Component
public class EventManagerImpl implements IEventManager
{

    @Autowired
    private StompListener stomp_listener;
    
    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
    
    
	@Override
	public void noticeNewMessage(int target_user_id, int chat_id, int message_id) 
	{
//TODO:
//		executor.execute(null);, ради демонстрации задержка вставлена
		executor.schedule(()->stomp_listener.getContextsByUserId(target_user_id).forEach(ctx -> ctx.connection.send("/notice/newmessage", new NewMessageNotice(chat_id, message_id))), 500, TimeUnit.MILLISECONDS);
	}

	@Override
	public void noticeAddRemoveConversation(int target_user_id, int chat_id, int user_id, boolean removed) 
	{
		executor.schedule(()->stomp_listener.getContextsByUserId(target_user_id).forEach(ctx -> ctx.connection.send("/notice/addremove_user", new AddRemoveUserNotice(chat_id, user_id, removed))), 500, TimeUnit.MILLISECONDS);
	}

	@Override
	public void noticeAddRemoveMessageFlag(int target_user_id, int chat_id, int message_id, int flag, boolean remove) 
	{
		//TODO: потом конечно можно отправлять flag и remove чтобы поменьше отправлять данных, вместо текущего полного запроса сообщения
		executor.schedule(()->stomp_listener.getContextsByUserId(target_user_id).forEach(ctx -> ctx.connection.send("/notice/addremove_message_flag", new NewMessageNotice(chat_id, message_id))), 500, TimeUnit.MILLISECONDS);
	}
	
}
