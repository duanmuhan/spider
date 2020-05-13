package com.cgs.job;

import com.cgs.service.StockInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Configuration
@EnableScheduling
public class StockInfoSpiderJob {

    @Autowired
    private StockInfoService stockInfoService;

    @Scheduled(cron = "0/5 * * * * ?")
    public void fetchStockData(){
    }
}
