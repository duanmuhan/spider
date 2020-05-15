package com.cgs.dto;

import com.cgs.entity.FinanceInfo;
import com.cgs.entity.StockInfo;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
public class FinanceInfoDTO {

    private String stockId;
    private String jbmgsy;
    private String kfmgsy;
    private String xsmgsy;
    private String mgjzc;
    private String mggjj;
    private String mgwfply;
    private String mgjyxjl;
    private String yyzsr;
    private String mlr;
    private String gsjlr;
    private String kfjlr;
    private String yyzsrtbzz;
    private String kfjlrtbzz;
    private String yyzsrgdhbzz;
    private String gsjlrgdhbzz;
    private String kfjlrgdhbzz;
    private String jqjzcsyl;
    private String tbjzcsyl;
    private String tbzzcsyl;
    private String mll;
    private String jll;
    private String sjsl;
    private String yskyysr;
    private String xsxjlyysr;
    private String jyxjlyysr;
    private String zzczzy;
    private String yszkzzts;
    private String chzzts;
    private String zcfzl;
    private String ldzczfz;
    private String ldbl;
    private String sdbl;
    private String date;

    public FinanceInfo convertToStockInfo(String stockId){
        FinanceInfo financeInfo = new FinanceInfo();
        financeInfo.setStockId(stockId);
        financeInfo.setBasicEarningsPerCommonShare(getJbmgsy());
        financeInfo.setDeductionOfNonEPS(getKfmgsy());
        financeInfo.setDilutedEPS(getXsmgsy());
        financeInfo.setNetAssetValuePerShare(getMgjzc());
        financeInfo.setPerShareReserve(getMggjj());
        financeInfo.setUDPPS(getMgwfply());
        financeInfo.setOperatingCashFlowPerShare(getMgjyxjl());
        financeInfo.setGrossRevenue(getYyzsr());
        financeInfo.setGrossProfit(getMlr());
        financeInfo.setAttributableNetProfit(getGsjlr());
        financeInfo.setDeductionOfNonNetProfit(getKfjlr());
        financeInfo.setYearOnYearGrowthOfTotalOperatingRevenue(getYyzsrtbzz());
        financeInfo.setAttributableNetProfitIncreasedYearOnYear(getGsjlr());
        financeInfo.setDeductionOfNonNetProfitIncreasedYearOnYear(getKfjlrtbzz());
        financeInfo.setAttributableNetProfitIncreasesOnARollingBasis(getYyzsrgdhbzz());
        financeInfo.setDeductionOfNonNetProfit(getGsjlrgdhbzz());
        financeInfo.setWeightedReturnOnEquity(getJqjzcsyl());
        financeInfo.setDilutedReturnOnEquity(getTbjzcsyl());
        financeInfo.setDilutedReturnOnTotalAssets(getTbzzcsyl());
        financeInfo.setRateOfGrossProfit(getTbjzcsyl());
        financeInfo.setRateOfMargin(getMll());
        financeInfo.setNetMargin(getMlr());
        financeInfo.setETR(getSjsl());
        financeInfo.setItemsReceivedInAdvance(getYskyysr());
        financeInfo.setCashFlowFromSales(getXsxjlyysr());
        financeInfo.setOperationCashFlow(getJyxjlyysr());
        financeInfo.setTotalAssetsTurnover(getZcfzl());
        financeInfo.setDaysSalesOutstanding(getYszkzzts());
        financeInfo.setDaysInInventory(getChzzts());
        financeInfo.setDEBT(getZcfzl());
        financeInfo.setAccruedLiabilities(getLdzczfz());
        financeInfo.setCurrentRatio(getLdbl());
        financeInfo.setQuickRatio(getSdbl());
        financeInfo.setReleaseDate(getDate());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        financeInfo.setDate(simpleDateFormat.format(new Date()));
        return financeInfo;
    }
}
