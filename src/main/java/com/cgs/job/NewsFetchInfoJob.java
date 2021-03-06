package com.cgs.job;

import com.cgs.service.info.DevelopmentAndReformCommissionService;
import com.cgs.service.info.IndustryAndInformationTechnologyService;
import com.cgs.service.info.PartyCentralCommitteeService;
import com.cgs.service.info.SateCouncilService;
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
public class NewsFetchInfoJob {

    @Autowired
    private SateCouncilService sateCouncilService;
    @Autowired
    private DevelopmentAndReformCommissionService developmentAndReformCommissionService;
    @Autowired
    private IndustryAndInformationTechnologyService industryAndInformationTechnologyService;
    @Autowired
    private PartyCentralCommitteeService partyCentralCommitteeService;

    @Scheduled(cron = "0 30 19 1/1 * ? ")
    public void fetchNewsInfo(){
        sateCouncilService.fetchStateCouncilService();
        developmentAndReformCommissionService.fetchDevelopmentReformService();
        industryAndInformationTechnologyService.fetchIndustryService();
        partyCentralCommitteeService.fetchPartyCentralCommitee();
    }
}
