package com.cgs.service;

import com.cgs.service.info.IndustryAndInformationTechnologyService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IndustryAndInformationTechnologyServiceTest {

    @Autowired
    private IndustryAndInformationTechnologyService industryAndInformationTechnologyService;

    @Test
    public void testKItemService(){
        try {
            industryAndInformationTechnologyService.fetchIndustryService();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
