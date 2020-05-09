package com.cgs.dao;

import com.cgs.entity.StockItem;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockItemDAO {

    String TABLE_NAME = "stock_item";
    String COLUMNS = "stock_id, exchange_id, name, listing_date, update_date";

    @Insert({"<script>"+
            "insert into " + TABLE_NAME + "(" + COLUMNS + ")" + "values " +
            "<foreach collection='list' index='index' item='item' separator=','>" +
            "(#{item.stockId}, #{item.exchangeId}, #{item.name}, #{item.listingDate}, #{item.updateTime} )" +
            "</foreach>"+
            "</script>"}
            )
    public void batchInsertStockItem(List<StockItem> list);

    @Insert("insert into " + TABLE_NAME + "(" + COLUMNS + ")" + " values(#{item.stockId}, #{item.exchangeId}, #{item.name}, #{item.listingDate}, #{item.updateTime})")
    public void insertStockItem(@Param("item") StockItem item);

    @Select("select stock_id as stockId, listing_date as listingDate, exchange_id as exchangeId, update_date as updateDate from " + TABLE_NAME)
    public List<StockItem> queryAllStockList();

    @Delete("delete  from " + TABLE_NAME)
    public void deleteAll();
}
