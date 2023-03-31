package com.mistressfilth.server.socket.bean;

import com.mistressfilth.server.bean.SortType;

public class IncomeMessage {
    private SortType sortType;
    private boolean dropped;
    private boolean play;

    public SortType getSortType() {
        return sortType;
    }

    public boolean isDropped() {
        return dropped;
    }

    public boolean isPlay() {
        return play;
    }
}
