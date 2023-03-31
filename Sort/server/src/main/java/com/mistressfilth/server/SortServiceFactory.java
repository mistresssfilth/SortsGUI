package com.mistressfilth.server;

import com.mistressfilth.server.bean.SortType;
import com.mistressfilth.server.service.BubbleSortService;
import com.mistressfilth.server.service.QuickSortService;
import com.mistressfilth.server.service.SortService;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class SortServiceFactory {
    private final BubbleSortService bubbleSortService;
    private final QuickSortService quickSortService;
    private static final Map<SortType, SortService> handler = new HashMap<>();

    @Bean
    private void defineHandler(){
        handler.put(SortType.BUBBLE_SORT, bubbleSortService);
        handler.put(SortType.QUICK_SORT, quickSortService);
    }
    public SortServiceFactory(BubbleSortService bubbleSortService, QuickSortService quickSortService) {
        this.bubbleSortService = bubbleSortService;
        this.quickSortService = quickSortService;
    }
    public SortService get(SortType type){
        return handler.get(type);
    }

}
