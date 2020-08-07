package com.cgs.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cgs.dao.StockItemDAO;
import com.cgs.entity.StockItem;
import com.cgs.util.HttpRequestUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class StockTechnologyService {

    @Value("${stock.technology.url}")
    private String requestUrl;

    @Autowired
    private StockItemDAO stockItemDAO;

    public void fetchStockTechnologyInfo() throws InterruptedException {
        List<StockItem> stockItemList = stockItemDAO.queryAllStockList();
        if (CollectionUtils.isEmpty(stockItemList)){
            return;
        }
        for (StockItem stockItem : stockItemList){
            String url = requestUrl.replace("stockId",stockItem.getStockId());
            String content = HttpRequestUtil.getRequestWithWenCaiHeader(url);
            if (StringUtils.isEmpty(content)){
                continue;
            }
            JSONObject object = JSON.parseObject(content);
            if (ObjectUtils.isEmpty(object)){
                continue;
            }

            Thread.sleep(1000);
        }
    }
}
