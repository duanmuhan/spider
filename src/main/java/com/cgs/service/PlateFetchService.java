package com.cgs.service;

import com.cgs.dao.PlateDAO;
import com.cgs.entity.PlateInfo;
import com.cgs.util.HttpRequestUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
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
        String pageContentOfConcept = HttpRequestUtil.getRequest(plateConceptUrl);
        if (StringUtils.isEmpty(pageContentOfConcept)){
            return;
        }
        String plateAreaFetchContent = HttpRequestUtil.getRequest(plateAreaFetchUrl);
        if (StringUtils.isEmpty(plateAreaFetchContent)){
            return;
        }
        String  plateTradeFetchContent = HttpRequestUtil.getRequest(plateTradeFetchUrl);
        if (StringUtils.isEmpty(plateTradeFetchContent)){
            return;
        }
        List<PlateInfo> plateInfoList = new ArrayList<>();
        plateDAO.batchInsertPlateInfo(plateInfoList);
    }
}
