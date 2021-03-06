package com.cgs.service;

import com.cgs.service.info.PartyCentralCommitteeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PartyCentralCommitteeServiceTest {

    @Autowired
    private PartyCentralCommitteeService partyCentralCommitteeService;

    @Test
    public void testPartyCentralCommitteeService(){
        try {
            partyCentralCommitteeService.fetchPartyCentralCommitee();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
