package com.cgs.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cgs.dao.KItemDAO;
import com.cgs.dao.StockItemDAO;
import com.cgs.dao.StockMoodIndexFetchDAO;
import com.cgs.entity.StockMoodIndex;
import com.cgs.entity.index.KItem;
import com.cgs.util.HttpRequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StockMoodIndexFetchService {

    @Value("${kitem.sh.url}")
    private String originRequestUrl;

    @Autowired
    private KItemDAO kItemDAO;
    @Autowired
    private StockMoodIndexFetchDAO stockMoodIndexFetchDAO;
    @Autowired
    private StockItemDAO stockItemDAO;

    public void fetchStockMoodIndexService(){
        long timestamp = System.currentTimeMillis();
        String requestUrl = originRequestUrl.replace("stock_id","000001")
                .replace("timestamp",String.valueOf(timestamp));
        String result = HttpRequestUtil.getRequestDirectly(requestUrl);
        if (StringUtils.isEmpty(result)){
            return;
        }
        JSONObject jsonObject = JSON.parseObject(result);
        if (ObjectUtils.isEmpty(jsonObject)){
            return;
        }
        int total = jsonObject.getInteger("total");
        String finalRequestUrl = requestUrl.replace("-300",String.valueOf(total * -1));
        String finalResult = HttpRequestUtil.getRequestDirectly(finalRequestUrl);
        if (StringUtils.isEmpty(finalResult)){
            return;
        }
        JSONObject secondJsonObject = JSON.parseObject(finalResult);
        String klineStr = secondJsonObject.getString("kline");
        if (StringUtils.isEmpty(klineStr)){
            return;
        }
        List<KItem> kItems = new ArrayList<>();
        List<String> klineList = JSON.parseArray(klineStr,String.class);
        for (String str : klineList){
            List<String> kItemList = JSON.parseArray(str,String.class);
            KItem kItem = new KItem();
            kItem.setDate(kItemList.get(0));
            kItem.setOpenPrice(Double.valueOf(kItemList.get(1)));
            kItem.setHigh(Double.valueOf(kItemList.get(2)));
            kItem.setLow(Double.valueOf(kItemList.get(3)));
            kItem.setClosePrice(Double.valueOf(kItemList.get(4)));
            kItem.setType(1);
            kItem.setDealAmount(Long.valueOf(kItemList.get(5)));
            kItem.setStockId("000001-sh");
            kItems.add(kItem);
        }
        log.info("kItem is :{}",JSON.toJSON(kItems));
        String date = kItemDAO.queryLatestDateOfKItem("000001-sh",1);
        log.info("kItem date is :{}",date);
        if (!StringUtils.isEmpty(date)){
            kItems = kItems.stream().filter(e->{
                return Integer.valueOf(e.getDate()) > Integer.valueOf(date);
            }).collect(Collectors.toList());
        }
        kItems = kItems.stream().filter(e->{
            return Integer.valueOf(e.getDate()) > 20200601;
        }).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(kItems)){
            return;
        }
        int stockTotalCount = stockItemDAO.currentStockCount();
        List<StockMoodIndex> stockMoodIndexList = new ArrayList<>();
        for (KItem kItem : kItems){
            log.info("fetchStockMoodIndexService: {}",kItem.getDate());
            List<KItem> kItemList = kItemDAO.querySecondLatestDateOfTargetDate(kItem.getDate());
            if (CollectionUtils.isEmpty(kItemList)){
                continue;
            }
            List<KItem> targetDateKItem = kItemDAO.queryKItemByDate(kItem.getDate(),1);
            if (CollectionUtils.isEmpty(targetDateKItem)){
                continue;
            }
            Map<String,KItem> secondLatestMap = kItemList.stream().collect(Collectors.toMap(KItem::getStockId, Function.identity()));
            int increaseCount = 0;
            for (KItem targetKItem : targetDateKItem){
                if (!secondLatestMap.containsKey(targetKItem.getStockId())){
                    continue;
                }
                KItem secondKItem = secondLatestMap.get(targetKItem.getStockId());
                double rate = (targetKItem.getClosePrice() - secondKItem.getClosePrice()) / secondKItem.getClosePrice();
                if (rate >= 0.098){
                    increaseCount = increaseCount + 1;
                }
            }
            StockMoodIndex stockMoodIndex = new StockMoodIndex();
            stockMoodIndex.setStockIncreaseCount(increaseCount);
            stockMoodIndex.setReleaseDate(kItem.getDate());
            stockMoodIndex.setStockPoint(kItem.getClosePrice());
            stockMoodIndex.setStockIncreaseRate(String.valueOf(increaseCount/stockTotalCount));
            stockMoodIndex.setStockTotalCount(stockTotalCount);
            stockMoodIndexList.add(stockMoodIndex);
        }
        kItemDAO.batchInsertKItem(kItems);
        if (!CollectionUtils.isEmpty(stockMoodIndexList)){
            stockMoodIndexFetchDAO.batchInsertStockMoodIndexItem(stockMoodIndexList);
        }
    }
}
