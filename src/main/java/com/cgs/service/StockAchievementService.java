package com.cgs.service;

import com.cgs.dao.StockAchievementDAO;
import com.cgs.entity.StockAchievement;
import com.cgs.util.HttpRequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class StockAchievementService {

    @Autowired
    private StockAchievementDAO stockAchievementDAO;

    public void fetchStockAchievement() throws InterruptedException {
        String url = "http://data.10jqka.com.cn/ajax/yjyg/date/2020-09-30/board/ALL/field/enddate/order/desc/page/pageNo/ajax/1/free/1/";
        int startIndex = 1;
        List<StockAchievement> stockAchievements = new ArrayList<>();
        while (true){
            String requestUrl = url.replace("pageNo",String.valueOf(startIndex));
            String content = HttpRequestUtil.getRequestDirectly(requestUrl);
            if (StringUtils.isEmpty(content)){
                break;
            }
            startIndex = startIndex + 1;
            Document document = Jsoup.parse(content);
            Elements trElements = document.getElementsByTag("tbody").first().getElementsByTag("tr");
            if (ObjectUtils.isEmpty(trElements)){
                break;
            }
            log.info("start to request startIndex : {}",startIndex);
            for (Element element : trElements){
                Elements tdElements = element.getElementsByTag("td");
                String stockId = tdElements.get(1).text();
                String stockName = tdElements.get(2).text();
                String achievementType = tdElements.get(3).text();
                String achievementTitle = tdElements.get(4).text();
                String profileChangeRate = tdElements.get(5).text();
                String profileLastYear = tdElements.get(6).text();
                String releaseDate = tdElements.get(7).text();

                StockAchievement stockAchievement = new StockAchievement();
                stockAchievement.setStockId(stockId);
                stockAchievement.setStockName(stockName);
                stockAchievement.setAchievementType(achievementType);
                stockAchievement.setAchievementTitle(achievementTitle);
                stockAchievement.setProfileChangeRate((StringUtils.isEmpty(profileChangeRate) || "-".equals(profileChangeRate))? 0:Double.valueOf(profileChangeRate));
                stockAchievement.setProfileLastYear(profileLastYear);
                stockAchievement.setReleaseDate(releaseDate);
                stockAchievements.add(stockAchievement);
            }
            Thread.sleep(1000);
        }
        if (!CollectionUtils.isEmpty(stockAchievements)){
            List<StockAchievement> stockAchievementList = stockAchievementDAO.batchQueryStockAchievementList();
            if (!CollectionUtils.isEmpty(stockAchievementList)){
                stockAchievements.removeAll(stockAchievementList);
            }
            if (CollectionUtils.isEmpty(stockAchievements)){
                stockAchievementDAO.batchInsertStockAchievement(stockAchievements);
            }
        }

    }
}
