package com.cgs.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cgs.dao.PlateDAO;
import com.cgs.entity.PlateInfo;
import com.cgs.util.HttpRequestUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
* 板块信息的获取
* */
@Service
@Log4j2
public class PlateFetchService {

    @Autowired
    private PlateDAO plateDAO;

    @Value("plate.concept.fetch.url")
    private String plateConceptUrl;
    @Value("plate.concept.area.url")
    private String plateAreaFetchUrl;
    @Value("plate.trade.fetch.url")
    private String plateTradeFetchUrl;

    public void fetchPlateInfo() throws IOException {
        if (StringUtils.isEmpty(plateConceptUrl) || StringUtils.isEmpty(plateAreaFetchUrl) || StringUtils.isEmpty(plateTradeFetchUrl)){
            return;
        }
        List<PlateInfo> conceptPlate = fetchConceptPlate();
        List<PlateInfo> areaPlate = fetchAreaPlate();
        List<PlateInfo> tradePlate = fetchTradePlate();
        conceptPlate.addAll(areaPlate);
        conceptPlate.addAll(tradePlate);
        plateDAO.batchInsertPlateInfo(conceptPlate);
    }

    private List<PlateInfo> fetchConceptPlate() throws IOException {
        List<PlateInfo> plateInfos = new ArrayList<>();
        String pageContentOfConcept = HttpRequestUtil.getRequest(plateConceptUrl);
        if (StringUtils.isEmpty(pageContentOfConcept)){
            return plateInfos;
        }
        JSONObject object = JSON.parseObject(pageContentOfConcept);
        JSONObject data = object.getJSONObject("data");
        if (ObjectUtils.isEmpty(data)){
            return plateInfos;
        }

        return plateInfos;
    }

    private List<PlateInfo> fetchAreaPlate() throws IOException {
        List<PlateInfo> plateInfos = new ArrayList<>();
        String pageContentOfArea = HttpRequestUtil.getRequest(plateConceptUrl);
        if (StringUtils.isEmpty(pageContentOfArea)){
            return plateInfos;
        }
        JSONObject object = JSON.parseObject(pageContentOfArea);
        JSONObject data = object.getJSONObject("data");
        return plateInfos;
    }

    private List<PlateInfo> fetchTradePlate() throws IOException {
        List<PlateInfo> plateInfos = new ArrayList<>();
        String pageContentOfTrade = HttpRequestUtil.getRequest(plateTradeFetchUrl);
        if (StringUtils.isEmpty(pageContentOfTrade)){
            return plateInfos;
        }
        JSONObject jsonObject = JSON.parseObject(pageContentOfTrade);
        JSONObject data = jsonObject.getJSONObject("data");
        return plateInfos;
    }




}
