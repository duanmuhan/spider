package com.cgs.service.index;

import com.cgs.dao.KItemDAO;
import com.cgs.dao.StockItemDAO;
import com.cgs.entity.StockItem;
import com.cgs.entity.index.KItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
@Slf4j
public class KItemCalculateService {
    @Autowired
    private KItemDAO kItemDAO;
    @Autowired
    private StockItemDAO stockItemDAO;

    public void calculateKItem(){
        List<StockItem> stockItemList = stockItemDAO.queryAllStockList();
        if (CollectionUtils.isEmpty(stockItemList)){
            return;
        }
        for (StockItem stockItem : stockItemList){
            List<KItem> kItems = kItemDAO.queryDateKItemsbyStockId(stockItem.getStockId(),1);
            if (CollectionUtils.isEmpty(kItems)){
                continue;
            }
        }
    }

    boolean isSameWeek(String fromDate,String lastDate){
        return true;
    }

    boolean isSameMonth(String fromDate,String lastDate){
        return true;
    }

    boolean isSameYear(String fromDate,String lastDate){
        return true;
    }
}
