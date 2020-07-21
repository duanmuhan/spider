package com.cgs.dto;

import lombok.Data;

@Data
public class ShStockInfoDTO {

    private String id;
    private Double change;
    private Double changeData;
    private Double closeMarketValue;
    private Double closeNegoValue;
    private Double closePrice;
    private Double closeProfitRate;
    private Double closeTrAmt;
    private Double closeTrTx;
    private Double closeTrVol;
    private String closeTxDate;
    private Double evgAmt;
    private Double evgPrice;
    private Double evgVol;
    private Double last_close_price;
    private Double maxHighPrice;
    private String maxHighPriceDate;
    private Double maxTrAmt;
    private String maxTrAmtDate;
    private Double maxTrVol;
    private String maxTrVol1;
    private String maxTrVolDate;
    private Double minLowPrice;
    private String minLowPriceDate;
    private Double minTrAmt;
    private String minTrAmtDate;
    private Double minTrVol;
    private Double minTrVol1;
    private Double openPrice;
    private String openTxDate;
    private String productName;
    private String productType;
    private Double totalAmt;
    private Double totalChange;
    private Double totalExchRate;
    private Double totalPrice;
    private Double totalTx;
    private Double totalTxDate;
    private Double totalVol;
    private String totalVol1;
}
