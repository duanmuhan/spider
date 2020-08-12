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
public class PartyCentralCommitteeService {

    @Value("${party.center.comitee.url}")
    private String requestUrl;

    private static String NAME = "中国共产党新闻网";

    private static String PREFIX = "http://cpc.people.com.cn/";

    private static ThreadLocal<SimpleDateFormat> tl = new ThreadLocal<>();

    private Queue<String> areaQueue = new ArrayDeque<>();

    private Queue<String> contentQueue = new ArrayDeque<>();

    @Autowired
    private PolicyTableDAO policyTableDAO;
    @Autowired
    private PlateDAO plateDAO;

    public void fetchPartyCentralCommitee(){
        String content = HttpRequestUtil.getRequestDirectlyWithEncoding(requestUrl,"GB2312");
        if (StringUtils.isEmpty(content)){
            return;
        }
        List<PolicyInfo> policyInfos = new ArrayList<>();
        Document document = Jsoup.parse(content);
        Elements h3Elements = document.getElementsByTag("h3");
        String target="高层动态,要闻,部委信息";
        for (Element h3Element : h3Elements){
            Element spanElement = h3Element.getElementsByTag("span").first();
            if (ObjectUtils.isEmpty(spanElement)){
                continue;
            }
            String text = spanElement.text();
            if (!target.contains(text)){
                continue;
            }
            Element aElement = h3Element.select("a").first();
            String url = aElement != null ? aElement.attr("href") : "";
            areaQueue.add(url);
        }

        while (!areaQueue.isEmpty()){
            String url = areaQueue.poll();
            String articleContent = HttpRequestUtil.getRequestDirectlyWithEncoding(url,"GB2312");
            Document articleDocument = Jsoup.parse(articleContent);
            Elements liElements = articleDocument.getElementsByTag("li");
            for (Element liElement : liElements){
                Element aElement = liElement.select("a").first();
                if (ObjectUtils.isEmpty(aElement)){
                    continue;
                }
                String contentUrl = aElement != null ? aElement.attr("href") : "";
                contentUrl = PREFIX + contentUrl;
                contentQueue.add(contentUrl);
            }
        }

        List<PlateInfo> plateInfos = plateDAO.queryAllPlateInfo();

        while (!contentQueue.isEmpty()){
            String url = contentQueue.poll();
            try {
                String articleContent = HttpRequestUtil.getRequestDirectlyWithEncoding(url,"GB2312");
                Document articleDocument = Jsoup.parse(articleContent);
                Element titleElement = articleDocument.getElementsByTag("h1").first();
                Element contentElement = articleDocument.getElementsByClass("show_text").first();
                Element dateElement = articleDocument.getElementsByClass("sou").first();
                if (ObjectUtils.isEmpty(titleElement) || ObjectUtils.isEmpty(contentElement) || ObjectUtils.isEmpty(dateElement)){
                    continue;
                }
                String date = dateElement.text();
                if (StringUtils.isEmpty(date)){
                    continue;
                }
                date = date.substring(0,10).replace("年","-").replace("月","-");
                SimpleDateFormat dateFormat = getSimpleDateFormat("yyyy-MM-dd");
                String currentDate = dateFormat.format(new Date());
                if (date.equals(currentDate)){
                    String text = contentElement.text();
                    List<PlateInfo> matchedPlateInfo = isTextContainsPlateName(text,plateInfos);
                    if (!CollectionUtils.isEmpty(plateInfos)){
                        PolicyInfo policyInfo = new PolicyInfo();
                        policyInfo.setRelease_date(titleElement.text());
                        policyInfo.setSource(url);
                        policyInfo.setTargetPlate(matchedPlateInfo.stream().map(e->e.getPlateName()).collect(Collectors.joining(",")));
                        policyInfo.setTargetPlateId(matchedPlateInfo.stream().map(e->e.getPlateId()).collect(Collectors.joining(",")));
                        policyInfo.setTitle(titleElement.text());
                        policyInfo.setRelease_date(date);
                        policyInfo.setPlatform(NAME);
                        policyInfos.add(policyInfo);
                    }
                }
            }catch (Exception e){
                log.error("fetchPartyCentralCommitee error:{}",e);
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
