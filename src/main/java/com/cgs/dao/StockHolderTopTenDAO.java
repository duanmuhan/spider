package com.cgs.dao;

import com.cgs.entity.StockHolderTopTen;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockHolderTopTenDAO {

    String TABLE_NAME = " stock_holder_top_ten ";

    String COLUMNS = " stock_id, stock_holder_name, type_of_stock_holder, type_of_share," +
            " numbers_of_shares, rate, changes, change_rate, release_date ";

    @Insert({"<script>"+
            "insert into " + TABLE_NAME + "(" + COLUMNS + ")" + " values " +
            "<foreach collection='list' index='index' item='item' separator=','>" +
            "(#{item.stockId}, #{item.stockHolderName}, #{item.typesOfStockHolders}, #{item.typesOfShares}, #{item.numbersOfShares}, #{item.rate} " +
            ", #{item.changes}, #{item.changeRate} " +
            ", #{item.releaseDate})" +
            "</foreach>"+
            "</script>"})
    public void batchInsertStockHolderTopTen(List<StockHolderTopTen> list);

    @Results( id = "resultMap",value = {
            @Result(property = "stockId",column = "stock_id"),
            @Result(property = "stockHolderName",column = "stock_holder_name"),
            @Result(property = "typesOfStockHolders",column = "type_of_stock_holder"),
            @Result(property = "typesOfShares",column = "type_of_share"),
            @Result(property = "numbersOfShares",column = "numbers_of_shares"),
            @Result(property = "rate",column = "rate"),
            @Result(property = "changes",column = "changes"),
            @Result(property = "changeRate",column = "change_rate"),
            @Result(property = "releaseDate",column = "release_date"),
    })
    @Select("select * from " + TABLE_NAME + " where stock_id=#{stockId} and release_date=#{releaseDate} ")
    public List<StockHolderTopTen> queryStockHolder(String stockId, String releaseDate);

}
