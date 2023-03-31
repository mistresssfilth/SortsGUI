package com.mistressfilth.server.service;

import com.mistressfilth.server.bean.Line;
import com.mistressfilth.server.bean.SortLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.util.*;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

abstract public class SortService {
    private static final int LIST_CAPACITY = 100;
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    private final Executor executor = Executors.newSingleThreadExecutor();

    @Value("${wait.time}")
    private int WAIT_TIME = 100;
    private final Map<String, BlockingDeque<SortLog>> sortLog = new HashMap<>();
    abstract protected List<Line> sortList(String target, List<Line> list);

    protected void addLog(String target, int i, List<Line> list) {
        sortLog.get(target).addLast(new SortLog(list, i));
        try {
            Thread.sleep(WAIT_TIME);
        } catch (InterruptedException e) {
            logger.error("Sort processing is interrupted/ {}", e.getMessage());
        }
    }
    public SortLog pollLog(String target){
        return sortLog.get(target).poll();
    }
    public void sort(String target){
        if (!sortLog.containsKey(target)){
            List<Line> list = generateList();
            sortLog.put(target, new LinkedBlockingDeque<>());
            executor.execute(() -> {
                sortList(target, list);
            });
        }
    }

    private List<Line> generateList() {
        Random random = new Random();
        List<Line> res = new LinkedList<>();
        for (int i = 0; i < LIST_CAPACITY; i++){
            res.add(new Line(random.nextInt(LIST_CAPACITY)));
        }
        return res;
    }
    public void drop(String target){
        sortLog.remove(target);
    }

}
