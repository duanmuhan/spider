package com.cgs.service;

import com.cgs.service.info.DevelopmentAndReformCommissionService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DevelopmentAndReformCommissionServiceTest {

    @Autowired
    private DevelopmentAndReformCommissionService developmentAndReformCommissionService;

    @Test
    public void testFetchDevelopmentReformService(){
        try {
            developmentAndReformCommissionService.fetchDevelopmentReformService();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
