package com.cgs.entity;

import lombok.Data;

import java.util.Date;

@Data
public class StockItemSzDTO {
    private String agdm;
    private String agjc;
    private String agltgb;
    private String agssrq;
    private String agzgb;
    private String gsjc;
    private String sshymc;
    private String zqdm;

    public StockItem convertToStockItem(){
        StockItem stockItem = new StockItem();
        stockItem.setStockId(agdm);
        stockItem.setExchangeId("sz");
        stockItem.setName(agjc);
        stockItem.setListingDate(agssrq);
        stockItem.setUpdateTime(System.currentTimeMillis());
        return stockItem;
    }
}
