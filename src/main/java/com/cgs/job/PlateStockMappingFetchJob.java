package com.cgs.job;

import com.cgs.service.PlateStockMappingFetchService;
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
public class PlateStockMappingFetchJob {

    @Autowired
    private PlateStockMappingFetchService plateStockMappingFetchService;

    @Scheduled(cron = "0 15 20 ? * WED")
    public void fetchPlateStockMapping(){
        log.info("start to execute fetchPlateStockMapping");
        try {
            plateStockMappingFetchService.fetchPlatStockMapping();
        }catch (Exception e){
            log.error("exception is :{}", e);
        }
        log.info("end to execute fetch plate stock mapping");
    }
}
