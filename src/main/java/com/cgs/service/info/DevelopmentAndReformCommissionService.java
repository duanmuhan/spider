package com.cgs.service.info;

import com.cgs.dao.PlateDAO;
import com.cgs.dao.PolicyTableDAO;
import com.cgs.util.HttpRequestUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.Queue;

/*
 * 发改委信息爬取
 * */
@Service
public class DevelopmentAndReformCommissionService {

    @Value("${notice.development.reform.url}")
    private String requestUrl;

    private static String PREFIX = "https://www.ndrc.gov.cn";

    private static ThreadLocal<SimpleDateFormat> tl = new ThreadLocal<>();

    @Autowired
    private PolicyTableDAO policyTableDAO;
    @Autowired
    private PlateDAO plateDAO;

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

    }

    private SimpleDateFormat getSimpleDateFormat(String datePattern){
        SimpleDateFormat sdf = tl.get();
        if (ObjectUtils.isEmpty(sdf)){
            sdf = new SimpleDateFormat(datePattern);
            tl.set(sdf);
        }
        return sdf;
    }
}
