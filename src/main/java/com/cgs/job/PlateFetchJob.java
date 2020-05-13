package com.cgs.job;

import com.cgs.service.PlateFetchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Configuration
@EnableScheduling
public class PlateFetchJob {

    @Autowired
    private PlateFetchService plateFetchService;

    @Scheduled(cron = "0 0 * * * ?")
    public void fetchPlateInfo() throws IOException {
        plateFetchService.fetchPlateInfo();
    }

}
