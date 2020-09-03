package com.cgs.entity;

import lombok.Data;

@Data
public class StockMoodIndex {

    private String stockIncreaseRate;
    private String stockPoint;
    private Integer stockTotalCount;
    private Integer stockIncreaseCount;
    private String releaseDate;
}
