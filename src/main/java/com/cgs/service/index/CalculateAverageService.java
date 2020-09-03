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
import org.springframework.util.ObjectUtils;

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

    public void  calculateAverage() throws InterruptedException {
        List<StockItem> stockItems = stockItemDAO.queryAllStockList();
        if (CollectionUtils.isEmpty(stockItems)){
            return;
        }
        List<String> stockIds = stockItems.stream().map(e->e.getStockId()).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(stockIds)){
            return;
        }
        for (String stockId : stockIds){
            log.info("start to calculate stockId:{}",stockId);
            List<KItem> kItems = kItemDAO.queryDateKItemsbyStockId(stockId,1);
            if (!CollectionUtils.isEmpty(kItems)){
                kItems = kItems.stream().sorted(Comparator.comparing(KItem::getDate)).collect(Collectors.toList());
                List<AverageItem> fiveDayList = calculate5DayAverage(kItems);
                Integer fiveDate = averageDAO.queryAverageLatestDateByStockId(stockId,AverageType.FIVE_DAYS);
                if (!ObjectUtils.isEmpty(fiveDate)){
                    fiveDayList = fiveDayList.stream().filter(e->{
                        return Integer.valueOf(e.getDate()) > fiveDate;
                    }).collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(fiveDayList)){
                        averageDAO.batchInsertAverageItem(fiveDayList);
                    }
                }else {
                    averageDAO.batchInsertAverageItem(fiveDayList);
                }

                log.info("start to calculate  ten stockId:{}",stockId);
                List<AverageItem> tenDaysList = calculate10DayAverage(kItems);
                Integer tenDate = averageDAO.queryAverageLatestDateByStockId(stockId,AverageType.TEN_DAYS);
                if (!ObjectUtils.isEmpty(tenDate)){
                    tenDaysList = tenDaysList.stream().filter(e->{
                        return Integer.valueOf(e.getDate()) > tenDate;
                    }).collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(tenDaysList)){
                        averageDAO.batchInsertAverageItem(tenDaysList);
                    }
                }else {
                    averageDAO.batchInsertAverageItem(tenDaysList);
                }
                log.info("start to calculate  twenty stockId:{}",stockId);
                List<AverageItem> twentiesDaysList = calculate20DayAverage(kItems);
                Integer twentiesDate = averageDAO.queryAverageLatestDateByStockId(stockId,AverageType.TWENTY_DAYS);
                if (!ObjectUtils.isEmpty(twentiesDate)){
                    twentiesDaysList = twentiesDaysList.stream().filter(e->{
                        return Integer.valueOf(e.getDate()) > twentiesDate;
                    }).collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(twentiesDaysList)){
                        averageDAO.batchInsertAverageItem(twentiesDaysList);
                    }
                }else {
                    averageDAO.batchInsertAverageItem(twentiesDaysList);
                }
                log.info("start to calculate  sixties stockId:{}",stockId);
                List<AverageItem> sixtiesDaysList = calculate60DayAverage(kItems);
                Integer sixtiesDate = averageDAO.queryAverageLatestDateByStockId(stockId,AverageType.SIXTIES_DAYS);
                if (!ObjectUtils.isEmpty(sixtiesDate)){
                    sixtiesDaysList = sixtiesDaysList.stream().filter(e->{
                        return Integer.valueOf(e.getDate()) >= sixtiesDate;
                    }).collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(sixtiesDaysList)){
                        averageDAO.batchInsertAverageItem(sixtiesDaysList);
                    }
                }else {
                    averageDAO.batchInsertAverageItem(sixtiesDaysList);
                }
                log.info("start to calculate  oneHundredTwentiesDays stockId:{}",stockId);
                List<AverageItem> oneHundredTwentiesDaysList = calculate120DayAverage(kItems);
                Integer oneHundredTwentiesDay = averageDAO.queryAverageLatestDateByStockId(stockId,AverageType.ONE_HUNDRED_TWENTY_DAYS);
                if (!ObjectUtils.isEmpty(oneHundredTwentiesDay)){
                    oneHundredTwentiesDaysList = oneHundredTwentiesDaysList.stream().filter(e->{
                        return Integer.valueOf(e.getDate()) > oneHundredTwentiesDay;
                    }).collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(oneHundredTwentiesDaysList)){
                        averageDAO.batchInsertAverageItem(oneHundredTwentiesDaysList);
                    }
                }else {
                    averageDAO.batchInsertAverageItem(oneHundredTwentiesDaysList);
                }
            }
            Thread.sleep(500);
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
