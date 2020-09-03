package com.cgs.dao;

import com.cgs.entity.StockMoodIndex;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockMoodIndexFetchDAO {

    String TABLE_NAME = " stock_mood_index ";
    String COLUMNS = "stock_increase_rate, stock_point, stock_total_count, stock_increase_count, release_date ";

    @Insert({"<script>" +
            "insert into " + TABLE_NAME + "(" + COLUMNS + ")" + " values " +
            "<foreach collection='list' index='index' item='item' separator=','>" +
            "(#{item.stockIncreaseRate}, #{item.stockPoint}, #{item.stockTotalCount}, #{item.stockIncreaseCount}, #{item.releaseDate})" +
            "</foreach>" +
            "</script>"}
    )
    public void batchInsertStockMoodIndexItem(List<StockMoodIndex> list);

    @Select(" select max(release_date) as date from  " + TABLE_NAME)
    public String queryLatestDateOfStockMoodIndex();
}
