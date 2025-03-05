package apo.managers.command;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.assertj.core.util.Lists;

public interface ICommandFormer 
{

	default String formCommand(String type, Stream<Object> args) 
	{
		return ">>" + type + args.map(String::valueOf).collect(Collectors.joining(";")) + "<<";
	}
	
	default String formCommand(String type, List<Object> args) 
	{
		return formCommand(type, args.stream());
	}

	default String formCommand(String type, String...args) 
	{
		if (Lists.list(args).size() != args.length)
			throw new RuntimeException("Problem? нужно поправить");
		return formCommand(type, Lists.list(args));
	}
	/*
	default String formError(String message) 
    {
		return formCommand("ERRO", message);
	}*/
	
	default List<Object> formError(String message)
	{
		return Lists.list("ERRO", message);
	}

}
