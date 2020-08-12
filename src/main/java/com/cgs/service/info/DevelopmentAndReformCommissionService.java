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

/*
 * 发改委信息爬取
 * */
@Service
@Slf4j
public class DevelopmentAndReformCommissionService {

    @Value("${notice.development.reform.url}")
    private String requestUrl;

    private static String PREFIX = "https://www.ndrc.gov.cn/xwdt";

    private static ThreadLocal<SimpleDateFormat> tl = new ThreadLocal<>();

    @Autowired
    private PolicyTableDAO policyTableDAO;
    @Autowired
    private PlateDAO plateDAO;

    private static String NAME = "中华人民共和国国家发展改革委员会";

    private Queue<String> areaQueue = new ArrayDeque<>();

    private Queue<String> contentQueue = new ArrayDeque<>();

    public void fetchDevelopmentReformService(){
        String content = HttpRequestUtil.getRequestDirectly(requestUrl);
        if (StringUtils.isEmpty(content)){
            return;
        }
        Document document = Jsoup.parse(content);
        Element areaElement = document.getElementsByClass("news-left").first();
        Elements newsElements = areaElement.getElementsByClass("news");
        Element dongTaiElement = areaElement.getElementsByClass("dongtai").first();
        for (Element element : newsElements){
            Element mtElement = element.getElementsByClass("mt").select("span").first();
            Element el = mtElement.select("a").first();
            String liUrl = el != null ? el.attr("href") : "";
            if (StringUtils.isEmpty(liUrl)){
                continue;
            }
            liUrl = PREFIX + liUrl.replace(".","");
            areaQueue.add(liUrl);
        }
        Elements dongtaiElements = dongTaiElement.getElementsByTag("ul").first().getElementsByTag("li");
        for (Element element : dongtaiElements){
            Element el = element.select("a").first();
            String liUrl = el != null ? el.attr("href") : "";
            liUrl = PREFIX + liUrl.replace(".","");
            areaQueue.add(liUrl);
        }

        Map<String,String> titleMap = new HashMap<>();

        while (!areaQueue.isEmpty()){
            String url = areaQueue.poll();
            String webContent = HttpRequestUtil.getRequestDirectly(url);
            if (StringUtils.isEmpty(webContent)){
                continue;
            }
            Document newsListDocument = Jsoup.parse(webContent);
            Element u_listElement = newsListDocument.getElementsByClass("u-list").first();
            Elements elements = u_listElement.getElementsByTag("li");
            for (Element element : elements){
                Element el = element.select("a").first();
                if (ObjectUtils.isEmpty(el)){
                    continue;
                }
                String liUrl = el != null ? el.attr("href") : "";
                String title = el.text();
                if (StringUtils.isEmpty(liUrl) || StringUtils.isEmpty(title)){
                    continue;
                }
                liUrl = url + liUrl.replace("./","");
                titleMap.put(liUrl,title);
                contentQueue.add(liUrl);
            }
        }
        List<PlateInfo> plateInfos = plateDAO.queryAllPlateInfo();
        List<PolicyInfo> policyInfos = new ArrayList<>();
        while (!contentQueue.isEmpty()){
            try {
                String url = contentQueue.poll();
                String webContentDetail = HttpRequestUtil.getRequestDirectly(url);
                if (StringUtils.isEmpty(webContentDetail)){
                    continue;
                }
                Document contentDocument = Jsoup.parse(webContentDetail);
                if (ObjectUtils.isEmpty(contentDocument)){
                    continue;
                }
                Element dateElement = contentDocument.getElementsByClass("time").first();
                Element contentElement = contentDocument.getElementsByClass("TRS_Editor").first();
                if (ObjectUtils.isEmpty(contentElement)){
                    continue;
                }
                String contentDetail = contentElement.text();
                String date = dateElement.text();
                if (StringUtils.isEmpty(contentDetail) || StringUtils.isEmpty(date)){
                    continue;
                }
                String finalDate = date.replaceAll("发布时间：","").replaceAll("/","-");
                SimpleDateFormat dateFormat = getSimpleDateFormat("yyyy-MM-dd");
                String currentDate = dateFormat.format(new Date());
                if ( currentDate.equals(finalDate) && !CollectionUtils.isEmpty(plateInfos)){
                    List<PlateInfo> matchedPlateInfo = isTextContainsPlateName(contentDetail,plateInfos);
                    if (!CollectionUtils.isEmpty(matchedPlateInfo)){
                        PolicyInfo policyInfo = new PolicyInfo();
                        policyInfo.setSource(url);
                        policyInfo.setTargetPlate(matchedPlateInfo.stream().map(e->e.getPlateName()).collect(Collectors.joining(",")));
                        policyInfo.setTargetPlateId(matchedPlateInfo.stream().map(e->e.getPlateId()).collect(Collectors.joining(",")));
                        policyInfo.setTitle(titleMap.get(url));
                        policyInfo.setRelease_date(finalDate);
                        policyInfo.setPlatform(NAME);
                        policyInfos.add(policyInfo);
                    }
                }
            }catch (Exception e){
                log.error("fetchDevelopmentReformService error:{}",e);
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
