package com.cgs.service;

import com.cgs.dao.FinanceDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FinanceInfoFetchService {

    @Autowired
    private FinanceDAO financeDAO;


}
