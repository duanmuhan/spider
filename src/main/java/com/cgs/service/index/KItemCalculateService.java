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

    public void calculateKItem(){
        List<StockItem> stockItemList = stockItemDAO.queryAllStockList();
        if (CollectionUtils.isEmpty(stockItemList)){
            return;
        }
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        for (StockItem stockItem : stockItemList){
            List<KItem> kItems = kItemDAO.queryDateKItemsbyStockId(stockItem.getStockId(),1);
            if (CollectionUtils.isEmpty(kItems)){
                continue;
            }
            List<KItem> weekList = new ArrayList<>();
            Map<String,List<KItem>> kItemMap = kItems.stream().collect(Collectors.groupingBy(e->generateWeekIndex(e.getDate(),sdf,cal)));
            if (ObjectUtils.isEmpty(kItemMap)){
                continue;
            }
            for (Map.Entry<String,List<KItem>> entry : kItemMap.entrySet()){
                KItem kItem = new KItem();
                List<KItem> tmpDayKItem = entry.getValue();
                tmpDayKItem = tmpDayKItem.stream().sorted(Comparator.comparing(KItem::getDate)).collect(Collectors.toList());
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
                    dealAmount = dayKItem.getDealAmount();
                }
                kItem.setDealAmount(dealAmount);
                weekList.add(kItem);
            }
            if (!CollectionUtils.isEmpty(weekList)){
                kItemDAO.batchInsertKItem(weekList);
            }
        }
    }

    String generateWeekIndex(String date,SimpleDateFormat simpleDateFormat, Calendar calendar){
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

    boolean isSameMonth(String fromDate,String lastDate){
        return true;
    }

    boolean isSameYear(String fromDate,String lastDate){
        return true;
    }
}
