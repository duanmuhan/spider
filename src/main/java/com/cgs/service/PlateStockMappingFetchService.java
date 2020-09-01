package com.cgs.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cgs.dao.PlateDAO;
import com.cgs.dao.StockPlateInfoMappingDAO;
import com.cgs.entity.PlateInfo;
import com.cgs.entity.StockPlateInfoMapping;
import com.cgs.util.HttpRequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
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
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<StockPlateInfoMapping> plateInfoMappings = new ArrayList<>();
        for (PlateInfo plateInfo : plateInfos){
            int pageNo = 1;
            int pageSize = 100;
            while (true){
                String requestUrl = plateInfoMappingFetchUrl.replace("pageno",String.valueOf(pageNo))
                        .replace("pagesize",String.valueOf(pageSize)).replace("plateid",plateInfo.getPlateId())
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
                List<StockPlateInfoMapping> list = new ArrayList<>();
                for (int i=0; i<array.size(); i++){
                    StockPlateInfoMapping mapping = new StockPlateInfoMapping();
                    mapping.setDate(simpleDateFormat.format(new Date()));
                    mapping.setPlateId(plateInfo.getPlateId());
                    mapping.setPlateName(plateInfo.getPlateName());
                    mapping.setStockId(array.getJSONObject(i).getString("f12"));
                    mapping.setStockName(array.getJSONObject(i).getString("f14"));
                    list.add(mapping);
                }
                log.info("stock info mapping is :{}",list);
                plateInfoMappings.addAll(list);
                pageNo = pageNo + 1;
                Thread.sleep(5 * 1000);
            }
        }
        stockPlateInfoMappingDAO.deleteStockPlateInfoMappingDAO();
        stockPlateInfoMappingDAO.batchInsertStockPlateInfoMapping(plateInfoMappings);
    }
}
