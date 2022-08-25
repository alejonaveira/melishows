package com.meli.shows.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

public class SortHelper {

    private static final Logger logger = LoggerFactory.getLogger(SortHelper.class);

    public static Sort convertFromString(String[] sort){

        List<Sort.Order> sortFields = new ArrayList<>();
        try{
            if(sort != null && sort.length > 0){
                for (String sortOrder : sort) {
                    if(sortOrder.contains(",")){
                        String[] s = sortOrder.split(",");
                        Sort.Direction direction = Sort.Direction.ASC;
                        Sort.Direction.fromString(s[1]);
                        sortFields.add(new Sort.Order(direction, s[0] ));
                    }else{
                        sortFields.add(new Sort.Order(Sort.Direction.ASC, sortOrder ));
                    }
                }
            }
        }catch (Exception e){
            logger.warn("Bad sort order", e);
        }
        return Sort.by(sortFields);
    }
}
