package com.cgs.service;

import com.cgs.service.info.SateCouncilService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StateCouncilServiceTest {
    @Autowired
    private SateCouncilService sateCouncilService;

    @Test
    public void testStateCouncilServiceStateCouncilService(){
        try {
            sateCouncilService.fetchStateCouncilService();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
