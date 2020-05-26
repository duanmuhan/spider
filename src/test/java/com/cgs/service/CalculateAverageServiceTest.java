package com.cgs.service;

import com.cgs.service.index.CalculateAverageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CalculateAverageServiceTest {

    @Autowired
    private CalculateAverageService calculateAverageService;

    @Test
    public void testCalculateAverageService(){
        calculateAverageService.calculateAverage();
    }
}
