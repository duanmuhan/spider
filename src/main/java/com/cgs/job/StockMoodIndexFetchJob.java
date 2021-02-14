package com.cgs.job;

import com.cgs.service.StockMoodIndexFetchService;
import com.xxl.job.core.handler.IJobHandler;
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
public class StockMoodIndexFetchJob extends IJobHandler {

    @Autowired
    private StockMoodIndexFetchService stockMoodIndexFetchService;

    @Override
    public void execute() throws Exception {
        log.info("start to execute fetchStockMoodIndex");
        try {
            stockMoodIndexFetchService.fetchStockMoodIndexService();
        }catch (Exception e){
            log.error("exception is :{}",e);
        }
    }
}
