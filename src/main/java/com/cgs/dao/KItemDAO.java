package com.cgs.dao;

import com.cgs.entity.KItemDate;
import com.cgs.entity.index.KItem;
import org.apache.ibatis.annotations.*;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KItemDAO {

    String TABLE_NAME = " k_item ";

    String COLUMNS = " stock_id, open_price, close_price, high, low, deal_amount, type, date ";

    @Insert({"<script>"+
            "insert into " + TABLE_NAME + "(" + COLUMNS + ")" + " values " +
            "<foreach collection='list' index='index' item='item' separator=','>" +
            "(#{item.stockId}, #{item.openPrice}, #{item.closePrice}, #{item.high}, #{item.low}, #{item.dealAmount}, #{item.type}, #{item.date} )" +
            "</foreach>"+
            "</script>"}
    )
    public void batchInsertKItem(List<KItem> list);

    @Select("select max(date) as date,stock_id as stockId from " + TABLE_NAME + " where stock_id=#{stockId} and type=#{type} ")
    public String queryLatestDateOfKItem(@Param("stockId") String stockId, @Param("type") Integer type);

    @Select("select max(date) as date,stock_id as stockId from " + TABLE_NAME + " group by stock_id ")
    public List<KItemDate> queryKItemLatestDate();

    @Select("select * from" + TABLE_NAME + "where stock_id = #{stockId} and type = #{type} order by date asc" )
    @Results( id = "resultMap",value = {
            @Result(property = "stockId",column = "stock_id"),
            @Result(property = "openPrice",column = "open_price"),
            @Result(property = "closePrice",column = "close_price"),
            @Result(property = "high",column = "high"),
            @Result(property = "low",column = "low"),
            @Result(property = "dealAmount",column = "deal_amount"),
            @Result(property = "dealCash",column = "deal_cash"),
            @Result(property = "type",column = "type"),
            @Result(property = "date",column = "date")
    })
    public List<KItem> queryDateKItemsbyStockId(@Param("stockId") String stockId,@Param("type") Integer type);

    @Select("select * from k_item where date = (select max(date) from k_item where date not in (select max(date) from k_item where date<#{targetDate})) and type = 1" )
    @ResultMap(value = "resultMap")
    public List<KItem> querySecondLatestDateOfTargetDate(@Param("targetDate") String date);

    @Select("select * from k_item where date=#{date} and type=#{type}")
    @ResultMap(value = "resultMap")
    public List<KItem> queryKItemByDate(@Param("date") String date, @Param("type") Integer type);
}

