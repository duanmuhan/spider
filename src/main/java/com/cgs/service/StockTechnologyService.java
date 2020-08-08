package com.cgs.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cgs.dao.StockItemDAO;
import com.cgs.dao.StockTechnologyDAO;
import com.cgs.dto.StockTechnologyDTO;
import com.cgs.entity.StockItem;
import com.cgs.entity.StockTechnology;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StockTechnologyService {

    @Value("${stock.technology.url}")
    private String requestUrl;

    @Autowired
    private StockItemDAO stockItemDAO;
    @Autowired
    private StockTechnologyDAO stockTechnologyDAO;

    private static ThreadLocal<SimpleDateFormat> tl = new ThreadLocal<>();

    public void fetchStockTechnologyInfo() throws InterruptedException {
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
        String releaseDate = date.replaceAll("-","");
        List<StockTechnology> stockTechnologies = new ArrayList<>();
        for (StockItem stockItem : stockItemList){
            log.info("fetch stock id ：{}",stockItem.getStockId());
            String url = requestUrl.replace("stockId",stockItem.getStockId());
            webDriver.get(url);
            String content = webDriver.getPageSource();
            if (StringUtils.isEmpty(content)){
                continue;
            }
            Document document = Jsoup.parse(content);
            String text = document.text();
            if (StringUtils.isEmpty(text)){
                continue;
            }
            JSONObject object = JSON.parseObject(text).getJSONObject("data").getJSONObject("data").getJSONObject("result");
            if (ObjectUtils.isEmpty(object)){
                continue;
            }
            object.remove("created");
            Map<String,Map<String, StockTechnologyDTO>> map = (Map<String, Map<String, StockTechnologyDTO>>) JSON.parse(object.toJSONString());
            for (Map.Entry<String,Map<String,StockTechnologyDTO>> entry : map.entrySet()){
                Map<String,StockTechnologyDTO> entryMap = entry.getValue();
                for (Map.Entry<String,StockTechnologyDTO> entryMapEntry : entryMap.entrySet()){
                    String json = JSON.toJSONString(entryMapEntry.getValue());
                    StockTechnologyDTO dto = JSON.parseObject(json,StockTechnologyDTO.class);
                    StockTechnology stockTechnology = new StockTechnology();
                    stockTechnology.setType(entry.getKey());
                    stockTechnology.setStockId(stockItem.getStockId());
                    stockTechnology.setSpecial(dto.getSpecial());
                    stockTechnology.setQuery(dto.getQuery());
                    List<String> list = dto.getTag();
                    if (!CollectionUtils.isEmpty(list)){
                        String tag = list.stream().collect(Collectors.joining(","));
                        stockTechnology.setTag(tag);
                    }
                    stockTechnology.setDescStr(dto.getDesc());
                    stockTechnology.setReleaseDate(releaseDate);
                    stockTechnologies.add(stockTechnology);
                }
            }
            if (!CollectionUtils.isEmpty(stockTechnologies)){
                stockTechnologyDAO.batchInsertStockTechnology(stockTechnologies);
            }
            Thread.sleep(1000);
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
