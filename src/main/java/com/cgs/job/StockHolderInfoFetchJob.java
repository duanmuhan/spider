package com.cgs.job;

import com.cgs.service.StockHolderInfoFetchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

@Component
@Configuration
@EnableScheduling
@Slf4j
public class StockHolderInfoFetchJob {

    @Autowired
    private StockHolderInfoFetchService stockHolderInfoFetchService;

    public void fetchStockHolder(){
        try {
            stockHolderInfoFetchService.fetchStockHolderInfo();
        } catch (Exception e) {
            log.error("StockHolderInfoFetchJob fetchStockHolder error {}" ,e);
        }
    }
}
