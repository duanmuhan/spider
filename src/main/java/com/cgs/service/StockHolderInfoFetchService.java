package com.cgs.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cgs.dao.StockHolderDAO;
import com.cgs.dao.StockHolderTopTenDAO;
import com.cgs.dao.StockItemDAO;
import com.cgs.entity.StockHolder;
import com.cgs.entity.StockItem;
import com.cgs.util.HttpRequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class StockHolderInfoFetchService {

    @Autowired
    private StockHolderDAO stockHolderDAO;
    @Autowired
    private StockHolderTopTenDAO stockHolderTopTenDAO;
    @Autowired
    private StockItemDAO stockItemDAO;

    @Value("${stock.holder.url}")
    private String stockHolderUrl;

    public void fetchStockHolderInfo() throws Exception{
        List<StockItem> stockItemList = stockItemDAO.queryAllStockList();
        if (CollectionUtils.isEmpty(stockItemList)){
            return;
        }
        List<StockHolder> list = new ArrayList<>();
        stockItemList.parallelStream().forEach(e->{
            String requestUrl = stockHolderUrl.concat(e.getExchangeId().toUpperCase() + e.getStockId());
            String result = HttpRequestUtil.getRequestDirectly(requestUrl);
            if (!StringUtils.isEmpty(result)){
                JSONObject object = JSON.parseObject(result);
                String stockHolderStr = object.getString("gdrs");
                if (!StringUtils.isEmpty(stockHolderStr)){

                }
                String stockHolderTopTenStr = object.getString("sdltgd");
                if (!StringUtils.isEmpty(stockHolderTopTenStr)){

                }
                String stockTop = object.getString("zlcc");
                if (!StringUtils.isEmpty(stockTop)){

                }
            }
        });
    }

}
