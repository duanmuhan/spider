package com.cgs.entity;

import lombok.Data;

@Data
public class StockTechnology {
    private String stockId;
    private String type;
    private String special;
    private String queryStr;
    private String tag;
    private String descStr;
    private String releaseDate;
}
