package com.yugoo.gis.common.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nihao on 17/10/17.
 */
public class StaticUtils {
    public static List split(List list, Integer pageSize){
        if(pageSize < 2){
            throw new RuntimeException("split size must bigger than one");
        }
        int listSize = list.size();
        int page = (listSize + (pageSize-1))/ pageSize;
        List<List> result = new ArrayList<>();
        for(int i=0;i<page;i++){
            List subList = new ArrayList();
            for(int j=0;j<listSize;j++){
                int pageIndex = ( (j + 1) + (pageSize-1) ) / pageSize;
                if(pageIndex == (i + 1)) {
                    subList.add(list.get(j));
                }

                if( (j + 1) == ((j + 1) * pageSize) ) {
                    break;
                }
            }
            result.add(subList);
        }
        return result;
    }
}
