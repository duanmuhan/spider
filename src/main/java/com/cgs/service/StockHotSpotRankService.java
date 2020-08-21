package com.cgs.service;

import com.cgs.dao.StockHotSpotDAO;
import com.cgs.dao.StockItemDAO;
import com.cgs.entity.StockHotspot;
import com.cgs.entity.StockItem;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class StockHotSpotRankService {

    @Autowired
    private StockHotSpotDAO stockHotSpotDAO;
    @Autowired
    private StockItemDAO stockItemDAO;

    @Value("${stock.hotspot.url}")
    private String requestUrl;

    private static ThreadLocal<SimpleDateFormat> tl = new ThreadLocal<>();

    public void fetchStockHotspotRand() throws Exception{
        System.setProperty("webdriver.chrome.driver","C:Program Files (x86)//Google//Chrome//Application//chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        WebDriver webDriver = new ChromeDriver(options);
        webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

        List<StockItem> stockItemList = stockItemDAO.queryAllStockList();
        if (CollectionUtils.isEmpty(stockItemList)){
            return;
        }
        SimpleDateFormat simpleDateFormat = getSimpleDateFormat("yyyy-MM-dd");
        String date = simpleDateFormat.format(new Date());
        List<StockHotspot> stockHotspotList = new ArrayList<>();
        for (StockItem stockItem : stockItemList){
            try{
                String url = requestUrl.replaceAll("stockId",stockItem.getStockId());
                webDriver.get(url);
                String content = webDriver.getPageSource();
                log.info("fetch stock item id :{}",stockItem.getStockId());
                if (StringUtils.isEmpty(content)){
                    continue;
                }
                Document document = Jsoup.parse(content);
                Element spanElement = document.getElementById("popularity_rank").getElementsByTag("span").first();
                String rank = spanElement.text();
                if (StringUtils.isEmpty(rank)){
                    continue;
                }
                StockHotspot stockHotspot = new StockHotspot();
                stockHotspot.setStockId(stockItem.getStockId());
                stockHotspot.setStockName(stockItem.getName());
                stockHotspot.setRank(Integer.valueOf(rank));
                stockHotspot.setReleaseDate(date);
                stockHotspotList.add(stockHotspot);
            }catch (Exception e){
                log.error("exception is :{}",e);
            }
            Thread.sleep(500);
        }

        if (!CollectionUtils.isEmpty(stockHotspotList)){
            stockHotSpotDAO.batchInsertStockHotspotItem(stockHotspotList);
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
