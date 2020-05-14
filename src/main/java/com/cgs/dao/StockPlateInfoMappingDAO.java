package com.cgs.dao;

import com.cgs.entity.StockPlateInfoMapping;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockPlateInfoMappingDAO {

    public void batchInsertStockPlateInfoMapping(List<StockPlateInfoMapping> list);

    public List<StockPlateInfoMapping> queryPlateInfoByStockId(String stockId);

    public List<StockPlateInfoMapping> queryPlateInfoById(String plateId);
}
