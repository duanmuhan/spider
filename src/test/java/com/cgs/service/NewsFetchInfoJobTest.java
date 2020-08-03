package com.cgs.service;

import com.cgs.job.NewsFetchInfoJob;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NewsFetchInfoJobTest {

    @Autowired
    private NewsFetchInfoJob newsFetchInfoJob;

    @Test
    public void testKItemService(){
        try {
            newsFetchInfoJob.fetchNewsInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
