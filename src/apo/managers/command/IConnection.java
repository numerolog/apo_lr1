package apo.managers.command;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.assertj.core.util.Lists;

public interface IConnection extends ICommandFormer
{

//	default void sendError(String message) throws IOException 
//    {
//		sendCommand("ERRO", message);
//	}
//	
//	default void sendCommand(String type, String...args) throws IOException
//	{
//		sendText(formCommand(type, args));
//	}
//
//	default void sendCommand(String type, Object...args) throws IOException
//	{
//		sendText(formCommand(type, Stream.of(args).map(v->String.valueOf(v))));
//	}
//	
//	default void sendCommand(List<Object> resp) throws IOException
//	{
//		sendText(formCommand(resp.getFirst().toString(), resp.subList(1, resp.size())));
//	}
//
//	void sendText(String text) throws IOException;

	String getIp();
	
}
