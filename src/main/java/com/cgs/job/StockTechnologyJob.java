package com.cgs.job;

import com.cgs.service.StockTechnologyService;
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
public class StockTechnologyJob extends IJobHandler {

    @Autowired
    private StockTechnologyService stockTechnologyService;

    @Override
    public void execute() throws Exception {
        log.info("start to execute fetchStockTechnologyInfo");
        try {
            stockTechnologyService.fetchStockTechnologyInfo();
        }catch (Exception e){
            log.error("exception is :{}",e);
        }
    }
}
