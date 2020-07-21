package com.cgs.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StockInfoServiceTest {

    @Autowired
    private StockInfoService stockInfoService;

    @Test
    public void testPlateStockMapping(){
        try {
            stockInfoService.fetchStockInfoService();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
