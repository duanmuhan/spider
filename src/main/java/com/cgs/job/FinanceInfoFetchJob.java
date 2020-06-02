package com.cgs.job;

import com.cgs.service.FinanceInfoFetchService;
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
public class FinanceInfoFetchJob {

    @Autowired
    private FinanceInfoFetchService financeInfoFetchService;

    @Scheduled(cron = "0/5 * * * * ?")
    public void fetchFinanceInfo(){
        log.info("start to fetch finance info");
        try {
            financeInfoFetchService.fetchFinanceInfo();
        }catch (Exception e){
            log.error("");
        }
        log.info("end to fetch finance info ");
    }
}
