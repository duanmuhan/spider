package com.cgs.service.info;

import com.cgs.dao.PlateDAO;
import com.cgs.entity.PlateInfo;
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
import java.util.ArrayDeque;
import java.util.Date;
import java.util.List;
import java.util.Queue;
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

    private static ThreadLocal<SimpleDateFormat> tl = new ThreadLocal<>();

    private Queue<String> areaQueue = new ArrayDeque<>();

    private Queue<String> contentQueue = new ArrayDeque<>();

    @Autowired
    private PlateDAO plateDAO;


    public void fetchIndustryService() throws UnsupportedEncodingException {
        String content = HttpRequestUtil.getRequestDirectly(requestUrl);
        if (StringUtils.isEmpty(content)){
            return;
        }
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
                if (!ObjectUtils.isEmpty(element)){
                    String text = element.text();
                    if (!StringUtils.isEmpty(text)){
                        String result = isTextContainsPlateName(text,plateInfos);
                    }
                }
            }catch (Exception e){
                log.error("error is :{}",e);
            }
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
                    if (!currentDate.equals(date)){
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

    private String isTextContainsPlateName(String text,List<PlateInfo> plateInfos){
        if (CollectionUtils.isEmpty(plateInfos) || StringUtils.isEmpty(text)){
            return "";
        }
        StringBuilder sb = new StringBuilder();
        List<String> plateNames = plateInfos.stream().map(e->e.getPlateName()).collect(Collectors.toList());
        for (String plateName : plateNames){
            plateName.replace("概念","");
            if (text.contains(plateName)){
                sb.append(plateName);
                sb.append(";");
            }
        }
        System.out.println(sb.toString());
        return sb.toString();
    }
}
