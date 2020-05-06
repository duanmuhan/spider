package com.cgs.service;

import com.cgs.dao.PlateDAO;
import com.cgs.util.HttpRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;

@Service
public class PlateFetchService {

    @Autowired
    private PlateDAO plateDAO;

    @Value("plate.concept.fetch.url")
    private String plateConceptUrl;
    @Value("plate.concept.area.url")
    private String plateAreaFetchUrl;
    private String plateTradeFetchUrl;
    public void fetchPlateInfo() throws IOException {
        if (StringUtils.isEmpty(plateConceptUrl) || StringUtils.isEmpty(plateAreaFetchUrl) || StringUtils.isEmpty(plateTradeFetchUrl)){
            return;
        }
        String pageContentOfConcept = HttpRequestUtil.getRequest(plateConceptUrl);

    }
}
