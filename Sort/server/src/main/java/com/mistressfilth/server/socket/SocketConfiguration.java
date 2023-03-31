package com.mistressfilth.server.socket;

import com.mistressfilth.server.SortServiceFactory;
import com.mistressfilth.server.service.BubbleSortService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableScheduling
@EnableWebSocket
public class SocketConfiguration implements WebSocketConfigurer {
    private final StateSocketHandler handler;

    public SocketConfiguration(SortServiceFactory sortServiceFactory) {
        this.handler = new StateSocketHandler(sortServiceFactory);
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(handler, "/websocket").setAllowedOrigins("*");
    }
    @Scheduled(fixedRate = 10)
    private void sendStates(){
        handler.sendState();

    }
}
