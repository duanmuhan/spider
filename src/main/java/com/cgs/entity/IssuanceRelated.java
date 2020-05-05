package com.cgs.entity;

import lombok.Data;

@Data
public class IssuanceRelated {

    private String stockId;
    //成立日期
    private String dateOfEstablishment;
    //上市日期
    private String dateOfListing;
    //发行市盈率
    private String ipoRatio;
    //网上发行日期
    private String onlineReleaseDate;
    //发行方式
    private String issuingMode;
    //每股面值
    private String parValue;
    //发行量
    private String circulation;
    //发型价
    private String issuePricePerShare;
    //发行费用
    private String distributionExpense;
    //发行总市值
    private String totalMarketValueOfIssue;
    //募集资金净额
    private String netAmountOfRaisedFunds;
    //首日开盘价
    private String openingPriceOnTheFirstDay;
    //首日收盘价
    private String firstDayClose;
    //首日换手率
    private String firstDayTurnover;
    //首日最高价
    private String topPriceOnTheFirstDay;
    //网下配售中签率
    private String winningRateOfOfflineAllotment;
    //定价中签率
    private String winningRateOfPricing;

}
