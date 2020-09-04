package com.cgs.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StockMoodIndexFetchServiceTest {

    @Autowired
    private StockMoodIndexFetchService stockMoodIndexFetchService;

    @Test
    public void testStockMoodIndexFetchService(){
        try {
            stockMoodIndexFetchService.fetchStockMoodIndexService();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
