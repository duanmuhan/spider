package com.cgs.job;

import com.cgs.service.StockItemFetchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Configuration
@EnableScheduling
public class StockItemFetchJob {

    @Autowired
    private StockItemFetchService stockItemFetchService;

    public void fetchStockItem() throws IOException {
        stockItemFetchService.fetchStockList();
    }
}
