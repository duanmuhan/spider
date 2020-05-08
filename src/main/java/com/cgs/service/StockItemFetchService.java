package com.cgs.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cgs.dao.StockItemDAO;
import com.cgs.dto.StockItemShDTO;
import com.cgs.entity.StockItem;
import com.cgs.dto.StockItemSzDTO;
import com.cgs.util.HttpRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class StockItemFetchService {

    @Autowired
    private StockItemDAO stockItemDAO;

    @Value("${sz.stock.list.url}")
    private String szStockListUrl;

    @Value("${sh.stock.list.url}")
    private String shStockListUrl;

    @Value("${sh.stock.refer.url}")
    private String stockReferUrl;

    @Value("${sh.stock.host.url}")
    private String stockHostUrl;

    public void fetchStockList() throws IOException, InterruptedException {
        List<StockItem> results = new ArrayList<>();
        List<StockItem> szExchangeStockList = fetchSzExchangeStockList();
        List<StockItem> shExchangeStockList = fetchShExchangeStockList();
        results.addAll(szExchangeStockList);
        results.addAll(shExchangeStockList);
        stockItemDAO.batchInsertStockItem(results);
    }

    private List<StockItem> fetchSzExchangeStockList() throws IOException, InterruptedException {
        int pageNo = 1;
        List<StockItem> stockItemList = new ArrayList<>();
        while (true){
            boolean isEmpty = true;
            szStockListUrl = szStockListUrl.replace("pageno",String.valueOf(pageNo));
            double random = System.currentTimeMillis() / 10E13;
            szStockListUrl = szStockListUrl.replace("randno",String.valueOf(random));
            String szRequest = HttpRequestUtil.getRequestDirectly(szStockListUrl);
            if (StringUtils.isEmpty(szRequest)){
                return new ArrayList<>();
            }
            JSONArray jsonArray = JSON.parseArray(szRequest);
            for (int i=0; i<jsonArray.size(); i++){
                JSONObject object = jsonArray.getJSONObject(i);
                String data = object.getString("data");
                if (!StringUtils.isEmpty(data)){
                    List<StockItemSzDTO> stockList = JSON.parseArray(data,StockItemSzDTO.class);
                    if (!CollectionUtils.isEmpty(stockList)){
                        List<StockItem> list = stockList.stream().map(e->e.convertToStockItem()).collect(Collectors.toList());
                        stockItemList.addAll(list);
                        isEmpty = false;
                    }
                }
            }
            if (isEmpty){
                break;
            }
            pageNo ++;
            Thread.sleep(1000 * 10);
        }
        return stockItemList;
    }

    private List<StockItem> fetchShExchangeStockList() throws IOException, InterruptedException {
        if (StringUtils.isEmpty(shStockListUrl) || StringUtils.isEmpty(stockReferUrl)){
            return new ArrayList<>();
        }
        List<StockItem> resultList = new ArrayList<>();
        int pageNo = 1;
        boolean isEmpty = false;
        while (!isEmpty){
            shStockListUrl = shStockListUrl.replace("pageno",String.valueOf(pageNo)).replace("beginpage",String.valueOf(pageNo)).replace("endpage",String.valueOf(10*pageNo + 1))
                    .replace("timestamp",String.valueOf(System.currentTimeMillis()));
            String shRequest = HttpRequestUtil.getRequestWithRefer(shStockListUrl,stockReferUrl);
            if (StringUtils.isEmpty(shRequest)){
                isEmpty = true;
            }
            int firstIndex  = shRequest.indexOf('(');
            int lastIndex = shRequest.lastIndexOf(')');
            String str = shRequest.substring(firstIndex+1,lastIndex);
            if (StringUtils.isEmpty(str)){
                isEmpty=true;
            }
            JSONObject jsonObject = JSON.parseObject(str).getJSONObject("pageHelp");
            if (ObjectUtils.isEmpty(jsonObject)){
                isEmpty = true;
            }
            String data = jsonObject.getString("data");
            if (StringUtils.isEmpty(data)){
                isEmpty = true;
            }
            List<StockItemShDTO> list = JSON.parseArray(data,StockItemShDTO.class);
            List<StockItem> shList = list.stream().map(e->e.convertToStockItem()).collect(Collectors.toList());
            resultList.addAll(shList);
            pageNo ++;
            Thread.sleep(1000 * 10);
        }
        return resultList;
    }
}
