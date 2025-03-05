package apo;

import javax.sql.DataSource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;


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

