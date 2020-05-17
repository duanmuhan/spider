package com.cgs.service;

import com.cgs.service.index.KItemFetchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StockItemFetchServiceTest {

    @Autowired
    private StockItemFetchService stockItemFetchService;

    @Test
    public void testFetchKItem(){
        try {
            stockItemFetchService.fetchStockList();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
