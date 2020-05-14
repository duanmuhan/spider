package com.cgs.dao;

import com.cgs.entity.StockPlateInfoMapping;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockPlateInfoMappingDAO {

    String TABLE_NAME = "plate_stock_mapping";
    String COLUMNS = "id, stock_id, stock_name, plate_id, plate_name, date";

    @Insert({"<script>"+
            "insert into " + TABLE_NAME + "(" + COLUMNS + ")" + "values " +
            "<foreach collection='list' index='index' item='item' separator=','>" +
            "(#{item.stockId}, #{item.stockName}, #{item.plateId}, #{item.plateName}, #{item.date})" +
            "</foreach>"+
            "</script>"}
            )
    public void batchInsertStockPlateInfoMapping(List<StockPlateInfoMapping> list);

    public List<StockPlateInfoMapping> queryPlateInfoByStockId(String stockId);

    public List<StockPlateInfoMapping> queryPlateInfoById(String plateId);
}
