package com.cgs.service;

import com.cgs.dao.StockInfoDAO;
import com.cgs.dao.StockItemDAO;
import com.cgs.entity.StockItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
@Slf4j
public class StockInfoService {

    @Autowired
    private StockInfoDAO stockInfoDAO;
    @Autowired
    private StockItemDAO stockItemDAO;

    public void fetchStockInfoService(){
        List<StockItem> list = stockItemDAO.queryAllStockList();
        if (CollectionUtils.isEmpty(list)){
            return;
        }

    }
}
