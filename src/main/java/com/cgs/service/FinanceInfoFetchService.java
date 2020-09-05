package com.cgs.service;

import com.alibaba.fastjson.JSON;
import com.cgs.dao.FinanceInfoDAO;
import com.cgs.dao.StockItemDAO;
import com.cgs.dto.FinanceInfoDTO;
import com.cgs.entity.FinanceInfo;
import com.cgs.entity.StockItem;
import com.cgs.util.HttpRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FinanceInfoFetchService {

    @Autowired
    private FinanceInfoDAO financeInfoDAO;
    @Autowired
    private StockItemDAO stockItemDAO;

    @Value("${finance.info.url}")
    private String financeFetchUrl;


    public void fetchFinanceInfo() throws InterruptedException {
        List<StockItem> stockItems = stockItemDAO.queryAllStockList();
        if (CollectionUtils.isEmpty(stockItems)){
            return;
        }
        financeInfoDAO.clearFinanceInfo();
        for (StockItem item : stockItems){
            String requestUrl = financeFetchUrl.replace("stockcode",item.getExchangeId().toUpperCase() + item.getStockId());
            String result= HttpRequestUtil.getRequestDirectly(requestUrl);
            if (StringUtils.isEmpty(result)){
                continue;
            }
            List<FinanceInfoDTO> dtoList = JSON.parseArray(result, FinanceInfoDTO.class);
            if (CollectionUtils.isEmpty(dtoList)){
                continue;
            }
            List<FinanceInfo> infos = dtoList.stream().map(e-> e.convertToStockInfo(item.getStockId())).collect(Collectors.toList());
            financeInfoDAO.batchInsertFinanceInfo(infos);
            Thread.sleep(500);

        }

    }
}
