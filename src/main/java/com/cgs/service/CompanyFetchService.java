package com.cgs.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cgs.dao.CompanyDAO;
import com.cgs.dao.StockItemDAO;
import com.cgs.dto.CompanyInfoDTO;
import com.cgs.entity.StockItem;
import com.cgs.util.HttpRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class CompanyFetchService {

    @Autowired
    private CompanyDAO companyDAO;
    @Autowired
    private StockItemDAO stockItemDAO;
    @Value("${company.survey.url}")
    private String companySurveyUrl;

    public void fetchCompanyInfo() throws IOException {
        List<StockItem> stockList = stockItemDAO.queryAllStockList();
        if (CollectionUtils.isEmpty(stockList)){
            return;
        }
        List<CompanyInfoDTO> companyInfoDTOS = new ArrayList<>();
        List<StockItem> stockItems = stockItemDAO.queryAllStockList();
        if (CollectionUtils.isEmpty(stockItems)){
            return;
        }
        for (StockItem item : stockItems){
            String param = item.getExchangeId().toUpperCase() + item.getStockId();
            companySurveyUrl = companySurveyUrl.replace("sharecode",param);
            String response = HttpRequestUtil.getRequestDirectly(companySurveyUrl);
            if (StringUtils.isEmpty(response)){
                continue;
            }
            JSONObject object = JSON.parseObject(response);
            CompanyInfoDTO companyInfoDTO = JSON.parseObject(object.getString("jbzl"),CompanyInfoDTO.class);

        }
    }

}
