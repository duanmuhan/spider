package com.cgs.dto;

import com.cgs.entity.StockItem;
import lombok.Data;

@Data
public class StockItemShDTO {
    private String LISTING_BOARD;
    private String COMPANY_ABBR;
    private String END_SHARE_CODE;
    private String NUM;
    private String END_SHARE_MAIN_DEPART;
    private String END_SHARE_VICE_DEPART;
    private String CHANGE_DATE;
    private String SECURITY_ABBR_B;
    private String SECURITY_ABBR_A;
    private String SECURITY_CODE_A;
    private String ENGLISH_ABBR;
    private String SECURITY_CODE_B;
    private String COMPANY_CODE;
    private String LISTING_DATE;

    public StockItem convertToStockItem(){
        StockItem stockItem = new StockItem();
        stockItem.setStockId(getCOMPANY_CODE());
        stockItem.setListingDate(getLISTING_DATE());
        stockItem.setName(getCOMPANY_ABBR());
        stockItem.setExchangeId("sh");
        return stockItem;
    }
}
