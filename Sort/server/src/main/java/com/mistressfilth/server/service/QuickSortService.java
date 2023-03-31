package com.mistressfilth.server.service;

import com.mistressfilth.server.bean.Line;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuickSortService extends SortService{
    @Override
    protected List<Line> sortList(String target, List<Line> list) {
        return sortList(target, list, 0, list.size() - 1);
    }
    private List<Line> sortList(String target, List<Line> list, int left, int right){
        if (left < right){
            int partitionIndex = partition(target, list, left, right);

            sortList(target, list, left, partitionIndex - 1);
            sortList(target, list, partitionIndex + 1, right);
        }
        return list;
    }

    private int partition(String target, List<Line> list, int left, int right) {
        int counter = left;
        for (int i = left; i < right; i++){
            if (list.get(i).compare(list.get(right))){
                Line temp = list.get(counter);
                list.set(counter, list.get(i));
                list.set(i, temp);
                addLog(target, counter, list);
                counter++;
            }
        }
        Line temp = list.get(right);
        list.set(right, list.get(counter));
        list.set(counter, temp);
        addLog(target, counter, list);

        return counter;
    }
}
