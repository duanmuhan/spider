package com.cgs.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class StockInfo implements Serializable {

    private String stockId;

    //交易所
    private String exchange;
    //收益
    private String income;
    //PE
    private String PE;
    //市净率
    private String PBV;
    //每股净资产
    private String netAssetValuePerShare;
    //总营收
    private String overRallRevenue;
    //总营收同比
    private String yearOnYearOfTotalRevenue;
    //净利润
    private String netRevenues;
    //净利润同比
    private String yearOnYearOfNetRevenues;
    //毛利率
    private String rateOfGrossProfit;
    //净利率
    private String netProfitMargin;
    //ROE
    private String ROE;
    //负债率
    private String debtRatio;
    //总股本
    private String shareholdingEquity;
    //总值
    private String totalValue;
    //流通股本
    private String flowOfEquity;
    //流通值
    private String flowValue;
    //每股未分配利润
    private String UDPPS;
    //上市时间
    private String timeToMarket;
}
