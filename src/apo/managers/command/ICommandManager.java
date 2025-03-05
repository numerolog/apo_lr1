package apo.managers.command;

import java.util.List;

import apo.server.ServerHandler.ConnectionContext;

public interface ICommandManager extends ICommandHandler
{
	
	// выход(удаление) из сессии (вернуться нельзя)
	String EXIT_COMMAND_TYPE = "EXIT";
	/*
	//TODO: для входа в другую сессию, без удаления старой (к старой можно будет вернуться по токену)
	String FREE_COMMAND_TYPE = "FREE";*/
	// отправляется в качестве приветствия при установке соединения 
	String CONNECTED_COMMAND_TYPE = "CONN";
	
	List<Object> handleRaw(ConnectionContext ctx, String rawPacket);

	@Override
	default String getType()
	{
		return null;
	}
	
}
