package com.cgs.service;

import com.cgs.util.HttpRequestUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StockTechnologyServiceTest {

    @Autowired
    private StockTechnologyService stockTechnologyService;

    @Test
    public void testFetchStockItem(){
        try {
            stockTechnologyService.fetchStockTechnologyInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
