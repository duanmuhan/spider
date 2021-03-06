package com.cgs.dao;

import com.cgs.entity.StockTechnology;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockTechnologyDAO {

    String TABLE_NAME = " stock_technology ";
    String COLUMNS = "stock_id, type, special, query_str, tag, desc_str, release_date";

    @Insert({"<script>"+
            "insert into " + TABLE_NAME + "(" + COLUMNS + ")" + " values " +
            "<foreach collection='list' index='index' item='item' separator=','>" +
            "(#{item.stockId}, #{item.type}, #{item.special}, #{item.queryStr}, #{item.tag}, #{item.descStr}, #{item.releaseDate})" +
            "</foreach>"+
            "</script>"}
    )
    public void batchInsertStockTechnology(List<StockTechnology> list);
}
