package com.cgs.dto;

import com.cgs.entity.StockItem;
import lombok.Data;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.util.StringUtils;

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
        if (!StringUtils.isEmpty(agjc)){
            Document document = Jsoup.parse(agjc);
            String text = document.text();
            stockItem.setName(text);
        }
        stockItem.setListingDate(agssrq);
        return stockItem;
    }
}
