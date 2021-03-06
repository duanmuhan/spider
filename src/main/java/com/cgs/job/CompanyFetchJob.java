package com.cgs.job;

import com.cgs.service.CompanyFetchService;
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
public class CompanyFetchJob {

    @Autowired
    private CompanyFetchService companyFetchService;

    @Scheduled(cron = "0 30 21 1/1 * ? ")
    public void fetchCompanyInfo(){
        log.info("start to fetch company info");
        try {
            companyFetchService.fetchCompanyInfo();
        } catch (Exception e) {
            log.error("exception is : {}",e);
        }
        log.info("finished to fetch company info");
    }
}
