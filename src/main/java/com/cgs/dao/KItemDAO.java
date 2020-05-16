package com.cgs.dao;

import com.cgs.entity.index.KItem;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KItemDAO {

    String TABLE_NAME = "k_item";

    String COLUMNS = " stock_id, open_price, close_price, high, low, deal_amount, date ";

    @Insert({"<script>"+
            "insert into " + TABLE_NAME + "(" + COLUMNS + ")" + "values " +
            "<foreach collection='list' index='index' item='item' separator=','>" +
            "(#{item.stockId}, #{item.openPrice}, #{item.closePrice}, #{item.high}, #{item.low}, #{item.dealAmount}, #{item.date} )" +
            "</foreach>"+
            "</script>"}
    )
    public void batchInsertKItem(List<KItem> list);

    @Insert(" insert into " + TABLE_NAME + "(" + COLUMNS + ")" + "values (#{item.stockId}, #{item.openPrice}," +
            " #{item.closePrice}, #{item.high}, #{item.low}, #{item.dealAmount}, #{item.date})")
    public void insertKItem(@Param("item") KItem item);
}
