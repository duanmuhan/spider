package com.cgs.job;

import com.cgs.service.PlateFetchService;
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
public class PlateFetchJob {

    @Autowired
    private PlateFetchService plateFetchService;

    @Scheduled(cron = "0 15 10 ? * MON")
    public void fetchPlateInfo() throws IOException {
        log.info("start to fetch plate info");
        plateFetchService.fetchPlateInfo();
        log.info("end to fetch plate info");
    }

}
