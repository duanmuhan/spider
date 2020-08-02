package com.cgs.service.info;

import com.cgs.dao.PlateDAO;
import com.cgs.dao.PolicyTableDAO;
import com.cgs.entity.PlateInfo;
import com.cgs.entity.PolicyInfo;
import com.cgs.util.HttpRequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SateCouncilService {

    @Value("${yaowen.guowuyuan.url}")
    private String yaowenUrl;

    private static String PREFIX = "http://www.gov.cn";

    private static String NAME = "国务院";

    private static ThreadLocal<SimpleDateFormat> tl = new ThreadLocal<>();

    private Queue<String> areaQueue = new ArrayDeque<>();

    private Queue<String> contentQueue = new ArrayDeque<>();

    @Autowired
    private PolicyTableDAO policyTableDAO;
    @Autowired
    private PlateDAO plateDAO;

    public void fetchStateCouncilService(){
        String content = HttpRequestUtil.getRequestDirectly(yaowenUrl);
        if (StringUtils.isEmpty(content)){
            return;
        }
        Document document = Jsoup.parse(content);
        Elements liElements = document.getElementsByTag("li");
        for (Element liElement : liElements){
            Element aElement = liElement.select("a").first();
            String url = aElement != null ? aElement.attr("href") : "";
            if (!url.contains(PREFIX)){
                url = PREFIX + url;
            }
            contentQueue.add(url);
        }
        List<PlateInfo> plateInfos = plateDAO.queryAllPlateInfo();
        List<PolicyInfo> policyInfos = new ArrayList<>();
        while (!contentQueue.isEmpty()){
            String url = contentQueue.poll();
            String pageContent = HttpRequestUtil.getRequestDirectly(url);
            Document pageDocument = Jsoup.parse(pageContent);
            Element titleElement = pageDocument.getElementsByTag("h1").first();
            Element contentElement = pageDocument.getElementById("UCAP-CONTENT");
            Element dateElement = pageDocument.getElementsByClass("pages-date").first();
            if (ObjectUtils.isEmpty(titleElement) || ObjectUtils.isEmpty(contentElement)){
                continue;
            }
            if (StringUtils.isEmpty(dateElement.text())){
                continue;
            }
            String date = dateElement.text().substring(0,10);
            SimpleDateFormat dateFormat = getSimpleDateFormat("yyyy-MM-dd");
            String currentDate = dateFormat.format(new Date());
            if (date.equals(currentDate)){
                String articleContent = contentElement.text();
                String title = titleElement.text();
                List<PlateInfo> matchedPlateInfo = isTextContainsPlateName(articleContent,plateInfos);
                if (!CollectionUtils.isEmpty(matchedPlateInfo)){
                    PolicyInfo policyInfo = new PolicyInfo();
                    policyInfo.setSource(url);
                    policyInfo.setTargetPlate(matchedPlateInfo.stream().map(e->e.getPlateName()).collect(Collectors.joining(",")));
                    policyInfo.setTargetPlateId(matchedPlateInfo.stream().map(e->e.getPlateId()).collect(Collectors.joining(",")));
                    policyInfo.setTitle(title);
                    policyInfo.setRelease_date(date);
                    policyInfo.setPlatform(NAME);
                    policyInfos.add(policyInfo);
                }
            }
        }
        if (!CollectionUtils.isEmpty(policyInfos)){
            policyTableDAO.batchInsertPolicyTable(policyInfos);
        }
    }

    private SimpleDateFormat getSimpleDateFormat(String datePattern){
        SimpleDateFormat sdf = tl.get();
        if (ObjectUtils.isEmpty(sdf)){
            sdf = new SimpleDateFormat(datePattern);
            tl.set(sdf);
        }
        return sdf;
    }

    private List<PlateInfo> isTextContainsPlateName(String text, List<PlateInfo> plateInfos){
        if (CollectionUtils.isEmpty(plateInfos) || StringUtils.isEmpty(text)){
            return null;
        }
        List<PlateInfo> resultList = new ArrayList<>();
        for (PlateInfo plateInfo : plateInfos){
            String plateName = plateInfo.getPlateName().replace("概念","");
            if (text.contains(plateName)){
                resultList.add(plateInfo);
            }
        }
        return resultList;
    }
}
