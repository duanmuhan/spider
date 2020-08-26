package com.cgs.service.index;

import com.cgs.constant.AverageType;
import com.cgs.dao.KItemDAO;
import com.cgs.dao.StockItemDAO;
import com.cgs.dao.index.AverageDAO;
import com.cgs.entity.StockItem;
import com.cgs.entity.index.AverageItem;
import com.cgs.entity.index.KItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
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
            log.info("start to calculate stockid:{}",stockId);
            List<KItem> kItems = kItemDAO.queryDateKItemsbyStockId(stockId,1);
            if (!CollectionUtils.isEmpty(kItems)){
                kItems = kItems.stream().sorted(Comparator.comparing(KItem::getDate)).collect(Collectors.toList());
                List<AverageItem> fiveDayList = calculate5DayAverage(kItems);
//                List<AverageItem> originFiveDayList = averageDAO.queryAverageItemByStockId(stockId,AverageType.FIVE_DAYS);
                List<AverageItem> tenDayList = calculate10DayAverage(kItems);
                List<AverageItem> originTendayList = averageDAO.queryAverageItemByStockId(stockId,AverageType.TEN_DAYS);
//                fiveDayList.removeAll(originFiveDayList);
                tenDayList.removeAll(originTendayList);
//                averageDAO.batchInsertAverageItem(fiveDayList);
                averageDAO.batchInsertAverageItem(tenDayList);
            }
        }

    }

    public List<AverageItem> calculateAverage(List<KItem> list, Integer day,Integer type){
        List<AverageItem> itemList = new ArrayList<>();
        if (day <= 0){
            return itemList;
        }
        DecimalFormat df = new DecimalFormat("#.00");
        int index = list.size();
        while (index >0){
            double totalPrice = 0;
            double averagePrice = 0;
            if (index>=day) {
                for (int i = 0; i < day; i++) {
                    totalPrice = list.get(index-1 - i).getClosePrice() + totalPrice;
                }
                averagePrice = totalPrice / day;
            }else if (index< day){
                for (int i=0; i<index; i++){
                    totalPrice = list.get(i).getClosePrice() + totalPrice;
                }
                averagePrice = totalPrice / day;
            }
            AverageItem item = new AverageItem();
            item.setType(type);
            item.setDate(list.get(index-1).getDate());
            item.setPrice(Double.valueOf(df.format(averagePrice)));
            item.setStockId(list.get(index-1).getStockId());
            itemList.add(item);
            index--;
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

    private List<KItem> calculateWeekKItem(List<KItem> list){
        List<KItem> kItems = new ArrayList<>();
        return kItems;
    }

}
