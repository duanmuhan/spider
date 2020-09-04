package com.cgs.job;

import com.cgs.service.StockInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Configuration
@EnableScheduling
@Slf4j
public class StockInfoFetchJob {

    @Autowired
    private StockInfoService stockInfoService;

    @Scheduled(cron = "0 30 17 1/1 * ?")
    public void fetchStockData(){
        log.info("start to execute ");
        try {
            stockInfoService.fetchStockInfoService();
        }catch (Exception e){
            log.error("error in fetchStockData:{}",e);
        }

    }
}
