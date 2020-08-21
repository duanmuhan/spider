package com.cgs.service;

import com.cgs.dao.StockHotSpotDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockHotSpotRankService {
    @Autowired
    private StockHotSpotDAO stockHotSpotDAO;

    public void fetchStockHotspotRand() throws Exception{

    }
}
