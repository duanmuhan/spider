package com.cgs.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cgs.dao.StockHolderComponentDAO;
import com.cgs.dao.StockHolderDAO;
import com.cgs.dao.StockHolderTopTenDAO;
import com.cgs.dao.StockItemDAO;
import com.cgs.dto.StockHolderComponentDTO;
import com.cgs.dto.StockHolderDTO;
import com.cgs.dto.StockHolderTopTenDTO;
import com.cgs.dto.StockHolderTopTenOutDTO;
import com.cgs.entity.StockHolder;
import com.cgs.entity.StockHolderComponent;
import com.cgs.entity.StockHolderTopTen;
import com.cgs.entity.StockItem;
import com.cgs.util.HttpRequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class StockHolderInfoFetchService {

    @Autowired
    private StockHolderDAO stockHolderDAO;
    @Autowired
    private StockHolderTopTenDAO stockHolderTopTenDAO;
    @Autowired
    private StockHolderComponentDAO stockHolderComponentDAO;

    @Autowired
    private StockItemDAO stockItemDAO;

    @Value("${stock.holder.url}")
    private String stockHolderUrl;

    public void fetchStockHolderInfo() throws Exception{
        List<StockItem> stockItemList = stockItemDAO.queryAllStockList();
        if (CollectionUtils.isEmpty(stockItemList)){
            return;
        }
        stockItemList.stream().forEach(e->{
            String stockId = e.getStockId();
            String requestUrl = stockHolderUrl.concat(e.getExchangeId().toUpperCase() + e.getStockId());
            log.info("start to request url:{}",requestUrl);
            String result = HttpRequestUtil.getRequestDirectly(requestUrl);
            if (!StringUtils.isEmpty(result)){
                JSONObject object = JSON.parseObject(result);
                String stockHolderStr = object.getString("gdrs");
                if (!StringUtils.isEmpty(stockHolderStr)){
                    List<StockHolderDTO> stockHolderDTOS = JSON.parseArray(stockHolderStr,StockHolderDTO.class);
                    if (!CollectionUtils.isEmpty(stockHolderDTOS)){
                        List<StockHolder> tmpList = stockHolderDTOS.stream().map(f->{
                            StockHolder stockHolder = new StockHolder();
                            stockHolder.setStockId(stockId);
                            stockHolder.setNumberOfShareholders(f.getGdrs());
                            stockHolder.setPerCapitaTradableShares(f.getRjltg());
                            stockHolder.setLastChange(f.getRjltg_jsqbh());
                            stockHolder.setStockConvergenceRate(f.getCmjzd());
                            stockHolder.setPrice(f.getGj());
                            stockHolder.setPerCapitaHoldingAmount(f.getRjcgje());
                            stockHolder.setTopTenStockHolder(f.getQsdltgdcghj());
                            stockHolder.setTopTenStockFlowHolder(f.getQsdltgdcghj());
                            stockHolder.setReleaseDate(f.getRq());
                            return stockHolder;
                        }).collect(Collectors.toList());
                        try {
                            stockHolderDAO.batchInsertStockHolder(tmpList);
                        }catch (Exception exception){
                            log.error( "exception while excute batchInsertStockHolder " +JSONObject.toJSONString(tmpList) +  stockHolderStr + " :{}",exception);
                        }
                    }
                }
                String stockHolderTopTenStr = object.getString("sdltgd");
                if (!StringUtils.isEmpty(stockHolderTopTenStr)){
                    List<StockHolderTopTenOutDTO> outDTOList = JSON.parseArray(stockHolderTopTenStr, StockHolderTopTenOutDTO.class);
                    for (StockHolderTopTenOutDTO dto : outDTOList){
                        List<StockHolderTopTenDTO> dtos = dto.getSdltgd();
                        if (!CollectionUtils.isEmpty(dtos)){
                            List<StockHolderTopTen> list =  dtos.stream().map(h->{
                                StockHolderTopTen stockHolderTopTen = new StockHolderTopTen();
                                stockHolderTopTen.setStockId(stockId);
                                stockHolderTopTen.setStockHolderName(h.getGdmc());
                                stockHolderTopTen.setTypesOfStockHolders(h.getGflx());
                                stockHolderTopTen.setTypesOfShares(h.getGflx());
                                stockHolderTopTen.setNumbersOfShares(h.getCgs());
                                stockHolderTopTen.setRate(h.getZltgbcgbl());
                                stockHolderTopTen.setChanges(h.getZj());
                                stockHolderTopTen.setChangeRate(h.getBdbl());
                                stockHolderTopTen.setReleaseDate(h.getRq());
                                return stockHolderTopTen;
                            }).collect(Collectors.toList());
                            try {
                                stockHolderTopTenDAO.batchInsertStockHolderTopTen(list);
                            }catch (Exception exception){
                                log.error( "exception while excute batchInsertStockHolderTopTen " +JSONObject.toJSONString(list) + " " + stockHolderTopTenStr + " :{}",exception);
                            }
                        }
                    }
                }
                String stockTopStr = object.getString("zlcc");
                if (!StringUtils.isEmpty(stockTopStr)){
                    List<StockHolderComponentDTO> tmpList = JSON.parseArray(stockTopStr, StockHolderComponentDTO.class);
                    if (!CollectionUtils.isEmpty(tmpList)){
                        List<StockHolderComponent> list = tmpList.stream().map(i->{
                            StockHolderComponent stockHolderComponent = new StockHolderComponent();
                            stockHolderComponent.setStockId(stockId);
                            stockHolderComponent.setOrganizationType(i.getJglx());
                            stockHolderComponent.setPositions(i.getCcjs());
                            stockHolderComponent.setNumberOfSharesHeld(i.getCcgs());
                            stockHolderComponent.setProportionOfTradableShares(i.getZltgbl());
                            stockHolderComponent.setProportionInTotalEquity(i.getZltgbbl());
                            stockHolderComponent.setReleaseDate(e.getListingDate());
                            return stockHolderComponent;
                        }).collect(Collectors.toList());
                        try {
                            stockHolderComponentDAO.batchInsertStockHolderComponent(list);
                        }catch (Exception exception){
                            log.error( "exception while excute batchInsertStockHolderComponent " +JSONObject.toJSONString(list) + " :{}",exception);
                        }
                    }
                }
            }
            try {
                Thread.sleep(3 * 1000);
            } catch (InterruptedException ex) {
                log.error("StockHolderInfoFetchService-fetchStockHolderInfo error:{}",e);
            }
        });

    }

}
