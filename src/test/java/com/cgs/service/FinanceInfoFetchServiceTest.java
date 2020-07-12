package com.cgs.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FinanceInfoFetchServiceTest {

    @Autowired
    private FinanceInfoFetchService financeInfoFetchService;

    @Test
    public void testFetchStockInfoService(){
        try {
            financeInfoFetchService.fetchFinanceInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
