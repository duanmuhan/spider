package com.cgs.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cgs.dao.StockItemDAO;
import com.cgs.entity.StockItem;
import com.cgs.dto.StockItemSzDTO;
import com.cgs.util.HttpRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;
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

    public void fetchStockList() throws IOException {

        int pageNo = 1;
        szStockListUrl = szStockListUrl.replace("pageno",String.valueOf(pageNo));
        double random = System.currentTimeMillis() / 10E13;
        szStockListUrl = szStockListUrl.replace("randno",String.valueOf(random));
        String szRequest = HttpRequestUtil.getRequestDirectly(szStockListUrl);
        if (StringUtils.isEmpty(szRequest)){
            return;
        }
        JSONArray jsonArray = JSON.parseArray(szRequest);
        boolean isEmpty = true;
        for (int i=0; i<jsonArray.size(); i++){
            JSONObject object = jsonArray.getJSONObject(i);
            String data = object.getString("data");
            if (StringUtils.isEmpty(data)){
                continue;
            }
            List<StockItemSzDTO> stockList = JSON.parseArray(data,StockItemSzDTO.class);
            if (CollectionUtils.isEmpty(stockList)){
                continue;
            }
            List<StockItem> list = stockList.stream().map(e->e.convertToStockItem()).collect(Collectors.toList());
            //stockItemDAO.batchInsertStockItem(list);
        }

        if (StringUtils.isEmpty(shStockListUrl) || StringUtils.isEmpty(stockReferUrl)){
            return;
        }
        shStockListUrl = shStockListUrl.replace("pageno","1")
                .replace("timestamp",String.valueOf(System.currentTimeMillis()));
        String shRequest = HttpRequestUtil.getRequestWithRefer(shStockListUrl,stockReferUrl,stockHostUrl);
        if (StringUtils.isEmpty(shRequest)){
            return;
        }

    }
}
