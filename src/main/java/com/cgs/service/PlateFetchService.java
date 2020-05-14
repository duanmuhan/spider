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
import org.springframework.util.CollectionUtils;
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
            log.info("start to fetch plate info of concept");
            List<PlateInfo> conceptPlate = fetchConceptPlate();
            log.info("concept result is :{}",JSON.toJSON(conceptPlate));
            log.info("start to fetch plate info of area");
            List<PlateInfo> areaPlate = fetchAreaPlate();
            log.info("area result is :{}",JSON.toJSON(areaPlate));
            log.info("start to fetch plate info of trade");
            List<PlateInfo> tradePlate = fetchTradePlate();
            log.info("trade result is :{}",JSON.toJSON(tradePlate));
            conceptPlate.addAll(areaPlate);
            conceptPlate.addAll(tradePlate);
            plateDAO.deleteAllPlateInfo();
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
            String requestPlateConceptUrl = plateConceptUrl.replace("pageno",String.valueOf(pageNo)).replace("pagesize",String.valueOf(pageSize)).replace("timestamp",String.valueOf(timestamp));
            String pageContentOfConcept = HttpRequestUtil.getRequestDirectly(requestPlateConceptUrl);
            if (StringUtils.isEmpty(pageContentOfConcept)){
                break;
            }
            log.info("trade plate is : {}", pageContentOfConcept);
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
            if (CollectionUtils.isEmpty(plateInfoDTOList)){
                break;
            }
            List<PlateInfo> plateInfoList = plateInfoDTOList.stream().map(e->e.convertToPlateInfo(PlateType.CONCEPT_CONSTANT)).collect(Collectors.toList());
            log.info("concept plate info list is :{}",plateInfoList);
            plateInfos.addAll(plateInfoList);
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
            String requestPlateAreaFetchUrl = plateAreaFetchUrl.replace("pageno",String.valueOf(pageNo)).replace("pagesize",String.valueOf(pageSize)).replace("timestamp",String.valueOf(timestamp));
            String pageContentOfArea = HttpRequestUtil.getRequestDirectly(requestPlateAreaFetchUrl);
            if (StringUtils.isEmpty(pageContentOfArea)){
                break;
            }
            log.info("trade plate is : {}", pageContentOfArea);
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
            if (CollectionUtils.isEmpty(plateInfoDTOList)){
                break;
            }
            List<PlateInfo> plateInfoList = plateInfoDTOList.stream().map(e->e.convertToPlateInfo(PlateType.AREA_CONSTANT)).collect(Collectors.toList());
            log.info("concept plate info list is :{}",plateInfoList);
            plateInfos.addAll(plateInfoList);
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
            String requestPlateTradeFetchUrl = plateTradeFetchUrl.replace("pageno",String.valueOf(pageNo)).replace("pagesize",String.valueOf(pageSize)).replace("timestamp",String.valueOf(timestamp));
            String pageContentOfTrade = HttpRequestUtil.getRequestDirectly(requestPlateTradeFetchUrl);
            if (StringUtils.isEmpty(pageContentOfTrade)){
                break;
            }
            log.info("trade plate is : {}", pageContentOfTrade);
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
            if (CollectionUtils.isEmpty(plateInfoDTOList)){
                break;
            }
            List<PlateInfo> plateInfoList = plateInfoDTOList.stream().map(e->e.convertToPlateInfo(PlateType.PLATE_CONSTANT)).collect(Collectors.toList());
            log.info("trade plate info list is :{}",plateInfoList);
            plateInfos.addAll(plateInfoList);
            pageNo = pageNo + 1;
            Thread.sleep(1000 * 10);
        }
        return plateInfos;
    }

}
