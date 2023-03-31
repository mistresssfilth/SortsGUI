package com.mistressfilth.server.bean;

public class Line {
    private int val;

    public Line() {
    }

    public Line(int val) {
        this.val = val;
    }

    public int getVal() {
        return val;
    }

    public boolean compare(Line other){
        return this.getVal() > other.getVal();
    }


}
