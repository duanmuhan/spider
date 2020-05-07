package com.cgs.service;

import com.alibaba.fastjson.JSON;
import com.cgs.dao.StockItemDAO;
import com.cgs.util.HttpRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Map;

@Service
public class StockItemFetchService {

    @Autowired
    private StockItemDAO stockDAO;

    @Value("sz.stock.list.url")
    private String szStockListUrl;

    @Value("sh.stock.list.url")
    private String shStockListUrl;

    public void fetchStockList() throws IOException {
        String szRequest = HttpRequestUtil.getRequest(szStockListUrl);
        if (StringUtils.isEmpty(szRequest)){
            return;
        }
        Map<String,Object> resultMap = JSON.parseObject(szRequest);
        String shRequest = HttpRequestUtil.getRequest(shStockListUrl);
        if (StringUtils.isEmpty(shRequest)){
            return;
        }

    }
}
