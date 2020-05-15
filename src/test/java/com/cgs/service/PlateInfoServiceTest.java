package com.cgs.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PlateInfoServiceTest {

    @Autowired
    private PlateFetchService plateFetchService;

    @Test
    public void testFetchPlateInfo(){
        try {
            plateFetchService.fetchPlateInfo();
            Thread.sleep(1000 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
