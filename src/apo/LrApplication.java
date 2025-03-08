package apo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class LrApplication 
{

	/**
	 * таблицы:
	 *  чатов (id, тип, владелец)
	 *  участников
	 *  сообщений
	 *  прочтений сообщений
	 *  закрепленных сообщений / прав участников чатов / пользователей?
	 */
    
	public static void main(String[] args) 
	{
//		System.setProperty("debug", "true");
		SpringApplication app = new SpringApplication(LrApplication.class);
		app.run(args);
	}

	
}

