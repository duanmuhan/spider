package com.cgs.service.index;

import com.cgs.dao.KItemDAO;
import com.cgs.dao.StockItemDAO;
import com.cgs.entity.StockItem;
import com.cgs.entity.index.KItem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class KItemCalculateService {
    @Autowired
    private KItemDAO kItemDAO;
    @Autowired
    private StockItemDAO stockItemDAO;

    public void calculateKItem() throws InterruptedException {
        List<StockItem> stockItemList = stockItemDAO.queryAllStockList();
        if (CollectionUtils.isEmpty(stockItemList)){
            return;
        }
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        for (StockItem stockItem : stockItemList){
            log.info("calculate stock weekItem : {}",stockItem.getStockId());
            List<KItem> kItems = kItemDAO.queryDateKItemsbyStockId(stockItem.getStockId(),1);
            if (CollectionUtils.isEmpty(kItems)){
                continue;
            }
            Map<String,List<KItem>> kItemWeekMap = kItems.stream().collect(Collectors.groupingBy(e->generateWeekIndex(e.getDate(),sdf,cal)));
            if (ObjectUtils.isEmpty(kItemWeekMap)){
                continue;
            }
            List<KItem> weekList = new ArrayList<>();
            for (Map.Entry<String,List<KItem>> entry : kItemWeekMap.entrySet()){
                String latestDate = kItemDAO.queryLatestDateOfKItem(stockItem.getStockId(),2);
                KItem kItem = new KItem();
                List<KItem> tmpDayKItem = entry.getValue();
                if (!StringUtils.isEmpty(latestDate)){
                    tmpDayKItem.stream().filter(e->{
                       return Integer.valueOf(e.getDate()) > Integer.valueOf(latestDate);
                    }).collect(Collectors.toList());
                }
                kItem.setOpenPrice(tmpDayKItem.get(0).getOpenPrice());
                kItem.setClosePrice(tmpDayKItem.get(tmpDayKItem.size()-1).getClosePrice());
                kItem.setDate(entry.getKey());
                kItem.setStockId(stockItem.getStockId());
                kItem.setType(2);
                double high = tmpDayKItem.get(0).getHigh(),low = tmpDayKItem.get(0).getLow();
                Long dealAmount = 0L;
                for (KItem dayKItem : tmpDayKItem){
                    if (dayKItem.getHigh() > high){
                        high = dayKItem.getHigh();
                    }
                    if (dayKItem.getLow() < low){
                        low = dayKItem.getLow();
                    }
                    dealAmount = dealAmount + dayKItem.getDealAmount();
                }
                kItem.setDealAmount(dealAmount);
                kItem.setLow(low);
                kItem.setHigh(high);
                weekList.add(kItem);
            }
            if (!CollectionUtils.isEmpty(weekList)){
                kItemDAO.batchInsertKItem(weekList);
            }
            log.info("calculate stock month : {}",stockItem.getStockId());
            List<KItem> monthList = new ArrayList<>();
            Map<String,List<KItem>> monthKItemMap = kItems.stream().collect(Collectors.groupingBy(e->generateMonthIndex(e.getDate(),sdf,cal)));
            if (ObjectUtils.isEmpty(monthKItemMap)){
                continue;
            }
            for (Map.Entry<String,List<KItem>> entry : monthKItemMap.entrySet()){
                String latestDate = kItemDAO.queryLatestDateOfKItem(stockItem.getStockId(),3);
                List<KItem> tmpMonthItem = entry.getValue();
                if (!StringUtils.isEmpty(latestDate)){
                    tmpMonthItem.stream().filter(e->{
                        return Integer.valueOf(e.getDate()) > Integer.valueOf(latestDate);
                    }).collect(Collectors.toList());
                }
                KItem kItem = new KItem();
                kItem.setOpenPrice(tmpMonthItem.get(0).getOpenPrice());
                kItem.setClosePrice(tmpMonthItem.get(tmpMonthItem.size()-1).getClosePrice());
                kItem.setDate(entry.getKey());
                kItem.setStockId(stockItem.getStockId());
                kItem.setType(3);
                double high = tmpMonthItem.get(0).getHigh(),low = tmpMonthItem.get(0).getLow();
                Long dealAmount = 0L;
                for (KItem dayKItem : tmpMonthItem){
                    if (dayKItem.getHigh() > high){
                        high = dayKItem.getHigh();
                    }
                    if (dayKItem.getLow() < low){
                        low = dayKItem.getLow();
                    }
                    dealAmount = dealAmount + dayKItem.getDealAmount();
                }
                kItem.setDealAmount(dealAmount);
                kItem.setLow(low);
                kItem.setHigh(high);
                monthList.add(kItem);
            }
            if (!CollectionUtils.isEmpty(monthList)){
                kItemDAO.batchInsertKItem(monthList);
            }
            Thread.sleep(10);
        }
    }

    private String generateWeekIndex(String date,SimpleDateFormat simpleDateFormat, Calendar calendar){
        String beginDate = "";
        try {
            calendar.setTime(simpleDateFormat.parse(date));
            calendar.setFirstDayOfWeek(Calendar.MONDAY);
            int dayWeek = calendar.get(Calendar.DAY_OF_WEEK);// 获得当前日期是一个星期的第几天
            int day = calendar.get(Calendar.DAY_OF_WEEK);
            // 获取该周第一天
            calendar.add(Calendar.DATE, calendar.getFirstDayOfWeek() - day);
            beginDate = simpleDateFormat.format(calendar.getTime());
        } catch (ParseException e) {
            log.error("error in generateWeekIndex:{}",e);
        }
        return beginDate;
    }

    private String generateMonthIndex(String dateStr,SimpleDateFormat simpleDateFormat, Calendar calendar){
        String beginDate = "";
        try {
            Date date = simpleDateFormat.parse(dateStr);
            calendar.setTime(date);
            calendar.set(Calendar.DAY_OF_MONTH,1);
            calendar.add(Calendar.MONTH, 0);
            return simpleDateFormat.format(calendar.getTime());
        }catch (Exception e){
            log.error("error in generateMonthIndex:{}",e);
        }
        return beginDate;
    }
}
