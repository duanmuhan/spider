package com.cgs.service;

import com.cgs.dao.StockInfoDAO;
import com.cgs.dao.StockItemDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class StockInfoService {

    @Autowired
    private StockInfoDAO stockInfoDAO;
    @Autowired
    private StockItemDAO stockItemDAO;

    @Value("${stock.info.url}")
    private String stockInfoUrl;

    public void fetchStockInfoService(){
    }
}
