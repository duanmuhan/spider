package com.cgs.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PlateStockMappingTest {

    @Autowired
    private PlateStockMappingFetchService plateStockMappingFetchService;

    @Test
    public void testPlateStockMapping(){
        try {
            plateStockMappingFetchService.fetchPlatStockMapping();
            Thread.sleep(1000 * 1000);
        }catch (Exception e){
        }
    }
}
