package com.cgs.service.index;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cgs.dao.KItemDAO;
import com.cgs.dao.StockItemDAO;
import com.cgs.entity.StockItem;
import com.cgs.entity.index.KItem;
import com.cgs.util.HttpRequestUtil;
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
public class KItemFetchService {

    @Autowired
    private KItemDAO kItemDAO;
    @Autowired
    private StockItemDAO stockItemDAO;

    @Value("${kitem.sz.url}")
    private String szRequestUrl;
    @Value("${kitem.sh.url}")
    private String shRequestUrl;

    public void fetchKItem(){
        List<StockItem> list = stockItemDAO.queryAllStockList();
        if (CollectionUtils.isEmpty(list)){
            return;
        }
        Map<String,List<StockItem>> stockMap = list.stream().collect(Collectors.groupingBy(StockItem::getExchangeId));
        if (ObjectUtils.isEmpty(stockMap)){
            return;
        }
        List<KItem> kItems = new ArrayList<>();
        List<StockItem> szStockList = stockMap.get("sz");
        List<StockItem> shStockList = stockMap.get("sh");
        if (!CollectionUtils.isEmpty(szStockList)){
            for (StockItem item : szStockList){
                double random = System.currentTimeMillis() / 10E13;
                String requestUrl = szRequestUrl.replace("randomno",String.valueOf(random))
                        .replace("stockcode",item.getStockId()).replace("cyclecode",String.valueOf(32));
                String result = HttpRequestUtil.getRequestDirectly(requestUrl);
                if (StringUtils.isEmpty(requestUrl)){
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
                    kItem.setDate(itemList.get(0));
                    kItem.setOpenPrice(Double.valueOf(itemList.get(1)));
                    kItem.setClosePrice(Double.valueOf(itemList.get(2)));
                    kItem.setLow(Double.valueOf(itemList.get(3)));
                    kItem.setHigh(Double.valueOf(itemList.get(4)));
                    kItem.setDealAmount(Long.valueOf(itemList.get(7)));
                    kItem.setDealCash(Double.valueOf(itemList.get(8)));
                    kItems.add(kItem);
                });
            }
        }
        if (!CollectionUtils.isEmpty(shStockList)){
            for (StockItem item : shStockList){

            }
        }
        kItemDAO.batchInsertKItem(kItems);
    }
}
