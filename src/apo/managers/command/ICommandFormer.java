package apo.managers.command;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.assertj.core.util.Lists;

public interface ICommandFormer 
{

	default String formCommand(String type, Stream<String> args) 
	{
		return ">>" + type + args.collect(Collectors.joining(";")) + "<<";
	}
	
	default String formCommand(String type, List<String> args) 
	{
		return formCommand(type, args.stream());
	}

	default String formCommand(String type, String...args) 
	{
		return formCommand(type, Lists.list(args));
	}
	/*
	default String formError(String message) 
    {
		return formCommand("ERRO", message);
	}*/
	
	default List<String> formError(String message)
	{
		return Lists.list("ERRO", message);
	}

}
