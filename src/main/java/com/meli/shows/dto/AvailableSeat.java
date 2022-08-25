package com.meli.shows.dto;

import lombok.Data;

@Data
public class AvailableSeat {

    private Long id;
    private String row;
    private Long isle;
    private Double price;
}
