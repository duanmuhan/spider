package com.cgs.service.info;

import com.cgs.util.HttpRequestUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

@Service
public class IndustryAndInformationTechnologyService {

    @Value("${notice.industry.url}")
    private String requestUrl;

    public void fetchIndustryService(){
        String content = HttpRequestUtil.getRequestDirectly(requestUrl);
        if (StringUtils.isEmpty(content)){
            return;
        }
        Document document = Jsoup.parse(content);
        Elements clist_con = document.getElementsByClass("clist_con");
        if (ObjectUtils.isEmpty(clist_con)){
            return;
        }
        Elements dt = document.getElementsByTag("<dt>");
        for (Element element : clist_con){
            Elements elements = clist_con.select("");
        }

    }
}
