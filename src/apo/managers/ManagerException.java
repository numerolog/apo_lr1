package apo.managers;

public class ManagerException extends Exception 
{

	private static final long serialVersionUID = 1L;

    public ManagerException(String message) 
    {
        super(message);
    }
    
	public ManagerException(String message, Throwable t) 
	{
		super(message, t);
	}
    
}
