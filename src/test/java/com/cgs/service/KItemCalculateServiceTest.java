package com.cgs.service;

import com.cgs.service.index.KItemCalculateService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KItemCalculateServiceTest {

    @Autowired
    private KItemCalculateService kItemCalculateService;

    @Test
    public void testKItemService(){
        try {
            kItemCalculateService.calculateKItem();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
