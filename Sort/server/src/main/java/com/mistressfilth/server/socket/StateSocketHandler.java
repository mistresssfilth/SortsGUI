package com.mistressfilth.server.socket;

import com.google.gson.Gson;
import com.mistressfilth.server.SortServiceFactory;
import com.mistressfilth.server.bean.SortLog;
import com.mistressfilth.server.bean.SortType;
import com.mistressfilth.server.service.BubbleSortService;
import com.mistressfilth.server.socket.bean.IncomeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class StateSocketHandler extends TextWebSocketHandler {
    private final Logger logger = LoggerFactory.getLogger(StateSocketHandler.class);
    private final Gson gson = new Gson();
    private final SortServiceFactory sortServiceFactory;
    private final Map<WebSocketSession, SortType> states = new ConcurrentHashMap<>();

    public StateSocketHandler(SortServiceFactory sortServiceFactory) {
        this.sortServiceFactory = sortServiceFactory;
    }

    public void sendState(){
        states.forEach((session, sortType) -> {
            try {
                SortLog log = sortServiceFactory.get(sortType).pollLog(sortType.name());
                TextMessage msg = new TextMessage(gson.toJson(log));
                session.sendMessage(msg);
            } catch (IOException e) {
                logger.error(e.getMessage());
                states.remove(session);
            }
            catch (NullPointerException e) {
                logger.info("Sort {}. Queue is empty. " + e.getMessage(), sortType);
            }
        });
    }
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
        IncomeMessage income = gson.fromJson(message.getPayload(), IncomeMessage.class);
        SortType sortType = income.getSortType();
        String target = sortType.name();
        if(income.isPlay()){
            states.put(session, sortType);
            sortServiceFactory.get(sortType).sort(target);
        }
        if(income.isDropped()) {
            states.remove(session);
            sortServiceFactory.get(sortType).drop(target);
        }
    }
}
