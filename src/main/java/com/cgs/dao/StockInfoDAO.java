package com.cgs.dao;

import com.cgs.entity.StockInfo;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockInfoDAO {

    String TABLE_NAME = "stock_info";

    String COLUMNS = "";

    @Insert("")
    Integer addStockItem(StockInfo item);

    @Insert("insert into " + TABLE_NAME + "(" + COLUMNS + ")" + " values(#{item.stockId}, #{item.exchangeId}, #{item.name}, #{item.listingDate}, #{item.updateTime})")
    public void batchInsertStockInfo(List<StockInfo> list);

}
