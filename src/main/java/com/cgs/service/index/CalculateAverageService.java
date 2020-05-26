package com.cgs.service.index;

import com.cgs.dao.KItemDAO;
import com.cgs.dao.StockItemDAO;
import com.cgs.dao.index.AverageDAO;
import com.cgs.entity.StockItem;
import com.cgs.entity.index.KItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CalculateAverageService {

    @Autowired
    private AverageDAO averageDAO;
    @Autowired
    private KItemDAO kItemDAO;
    @Autowired
    private StockItemDAO stockItemDAO;

    public void  calculateAverage(){
        List<StockItem> stockItems = stockItemDAO.queryAllStockList();
        if (CollectionUtils.isEmpty(stockItems)){
            return;
        }
        List<String> stockIds = stockItems.stream().map(e->e.getStockId()).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(stockIds)){
            return;
        }
        for (String stockId : stockIds){
            List<KItem> kItems = kItemDAO.queryKItemsbyStockId(stockId);

        }
    }
}
