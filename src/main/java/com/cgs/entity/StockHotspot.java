package com.cgs.entity;

import lombok.Data;

@Data
public class StockHotspot {
    private String stockId;
    private String stockName;
    private Integer rank;
    private String releaseDate;
}
