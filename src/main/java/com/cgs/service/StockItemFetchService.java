package com.cgs.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cgs.dao.StockItemDAO;
import com.cgs.dto.StockItemShDTO;
import com.cgs.dto.StockItemSzDTO;
import com.cgs.entity.StockItem;
import com.cgs.util.HttpRequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
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
        stockItemDAO.deleteAll();
        stockItemDAO.batchInsertStockItem(results);
    }

    private List<StockItem> fetchSzExchangeStockList() throws IOException, InterruptedException {
        int pageNo = 1;
        List<StockItem> stockItemList = new ArrayList<>();
        while (true){
            boolean isEmpty = true;
            String requestUrl = szStockListUrl.replace("pageno",String.valueOf(pageNo));
            double random = System.currentTimeMillis() / 10E13;
            requestUrl = requestUrl.replace("randno",String.valueOf(random));
            String szRequest = HttpRequestUtil.getRequestDirectly(requestUrl);
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
            log.info("szPageNo is :{}", pageNo);
            Thread.sleep(1000);
        }
        return stockItemList;
    }

    private List<StockItem> fetchShExchangeStockList() throws IOException, InterruptedException {
        if (StringUtils.isEmpty(shStockListUrl) || StringUtils.isEmpty(stockReferUrl)){
            return new ArrayList<>();
        }
        List<StockItem> resultList = new ArrayList<>();
        int pageNo = 1;
        while (true){
            String requestUrl = shStockListUrl.replace("pageno",String.valueOf(pageNo)).replace("beginpage",String.valueOf(pageNo)).replace("endpage",String.valueOf(10*pageNo + 1))
                    .replace("timestamp",String.valueOf(System.currentTimeMillis()));
            String shRequest = HttpRequestUtil.getRequestWithRefer(requestUrl,stockReferUrl);
            if (StringUtils.isEmpty(shRequest)){
                break;
            }
            JSONObject jsonObject = JSON.parseObject(shRequest).getJSONObject("pageHelp");
            if (ObjectUtils.isEmpty(jsonObject)){
                break;
            }
            String data = jsonObject.getString("data");
            if (StringUtils.isEmpty(data)){
                break;
            }
            List<StockItemShDTO> list = JSON.parseArray(data,StockItemShDTO.class);
            if (CollectionUtils.isEmpty(list)){
                break;
            }
            List<StockItem> shList = list.stream().map(e->e.convertToStockItem()).collect(Collectors.toList());
            resultList.addAll(shList);
            pageNo ++;
            log.info("shPageNo is :{}", pageNo);
            Thread.sleep(1000);
        }
        return resultList;
    }
}
