package com.cgs.service;

import com.cgs.dao.StockInfoDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockInfoService {

    @Autowired
    private StockInfoDAO stockInfoDAO;

    public void fetchStockInfoService(){

    }
}
