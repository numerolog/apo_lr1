package apo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import apo.server.WebSocketConfig;

@SpringBootApplication
public class LrApplication {

	/**
	 * таблицы:
	 *  чатов (id, тип, владелец)
	 *  участников
	 *  сообщений
	 *  прочтений сообщений
	 *  закрепленных сообщений / прав участников чатов / пользователей?
	 */
    /*@Bean
    public WebSocketConfig webSocketConfig() {
        return new WebSocketConfig();
    }*/
    
	public static void main(String[] args) {
//		System.setProperty("debug", "true");
		SpringApplication app = new SpringApplication(LrApplication.class);
		app.run(args);
	}

}

