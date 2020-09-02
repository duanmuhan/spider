package com.cgs.job;

import com.cgs.service.StockTechnologyService;
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
public class StockTechnologyJob {

    @Autowired
    private StockTechnologyService stockTechnologyService;

    @Scheduled(cron = "0 0 23 * * ?")
    public void fetchStockTechnologyInfo(){
        log.info("start to execute fetchStockTechnologyInfo");
        try {
            stockTechnologyService.fetchStockTechnologyInfo();
        }catch (Exception e){
            log.error("exception is :{}",e);
        }
    }
}
