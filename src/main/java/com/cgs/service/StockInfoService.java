package com.cgs.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cgs.dao.StockInfoDAO;
import com.cgs.dao.StockItemDAO;
import com.cgs.dto.ShStockInfoDTO;
import com.cgs.dto.SzStockInfoDTO;
import com.cgs.entity.StockInfo;
import com.cgs.entity.StockItem;
import com.cgs.util.HttpRequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StockInfoService {

    @Autowired
    private StockInfoDAO stockInfoDAO;
    @Autowired
    private StockItemDAO stockItemDAO;
    @Value("${stock.core.info.sh.url}")
    private String stockCoreInfoShUrl;
    @Value("${stock.core.info.sz.url}")
    private String stockCoreInfoSzUrl;
    @Value("${stock.core.info.refer.url}")
    private String referUrl;

    public void fetchStockInfoService() throws IOException, InterruptedException {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        List<StockItem> list = stockItemDAO.queryAllStockList();
        List<StockInfo> stockInfos = new ArrayList<>();
        if (CollectionUtils.isEmpty(list)){
            return;
        }
        List<StockItem> szStockList = list.stream().filter(e->{
            return  "sz".equals(e.getExchangeId());
        }).collect(Collectors.toList());

        List<StockItem> shStockList = list.stream().filter(e->{
            return  "sh".equals(e.getExchangeId());
        }).collect(Collectors.toList());


        if (!CollectionUtils.isEmpty(szStockList)){
            for (StockItem item : szStockList){
                String requestUrl = stockCoreInfoSzUrl.replace("seccode",item.getStockId());
                String result = HttpRequestUtil.getRequestDirectly(requestUrl);
                if (!StringUtils.isEmpty(result)){
                    JSONObject object = JSON.parseObject(result);
                    String stockInfo = object.getString("data");
                    if (StringUtils.isEmpty(stockInfo)){
                        continue;
                    }
                    List<SzStockInfoDTO> szStockInfoDTOS = JSON.parseArray(stockInfo,SzStockInfoDTO.class);
                    SzStockInfoDTO stockInfoDTO = szStockInfoDTOS.stream().filter(e->{
                        return e.isEmpty();
                    }).findAny().get();
                    StockInfo info = convertSzDtoToStockInfo(simpleDateFormat,stockInfoDTO,item.getStockId());
                    stockInfos.add(info);
                }
                Thread.sleep(1000);
            }
        }
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH) + 1;
        int year = calendar.get(Calendar.YEAR);
        String monthStr = String.valueOf(year).concat(String.valueOf(month));
        String date = simpleDateFormat.format(new Date());
        if (!CollectionUtils.isEmpty(shStockList)){
            for (StockItem item : shStockList){
                String requestUrl = stockCoreInfoShUrl.replace("fundid",item.getStockId())
                                                    .replace("month",monthStr)
                                                    .replace("inyear",String.valueOf(year))
                                                    .replace("searchdate",simpleDateFormat.format(new Date()))
                                                    .replace("currmills",String.valueOf(System.currentTimeMillis()));
                String referTargetUrl = referUrl.replace("companycode",item.getStockId());
                String result = HttpRequestUtil.getRequestWithRefer(requestUrl,referTargetUrl);
                if (StringUtils.isEmpty(result)){
                    continue;
                }
                JSONObject jsonObject = JSON.parseObject(result);
                List<ShStockInfoDTO> shStockInfoDTOList = JSON.parseArray(jsonObject.getString("result"),ShStockInfoDTO.class);
                ShStockInfoDTO shStockInfoDTO = shStockInfoDTOList.stream().filter(e->{
                    return StringUtils.isEmpty(e.getMaxHighPriceDate()) && date.equals(e.getMaxHighPriceDate());
                }).findAny().get();
                StockInfo stockInfo = convertShDtoToStockInfo(simpleDateFormat,shStockInfoDTO,item.getStockId());
                stockInfos.add(stockInfo);
            }
        }
        stockInfoDAO.batchInsertStockInfo(stockInfos);
    }

    private StockInfo convertSzDtoToStockInfo(SimpleDateFormat simpleDateFormat,SzStockInfoDTO szStockInfoDTO, String stockId){
        StockInfo stockInfo = new StockInfo();
        stockInfo.setStockId(stockId);
        stockInfo.setTotalVolume(Double.valueOf(szStockInfoDTO.getNow_cjje()));
        stockInfo.setTotalTransactionAmount(Double.valueOf(szStockInfoDTO.getNow_cjbs()));
        stockInfo.setPeRatio(Double.valueOf(szStockInfoDTO.getNow_syl()));
        stockInfo.setAverageTurnoverRate(Double.valueOf(szStockInfoDTO.getNow_hsl()));
        stockInfo.setTotalMarketValue(Double.valueOf(szStockInfoDTO.getNow_sjzz()));
        stockInfo.setFlowMarketValue(Double.valueOf(szStockInfoDTO.getNow_ltsz()));
        stockInfo.setTotalShareCapital(Double.valueOf(szStockInfoDTO.getNow_zgb()));
        stockInfo.setStockCirculationShareCapital(Double.valueOf(szStockInfoDTO.getNow_ltgb()));
        stockInfo.setDate(simpleDateFormat.format(new Date()));
        return stockInfo;
    }

    private StockInfo convertShDtoToStockInfo(SimpleDateFormat simpleDateFormat,ShStockInfoDTO shStockInfoDTO, String stockId){
        DecimalFormat df = new DecimalFormat("#.00");
        StockInfo stockInfo = new StockInfo();
        Double closeTrAmt = Double.valueOf(df.format(shStockInfoDTO.getCloseTrAmt()/10000));
        stockInfo.setTotalTransactionAmount(closeTrAmt);
        Double maxTrVol1 = Double.valueOf(df.format(Double.valueOf(shStockInfoDTO.getMaxTrVol1())/10000));
        stockInfo.setTotalVolume(maxTrVol1);
        Double totalMarketValue = Double.valueOf(df.format(shStockInfoDTO.getCloseMarketValue()/10000));
        stockInfo.setTotalMarketValue(totalMarketValue);

        Double flowMarketValue = Double.valueOf(df.format(shStockInfoDTO.getCloseNegoValue()/10000));
        stockInfo.setFlowMarketValue(flowMarketValue);
        stockInfo.setPeRatio(shStockInfoDTO.getCloseProfitRate());
        stockInfo.setAverageTurnoverRate(shStockInfoDTO.getTotalExchRate());
        stockInfo.setDate(simpleDateFormat.format(new Date()));
        stockInfo.setStockId(stockId);
        return stockInfo;
    }
}
