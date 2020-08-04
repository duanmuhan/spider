package com.cgs.dao;

import com.cgs.entity.StockAchievement;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockAchievementDAO {

    String TABLE_NAME = " stock_achievement ";

    String COLUMNS = " stock_id, stock_name, achievement_type, achievement_abstract, profit_change_rate, profit_last_year, release_date ";

    @Insert({"<script>"+
            "insert into " + TABLE_NAME + "(" + COLUMNS + ")" + " values " +
            "<foreach collection='list' index='index' item='item' separator=','>" +
            "(#{item.stockId}, #{item.stockName}, #{item.achievementType} ,  #{item.achievementTitle}" +
            ", #{item.profileChangeRate} ,  #{item.profileLastYear} " +
            ", #{item.releaseDate} )" +
            "</foreach>"+
            "</script>"})
    public void batchInsertStockAchievement(List<StockAchievement> list);

    @Results(id="stockAchievement", value = {
            @Result(property = "stockId",column = "stock_id"),
            @Result(property = "stockName",column = "stock_name"),
            @Result(property = "achievementType",column = "achievement_type"),
            @Result(property = "achievementTitle",column = "achievement_title"),
            @Result(property = "profileChangeRate",column = "profile_change_rate"),
            @Result(property = "profileLastYear",column = "profile_last_year"),
            @Result(property = "releaseDate",column = "release_date")
    })
    @Select(" select * from " + TABLE_NAME)
    public List<StockAchievement> batchQueryStockAchievementList();
}
