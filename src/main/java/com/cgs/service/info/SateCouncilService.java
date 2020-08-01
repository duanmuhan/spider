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
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.Queue;

@Service
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

        }
    }
}
