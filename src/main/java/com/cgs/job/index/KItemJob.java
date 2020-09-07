package com.cgs.job.index;

import com.cgs.service.StockMoodIndexFetchService;
import com.cgs.service.index.CalculateAverageService;
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
    @Autowired
    private CalculateAverageService calculateAverageService;
    @Autowired
    private StockMoodIndexFetchService stockMoodIndexFetchService;

    @Scheduled(cron = "0 10 17 * * ?")
    public void fetchStockData(){
        try {
            log.info("start to fetch Stock KItem");
            kItemFetchService.fetchKItem();
            log.info("start to calculateKItem");
            kItemCalculateService.calculateKItem();
//            calculateAverageService.calculateAverage();

        }catch (Exception e){
            log.error("error while fetch fetchStockData:{} ",e);
        }
        log.info("start to fetchStockMoodIndexService");

    }
}
