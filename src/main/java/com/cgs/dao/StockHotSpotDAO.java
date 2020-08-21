package com.cgs.dao;

import com.cgs.entity.StockHotspot;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockHotSpotDAO {

    String TABLE_NAME = " stock_hotspot ";

    String COLUMNS = " stock_id , stock_name , rank, release_date ";

    @Insert({"<script>"+
            "insert into " + TABLE_NAME + "(" + COLUMNS + ")" + " values " +
            "<foreach collection='list' index='index' item='item' separator=','>" +
            "(#{item.stockId}, #{item.stockName}, #{item.rank}, #{item.releaseDate} )" +
            "</foreach>"+
            "</script>"}
    )
    public void batchInsertStockHotspotItem(List<StockHotspot> list);
}
