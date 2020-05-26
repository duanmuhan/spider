package com.cgs.service.index;

import com.cgs.constant.AverageType;
import com.cgs.dao.KItemDAO;
import com.cgs.dao.StockItemDAO;
import com.cgs.dao.index.AverageDAO;
import com.cgs.entity.StockItem;
import com.cgs.entity.index.AverageItem;
import com.cgs.entity.index.KItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        List<AverageItem> averageItems = new ArrayList<>();
        for (String stockId : stockIds){
            List<KItem> kItems = kItemDAO.queryKItemsbyStockId(stockId);
            List<AverageItem> fiveDayList = calculate5DayAverage(kItems);

        }
    }

    public List<AverageItem> calculateAverage(List<KItem> list, Integer day,Integer type){
        List<AverageItem> itemList = new ArrayList<>();
        if (day <= 0){
            return itemList;
        }
        int index = 0;
        while (index < list.size()){
            if (index - day < 0){
                index ++;
                continue;
            }
            double totalPrice = 0;
            for (int i=0; i<day; i++){
                totalPrice = list.get(i).getClosePrice() + totalPrice;
            }
            double averagePrice = totalPrice / day;
            AverageItem item = new AverageItem();
            item.setAverageTye(type);
            item.setDate(list.get(index).getDate());
            item.setPrice(averagePrice);
            item.setStockId(list.get(index).getStockId());
            itemList.add(item);
            index ++;
        }
        return itemList;
    }

    private List<AverageItem> calculate5DayAverage(List<KItem> list){
        List<AverageItem> itemList = calculateAverage(list,5,AverageType.FIVE_DAYS);
        return itemList;
    }

    private List<AverageItem> calculate10DayAverage(List<KItem> list){
        List<AverageItem> itemList = calculateAverage(list,10,AverageType.TEN_DAYS);
        return itemList;
    }

    private List<AverageItem> calculate20DayAverage(List<KItem> list){
        List<AverageItem> itemList = calculateAverage(list,20,AverageType.TWENTY_DAYS);
        return itemList;
    }

    private List<AverageItem> calculate60DayAverage(List<KItem> list){
        List<AverageItem> itemList = calculateAverage(list,60,AverageType.SIXTIES_DAYS);
        return itemList;
    }

    private List<AverageItem> calculate120DayAverage(List<KItem> list){
        List<AverageItem> itemList = calculateAverage(list,120,AverageType.ONE_HUNDRED_TWENTY_DAYS);
        return itemList;
    }

}
