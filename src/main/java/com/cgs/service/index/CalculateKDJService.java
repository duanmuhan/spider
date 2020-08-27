package com.cgs.service.index;

import com.cgs.dao.KItemDAO;
import com.cgs.dao.StockItemDAO;
import com.cgs.dao.index.AverageDAO;
import com.cgs.dao.index.KDJDAO;
import com.cgs.entity.StockItem;
import com.cgs.entity.index.KDJItem;
import com.cgs.entity.index.KItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CalculateKDJService {

    @Autowired
    private KItemDAO kItemDAO;
    @Autowired
    private StockItemDAO stockItemDAO;
    @Autowired
    private KDJDAO kdjDAO;
    private static Integer LOWEST_PERIOD = 9;
    private static Integer DEFAULT_VALUE = 50;

    public void calculateKDJItem(){
        List<StockItem> stockItems = stockItemDAO.queryAllStockList();
        List<String> list = stockItems.stream().map(e->e.getStockId()).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(list)){
            return;
        }
        List<KDJItem> kdjItems = new ArrayList<>();
        Map<String,KDJItem> kdjItemMap = new HashMap<>();
        for (String stockId : list){
            List<KItem> kItems = kItemDAO.queryDateKItemsbyStockId(stockId,1);
            if (CollectionUtils.isEmpty(kItems)){
                continue;
            }
            for (int index=0; index<kItems.size(); index ++){
                if (index < LOWEST_PERIOD){
                    continue;
                }
                KDJItem kdjItem = new KDJItem();
                List<KItem> indexList = kItems.subList(index-LOWEST_PERIOD,index);
                Double highestPrice = indexList.stream().max((e1,e2)->e1.getHigh()>e2.getHigh()? 1:-1).get().getHigh();
                Double lowestPrice = indexList.stream().min((e1,e2)->e1.getLow()<e2.getLow()? 1:-1).get().getLow();
                Integer rsv = (int)((kItems.get(index).getClosePrice() - lowestPrice)/(highestPrice - lowestPrice) * 100);
                String dateBefore = (index-1<0) ? "": kItems.get(index-1).getDate();
                KDJItem item = kdjItemMap.get(dateBefore + stockId);
                Integer kValueBefore=0;
                Integer dValueBefore=0;
                if (ObjectUtils.isEmpty(item)){
                    kValueBefore = DEFAULT_VALUE;
                    dValueBefore = DEFAULT_VALUE;
                }else {
                    kValueBefore = item.getKValue();
                    dValueBefore = item.getDValue();
                }
                Integer kValue = 2/3 * kValueBefore + 1/3 * rsv;
                Integer dValue = 2/3 * dValueBefore + 1/3 * kValue;
                Integer jValue = 3 * kValue - 2* dValue;
                kdjItem.setKValue(kValue);
                kdjItem.setDValue(dValue);
                kdjItem.setJValue(jValue);
                kdjItem.setDate(kItems.get(index).getDate());
                kdjItem.setStockId(stockId);
                kdjItems.add(kdjItem);
                kdjItemMap.put(kItems.get(index).getDate() + stockId,kdjItem);
            }
        }
        kdjDAO.batchInsertKDJItems(kdjItems);
    }
}
