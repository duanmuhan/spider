package com.cgs.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cgs.constant.PlateType;
import com.cgs.dao.PlateDAO;
import com.cgs.dto.PlateInfoDTO;
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
import java.util.stream.Collectors;

/*
* 板块信息的获取
* */
@Service
@Log4j2
public class PlateFetchService {

    @Autowired
    private PlateDAO plateDAO;

    @Value("${plate.concept.url}")
    private String plateConceptUrl;
    @Value("${plate.area.url}")
    private String plateAreaFetchUrl;
    @Value("${plate.industry.url}")
    private String plateTradeFetchUrl;

    public void fetchPlateInfo() {
        if (StringUtils.isEmpty(plateConceptUrl) || StringUtils.isEmpty(plateAreaFetchUrl) || StringUtils.isEmpty(plateTradeFetchUrl)){
            return;
        }
        try {
            List<PlateInfo> conceptPlate = fetchConceptPlate();
            List<PlateInfo> areaPlate = fetchAreaPlate();
            List<PlateInfo> tradePlate = fetchTradePlate();
            conceptPlate.addAll(areaPlate);
            conceptPlate.addAll(tradePlate);
            plateDAO.batchInsertPlateInfo(conceptPlate);
        }catch (Exception e){
            log.error("fetch plate info exception :{}",e);
        }
    }

    private List<PlateInfo> fetchConceptPlate() throws IOException, InterruptedException {
        List<PlateInfo> plateInfos = new ArrayList<>();
        int pageNo = 1;
        while(true){
            int pageSize = 50;
            long timestamp = System.currentTimeMillis();
            plateConceptUrl = plateConceptUrl.replace("pageno",String.valueOf(pageNo)).replace("pagesize",String.valueOf(pageSize)).replace("timestamp",String.valueOf(timestamp));
            String pageContentOfConcept = HttpRequestUtil.getRequestDirectly(plateConceptUrl);
            if (StringUtils.isEmpty(pageContentOfConcept)){
                break;
            }
            JSONObject object = JSON.parseObject(pageContentOfConcept);
            JSONObject data = object.getJSONObject("data");
            if (ObjectUtils.isEmpty(data)){
                break;
            }
            String plateData = data.getString("diff");
            if (StringUtils.isEmpty(plateData)){
                break;
            }
            List<PlateInfoDTO> plateInfoDTOList = JSON.parseArray(plateData,PlateInfoDTO.class);
            plateInfoDTOList.stream().map(e->e.convertToPlateInfo(PlateType.CONCEPT_CONSTANT)).collect(Collectors.toList());
            pageNo = pageNo + 1;
            Thread.sleep(1000 * 10);
        }
        return plateInfos;
    }

    private List<PlateInfo> fetchAreaPlate() throws IOException, InterruptedException {
        List<PlateInfo> plateInfos = new ArrayList<>();
        int pageNo = 1;
        while (true){
            int pageSize = 50;
            long timestamp = System.currentTimeMillis();
            plateAreaFetchUrl = plateAreaFetchUrl.replace("pageno",String.valueOf(pageNo)).replace("pagesize",String.valueOf(pageSize)).replace("timestamp",String.valueOf(timestamp));
            String pageContentOfArea = HttpRequestUtil.getRequest(plateAreaFetchUrl);
            if (StringUtils.isEmpty(pageContentOfArea)){
                break;
            }
            JSONObject object = JSON.parseObject(pageContentOfArea);
            JSONObject data = object.getJSONObject("data");
            if (ObjectUtils.isEmpty(data)){
                break;
            }
            String plateData = data.getString("diff");
            if (StringUtils.isEmpty(plateData)){
                break;
            }
            List<PlateInfoDTO> plateInfoDTOList = JSON.parseArray(plateData,PlateInfoDTO.class);
            plateInfoDTOList.stream().map(e->e.convertToPlateInfo(PlateType.AREA_CONSTANT)).collect(Collectors.toList());
            pageNo = pageNo + 1;
            Thread.sleep(1000 * 10);
        }
        return plateInfos;
    }

    private List<PlateInfo> fetchTradePlate() throws IOException, InterruptedException {
        List<PlateInfo> plateInfos = new ArrayList<>();
        int pageNo = 1;
        while (true){
            int pageSize = 50;
            long timestamp = System.currentTimeMillis();
            plateTradeFetchUrl = plateTradeFetchUrl.replace("pageno",String.valueOf(pageNo)).replace("pagesize",String.valueOf(pageSize)).replace("timestamp",String.valueOf(timestamp));
            String pageContentOfTrade = HttpRequestUtil.getRequest(plateTradeFetchUrl);
            if (StringUtils.isEmpty(pageContentOfTrade)){
                break;
            }
            JSONObject jsonObject = JSON.parseObject(pageContentOfTrade);
            JSONObject data = jsonObject.getJSONObject("data");
            if (ObjectUtils.isEmpty(data)){
                break;
            }
            String plateData = data.getString("diff");
            if (StringUtils.isEmpty(plateData)){
                break;
            }
            List<PlateInfoDTO> plateInfoDTOList = JSON.parseArray(plateData,PlateInfoDTO.class);
            plateInfoDTOList.stream().map(e->e.convertToPlateInfo(PlateType.PLATE_CONSTANT)).collect(Collectors.toList());
            pageNo = pageNo + 1;
            Thread.sleep(1000 * 10);
        }
        return plateInfos;
    }

}
