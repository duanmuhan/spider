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

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/*
* 工信部信息爬取
* */
@Service
@Slf4j
public class IndustryAndInformationTechnologyService {

    @Value("${notice.industry.url}")
    private String requestUrl;

    private static String PREFIX = "http://www.miit.gov.cn";

    private static String NAME = "中华人民共和国工业和信息化部";

    private static ThreadLocal<SimpleDateFormat> tl = new ThreadLocal<>();

    private Queue<String> areaQueue = new ArrayDeque<>();

    private Queue<String> contentQueue = new ArrayDeque<>();

    @Autowired
    private PolicyTableDAO policyTableDAO;
    @Autowired
    private PlateDAO plateDAO;



    public void fetchIndustryService() {
        String content = HttpRequestUtil.getRequestDirectly(requestUrl);
        if (StringUtils.isEmpty(content)){
            return;
        }
        List<PolicyInfo> policyInfos = new ArrayList<>();
        Document document = Jsoup.parse(content);
        Elements dtElements = document.getElementsByTag("dt");
        for (Element dtElement : dtElements){
            Element aElement = dtElement.select("a").first();
            String url = aElement != null ? aElement.attr("href") : "";
            url = PREFIX + url.replace("../..","");
            areaQueue.add(url);
        }
        while (!areaQueue.isEmpty()){
            String url = areaQueue.poll();
            if (StringUtils.isEmpty(url)){
                continue;
            }
            fetchIndustryServiceArea(url);
        }
        List<PlateInfo> plateInfos = plateDAO.queryAllPlateInfo();
        while (!contentQueue.isEmpty()){
            try {
                String contentUrl = contentQueue.poll();
                String pageContent = HttpRequestUtil.getRequestDirectly(contentUrl);
                if (StringUtils.isEmpty(pageContent)){
                    continue;
                }
                Document contentDocument = Jsoup.parse(pageContent);
                Element element = contentDocument.getElementById("con_con");
                Element titleElement = contentDocument.getElementById("con_title");
                Element releaseDate = contentDocument.getElementById("con_time");
                if (!ObjectUtils.isEmpty(element)){
                    String text = element.text();
                    if (!StringUtils.isEmpty(text)){
                       List<PlateInfo> matchedPlateInfo = isTextContainsPlateName(text,plateInfos);
                       if (!CollectionUtils.isEmpty(plateInfos)){
                         PolicyInfo policyInfo = new PolicyInfo();
                         policyInfo.setRelease_date(titleElement.text());
                         policyInfo.setSource(contentUrl);
                         policyInfo.setTargetPlate(matchedPlateInfo.stream().map(e->e.getPlateName()).collect(Collectors.joining(",")));
                         policyInfo.setTargetPlateId(matchedPlateInfo.stream().map(e->e.getPlateId()).collect(Collectors.joining(",")));
                         policyInfo.setTitle(titleElement.text());
                         policyInfo.setRelease_date(releaseDate.text());
                         policyInfo.setPlatform(NAME);
                         policyInfos.add(policyInfo);
                       }
                    }
                }
            }catch (Exception e){
                log.error("error is :{}",e);
            }
        }
        if (!CollectionUtils.isEmpty(policyInfos)){
            policyTableDAO.batchInsertPolicyTable(policyInfos);
        }
    }

    private void fetchIndustryServiceArea(String url){
        String content = HttpRequestUtil.getRequestDirectly(url);
        if (StringUtils.isEmpty(content)){
            return;
        }
        Document document = Jsoup.parse(content);
        Elements clist_con = document.getElementsByClass("clist_con");
        if (ObjectUtils.isEmpty(clist_con)){
            return;
        }
        for (Element element : clist_con){
            Elements elements = element.getElementsByTag("ul");
            for (Element ulElement : elements){
                Elements liElements = ulElement.getElementsByTag("li");
                for (Element liElement : liElements){
                    Element el = liElement.select("a").first();
                    String liUrl = el != null ? el.attr("href") : "";
                    if (liUrl.contains("www")){
                        continue;
                    }
                    liUrl = PREFIX + liUrl.replace("../..","");
                    String date = liElement.select("span").first().text();
                    SimpleDateFormat dateFormat = getSimpleDateFormat("yyyy-MM-dd");
                    String currentDate = dateFormat.format(new Date());
                    if (currentDate.equals(date)){
                        contentQueue.add(liUrl);
                    }
                }
            }
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

    private List<PlateInfo> isTextContainsPlateName(String text,List<PlateInfo> plateInfos){
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
