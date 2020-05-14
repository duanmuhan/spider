package com.cgs.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cgs.dao.PlateDAO;
import com.cgs.dao.StockPlateInfoMappingDAO;
import com.cgs.entity.PlateInfo;
import com.cgs.entity.StockPlateInfoMapping;
import com.cgs.util.HttpRequestUtil;
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
public class PlateStockMappingFetchService {

    @Autowired
    private PlateDAO plateDAO;

    @Autowired
    private StockPlateInfoMappingDAO stockPlateInfoMappingDAO;

    @Value("${plate.stock.mapping.url}")
    private String plateInfoMappingFetchUrl;

    public void fetchPlatStockMapping() throws IOException, InterruptedException {
        List<PlateInfo> plateInfos = plateDAO.queryAllPlateInfo();
        if (CollectionUtils.isEmpty(plateInfos)){
            return;
        }
        List<String> plateIds = plateInfos.stream().map(e->e.getPlateId()).collect(Collectors.toList());
        List<StockPlateInfoMapping> mappingList = new ArrayList<>();
        for (String id : plateIds){
            int pageNo = 1;
            int pageSize = 100;
            List<StockPlateInfoMapping> plateInfoMappings = new ArrayList<>();
            while (true){
                String requestUrl = plateInfoMappingFetchUrl.replace("pageno",String.valueOf(pageNo))
                        .replace("pagesize",String.valueOf(pageSize))
                        .replace("timestamp",String.valueOf(System.currentTimeMillis()));
                String result = HttpRequestUtil.getRequestDirectly(requestUrl);
                if (StringUtils.isEmpty(result)){
                    break;
                }
                JSONObject jsonObject = JSON.parseObject(result);
                JSONObject data = jsonObject.getJSONObject("data");
                if (ObjectUtils.isEmpty(data)){
                    break;
                }
                JSONArray array = data.getJSONArray("diff");
                if (ObjectUtils.isEmpty(array) || array.size() == 0){
                    break;
                }
                List<StockPlateInfoMapping> list =  array.stream().map(e->{
                    StockPlateInfoMapping mapping = new StockPlateInfoMapping();
                    return mapping;
                }).collect(Collectors.toList());
                plateInfoMappings.addAll(list);
                pageNo = pageNo + 1;
                Thread.sleep(5 * 1000);
            }
            mappingList.addAll(plateInfoMappings);
        }
        stockPlateInfoMappingDAO.batchInsertStockPlateInfoMapping(mappingList);
    }
}
