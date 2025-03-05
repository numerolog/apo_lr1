package apo.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer 
{

	@Autowired
	private ServerHandler handler;
	
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) 
    {
        registry.addHandler(handler, "/ws").setAllowedOrigins("*");
        //.addInterceptors(new WebSocketInterceptor());
    }

    /*
    @Bean
    public ServerHandler myHandler() 
    {
        return handler;//appContext.getBean(ServerHandler.class);// new ServerHandler();
    }*/
}


