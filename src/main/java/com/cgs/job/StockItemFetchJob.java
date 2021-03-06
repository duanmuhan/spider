package com.cgs.job;

import com.cgs.service.StockItemFetchService;
import com.xxl.job.core.handler.IJobHandler;
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
public class StockItemFetchJob extends IJobHandler {

    @Autowired
    private StockItemFetchService stockItemFetchService;

    @Override
    public void execute() throws Exception {
        log.info("start to execute fetchStockItem");
        try {
            stockItemFetchService.fetchStockList();
        }catch (Exception e){
            log.error("exception is :{}",e);
        }
    }
}
