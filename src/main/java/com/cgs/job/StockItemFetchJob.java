package com.cgs.job;

import com.cgs.service.StockItemFetchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Configuration
@EnableScheduling
@Slf4j
public class StockItemFetchJob {

    @Autowired
    private StockItemFetchService stockItemFetchService;

    @Scheduled(cron = "0/5 * * * * ?")
    public void fetchStockItem(){
        try {
            stockItemFetchService.fetchStockList();
        }catch (Exception e){
            log.error("exception is :{}",e);
        }
    }
}
