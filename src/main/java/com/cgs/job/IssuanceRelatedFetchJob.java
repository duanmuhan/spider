package com.cgs.job;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Configuration
@EnableScheduling
@Slf4j
public class IssuanceRelatedFetchJob {

    @Scheduled(cron = "0/5 * * * * ?")
    public void fetchTestJob(){
        log.info("test fetchTestJob");
    }
}
