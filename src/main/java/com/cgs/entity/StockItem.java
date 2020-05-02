package com.cgs.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class StockItem implements Serializable {

    private String stockId;

    private String openingPrice;

    private String closingPrice;

    private String highestPrice;

    private String lowestPrice;

    private String bidPrice;

    private String sellPrice;

    private String exchangeShares;

    private String exchangeAmount;

    private String bidOneAmount;

    private String bidOnePrice;

    private String bidTwoAmount;

    private String bidTwoPrice;

    private String bidThreeAmount;

    private String bidThreePrice;

    private String bidFourAmount;

    private String bidFourPrice;

    private String bidFiveAmount;

    private String bidFivePrice;

    private String sellOneAmount;

    private String sellOnePrice;

    private String sellTwoAmount;

    private String sellTwoPrice;

    private String sellThreeAmount;

    private String sellThreePrice;

    private String sellFourAmount;

    private String sellFourPrice;

    private String sellFiveAmount;

    private String sellFivePrice;

}
