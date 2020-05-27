package com.cgs.service.index;

import com.cgs.dao.KItemDAO;
import com.cgs.dao.StockItemDAO;
import com.cgs.dao.index.AverageDAO;
import com.cgs.entity.StockItem;
import com.cgs.entity.index.KItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CalculateKDJService {

    @Autowired
    private KItemDAO kItemDAO;
    @Autowired
    private StockItemDAO stockItemDAO;
    @Autowired
    private AverageDAO averageDAO;

    public void calculateKDJItem(){
        List<StockItem> stockItems = stockItemDAO.queryAllStockList();
        List<String> list = stockItems.stream().map(e->e.getStockId()).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(list)){
            return;
        }
        for (String stockId : list){
            List<KItem> kItems = kItemDAO.queryKItemsbyStockId(stockId);
            if (CollectionUtils.isEmpty(kItems)){
                continue;
            }

        }
    }
}
