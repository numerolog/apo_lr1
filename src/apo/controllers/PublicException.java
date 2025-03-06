package apo.controllers;

public class PublicException extends Exception 
{

	private static final long serialVersionUID = 1L;

    public PublicException(String message) 
    {
        super(message);
    }

	public PublicException(String message, Throwable t) 
	{
		super(message, t);
	}
    
}
