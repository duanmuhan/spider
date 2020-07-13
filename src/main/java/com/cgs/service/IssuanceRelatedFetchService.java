package com.cgs.service;

import com.cgs.dao.StockHolderDAO;
import com.cgs.dao.StockHolderTopTenDAO;
import com.cgs.dao.StockItemDAO;
import com.cgs.entity.StockItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IssuanceRelatedFetchService {

    @Autowired
    private StockHolderDAO stockHolderDAO;
    @Autowired
    private StockHolderTopTenDAO stockHolderTopTenDAO;
    @Autowired
    private StockItemDAO stockItemDAO;

    public void fetch(){
        List<StockItem> stockItemList = stockItemDAO.queryAllStockList();

    }
}
