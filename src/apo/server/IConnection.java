package apo.server;

public interface IConnection 
{

	void send(String path, Object object);
	
	String getIp();
	
}
