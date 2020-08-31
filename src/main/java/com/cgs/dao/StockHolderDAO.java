package com.cgs.dao;

import com.cgs.entity.StockHolder;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockHolderDAO {

    String TABLE_NAME = " stock_holder ";

    String COLUMNS = " stock_id, number_of_share_holder, per_capita_tradable_shares, last_change," +
            " stock_convergence_rate, price, per_capita_holding_amount, top_ten_stock_holder, top_ten_stock_flow_holder, release_date ";

    @Insert({"<script>"+
            "insert into " + TABLE_NAME + "(" + COLUMNS + ")" + " values " +
            "<foreach collection='list' index='index' item='item' separator=','>" +
            "( #{item.stockId}, #{item.numberOfShareholders}, #{item.perCapitaTradableShares} ,  #{item.lastChange}" +
            ", #{item.stockConvergenceRate},  #{item.price} " +
            ", #{item.perCapitaHoldingAmount},  #{item.topTenStockHolder} " +
            ", #{item.topTenStockFlowHolder},  #{item.releaseDate} " +
            ")" +
            "</foreach>"+
            "</script>"})
    public void batchInsertStockHolder(List<StockHolder> list);


    @Results( id = "resultMap",value = {
            @Result(property = "stockId",column = "stock_id"),
            @Result(property = "numberOfShareholders",column = "number_of_share_holder"),
            @Result(property = "perCapitaTradableShares",column = "per_capita_tradable_shares"),
            @Result(property = "lastChange",column = "last_change"),
            @Result(property = "stockConvergenceRate",column = "stock_convergence_rate"),
            @Result(property = "price",column = "price"),
            @Result(property = "perCapitaHoldingAmount",column = "per_capita_holding_amount"),
            @Result(property = "topTenStockHolder",column = "top_ten_stock_holder"),
            @Result(property = "topTenStockFlowHolder",column = "top_ten_stock_flow_holder"),
            @Result(property = "releaseDate",column = "release_date")
    })
    @Select(" select * from  " + TABLE_NAME + " where stock_id= #{stockId}")
    public List<StockHolder> queryStockHolderByStockId(@Param("stockId") String stockId);

}
