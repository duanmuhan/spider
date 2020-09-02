package com.cgs.job;

import com.cgs.service.StockAchievementService;
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
public class StockAchievementJob {

    @Autowired
    private StockAchievementService stockAchievementService;

    @Scheduled(cron = "0 40 22 ? * WED")
    public void fetchStockAchievement(){
        log.info("start to execute fetchStockAchievement");
        try {
            stockAchievementService.fetchStockAchievement();
        }catch (Exception e){
            log.error("exception is :{}", e);
        }
        log.info("end to execute fetch fetchStockAchievement");
    }
}
