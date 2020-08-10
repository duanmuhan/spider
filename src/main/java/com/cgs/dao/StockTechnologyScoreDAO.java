package com.cgs.dao;

import com.cgs.entity.StockTechnologyScore;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockTechnologyScoreDAO {

    String TABLE_NAME = " stock_score ";
    String COLUMNS = "stock_id, stock_name, score, release_date";

    @Insert({"<script>"+
            "insert into " + TABLE_NAME + "(" + COLUMNS + ")" + " values " +
            "<foreach collection='list' index='index' item='item' separator=','>" +
            "(#{item.stockId}, #{item.stockName}, #{item.score}, #{item.releaseDate})" +
            "</foreach>"+
            "</script>"}
    )
    public void batchInsertStockTechnologyScore(List<StockTechnologyScore> list);
}
