package com.cgs.entity;

import lombok.Data;

@Data
public class StockHolderTopTen {

    //日期
    private String releaseDate;
    //股东名称
    private String stockHolderName;
    //股东性质
    private String typesOfStockHolders;
    //股份类型
    private String typesOfShares;
    //持股数(股)
    private String numbersOfShares;
    //占总流通股本持股比例
    private String rate;
    //增减(股)
    private String change;
    //变动比例
    private String changeRate;
    private String date;

}
