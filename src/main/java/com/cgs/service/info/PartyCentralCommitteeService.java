package com.cgs.service.info;

import com.cgs.dao.PlateDAO;
import com.cgs.dao.PolicyTableDAO;
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
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

@Service
@Slf4j
public class PartyCentralCommitteeService {

    @Value("${party.center.comitee.url}")
    private String requestUrl;

    private static String NAME = "党中央";

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
    }
}
