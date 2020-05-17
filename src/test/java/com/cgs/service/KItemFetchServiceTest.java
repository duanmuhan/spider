package com.cgs.service;

import com.cgs.service.index.KItemFetchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KItemFetchServiceTest {

    @Autowired
    private KItemFetchService kItemFetchService;

    @Test
    public void testKItemService(){
        try {
            kItemFetchService.fetchKItem();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
