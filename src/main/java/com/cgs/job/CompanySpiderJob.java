package com.cgs.job;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Configuration
@EnableScheduling
public class CompanySpiderJob {

    @Scheduled(cron = "0/5 * * * * ?")
    public void fetchCompanyInfo(){
    }
}
