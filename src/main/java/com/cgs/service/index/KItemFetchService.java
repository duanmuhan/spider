package com.cgs.service.index;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cgs.dao.KItemDAO;
import com.cgs.dao.StockItemDAO;
import com.cgs.entity.KItemDate;
import com.cgs.entity.StockItem;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class KItemFetchService {

    @Autowired
    private KItemDAO kItemDAO;
    @Autowired
    private StockItemDAO stockItemDAO;

    @Value("${kitem.sz.url}")
    private String szRequestUrl;
    @Value("${kitem.sh.url}")
    private String shRequestUrl;

    public void fetchKItem() throws InterruptedException {
        List<StockItem> list = stockItemDAO.queryAllStockList();
        if (CollectionUtils.isEmpty(list)){
            return;
        }
        Map<String,List<StockItem>> stockMap = list.stream().collect(Collectors.groupingBy(StockItem::getExchangeId));
        if (ObjectUtils.isEmpty(stockMap)){
            return;
        }
        List<StockItem> szStockList = stockMap.get("sz");
        List<StockItem> shStockList = stockMap.get("sh");
        List<KItemDate> latestDateList = kItemDAO.queryKItemLatestDate();
        Map<String,String> latestDateMap = new HashMap<>();
        if (!CollectionUtils.isEmpty(latestDateList)){
            latestDateList.stream().forEach(e->{
                latestDateMap.put(e.getStockId(),e.getDate());
            });
        }
        if (!CollectionUtils.isEmpty(szStockList)){
            for (StockItem item : szStockList){
                List<KItem> kItems = new ArrayList<>();
                double random = System.currentTimeMillis() / 10E13;
                String requestUrl = szRequestUrl.replace("randomno",String.valueOf(random))
                        .replace("stockcode",item.getStockId()).replace("cyclecode",String.valueOf(32));
                String result = HttpRequestUtil.getRequestDirectly(requestUrl);
                if (StringUtils.isEmpty(result)){
                    continue;
                }
                JSONObject jsonObject = JSON.parseObject(result);
                String data = jsonObject.getString("data");
                if (StringUtils.isEmpty(data)){
                    continue;
                }
                JSONObject dataObject = JSON.parseObject(data);
                if (ObjectUtils.isEmpty(dataObject)){
                    continue;
                }
                String picUpData = dataObject.getString("picupdata");
                if (StringUtils.isEmpty(picUpData)){
                    continue;
                }
                List<String> picUpDataList = JSON.parseArray(picUpData,String.class);
                if (CollectionUtils.isEmpty(picUpDataList)){
                    continue;
                }
                picUpDataList.forEach(e->{
                    KItem kItem = new KItem();
                    List<String> itemList = JSON.parseArray(e,String.class);
                    kItem.setStockId(item.getStockId());
                    kItem.setDate(itemList.get(0).replace("-",""));
                    kItem.setOpenPrice(Double.valueOf(itemList.get(1)));
                    kItem.setClosePrice(Double.valueOf(itemList.get(2)));
                    kItem.setLow(Double.valueOf(itemList.get(3)));
                    kItem.setHigh(Double.valueOf(itemList.get(4)));
                    kItem.setType(1);
                    kItem.setDealAmount(Long.valueOf(itemList.get(7)));
                    kItem.setDealCash(Double.valueOf(itemList.get(8)));
                    kItems.add(kItem);
                });
                log.info("sz stock k item id is :{}",item.getStockId());
                if (latestDateMap.containsKey(item.getStockId())){
                    List<KItem> resultList = kItems.stream().filter(e->{
                        return Integer.valueOf(e.getDate()) > Integer.valueOf(latestDateMap.get(item.getStockId()));
                    }).collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(resultList)){
                        kItemDAO.batchInsertKItem(resultList);
                    }
                }else {
                    kItemDAO.batchInsertKItem(kItems);
                }
                Thread.sleep( 500);
            }
        }
        if (!CollectionUtils.isEmpty(shStockList)){
            for (StockItem item : shStockList){
                List<KItem> kItems = new ArrayList<>();
                long timestamp = System.currentTimeMillis();
                String requestUrl = shRequestUrl.replace("stock_id",item.getStockId())
                        .replace("timestamp",String.valueOf(timestamp));
                String result = HttpRequestUtil.getRequestDirectly(requestUrl);
                if (StringUtils.isEmpty(result)){
                    continue;
                }
                JSONObject jsonObject = JSON.parseObject(result);
                if (ObjectUtils.isEmpty(jsonObject)){
                    continue;
                }
                int total = jsonObject.getInteger("total");
                String finalRequestUrl = requestUrl.replace("-300",String.valueOf(total * -1));
                String finalResult = HttpRequestUtil.getRequestDirectly(finalRequestUrl);
                if (StringUtils.isEmpty(finalRequestUrl)){
                    continue;
                }
                JSONObject secondJsonObject = JSON.parseObject(finalResult);
                String klineStr = secondJsonObject.getString("kline");
                if (StringUtils.isEmpty(klineStr)){
                    continue;
                }
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
                    kItem.setStockId(item.getStockId());
                    kItems.add(kItem);
                }
                log.info("sh stock k item id is :{}",item.getStockId());
                if (latestDateMap.containsKey(item.getStockId())){
                    List<KItem> resultList = kItems.stream().filter(e->{
                        return Integer.valueOf(e.getDate()) > Integer.valueOf(latestDateMap.get(item.getStockId()));
                    }).collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(resultList)){
                        kItemDAO.batchInsertKItem(resultList);
                    }
                }else {
                    kItemDAO.batchInsertKItem(kItems);
                }
                Thread.sleep(500);
            }
        }
    }
}
