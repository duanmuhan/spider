package com.cgs.job;

import com.cgs.service.FinanceInfoFetchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

@Component
@Configuration
@EnableScheduling
public class FinanceInfoFetchJob {

    @Autowired
    private FinanceInfoFetchService financeInfoFetchService;

    public void fetchFinanceInfo(){

    }
}
