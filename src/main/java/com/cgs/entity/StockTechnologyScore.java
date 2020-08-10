package com.cgs.entity;

import lombok.Data;

@Data
public class StockTechnologyScore {

    private String stockId;
    private String stockName;
    private Double score;
    private String releaseDate;
}
