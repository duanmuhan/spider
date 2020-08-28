package com.cgs.job.index;

import com.cgs.service.index.KItemCalculateService;
import com.cgs.service.index.KItemFetchService;
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
public class KItemJob {

    @Autowired
    private KItemFetchService kItemFetchService;
    @Autowired
    private KItemCalculateService kItemCalculateService;

    @Scheduled(cron = "0/5 * * * * ?")
    public void fetchStockData(){
        try {
            kItemFetchService.fetchKItem();
            kItemCalculateService.calculateKItem();
        }catch (Exception e){
            log.error("error while fetch fetchStockData:{} ",e);
        }

    }
}
