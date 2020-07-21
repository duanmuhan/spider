package com.cgs.dao;

import com.cgs.entity.StockInfo;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockInfoDAO {

    String TABLE_NAME = "stock_info";

    String COLUMNS = " stock_id, total_transaction_amount, total_volume, total_share_capital, stock_circulation_share_capital, " +
            "total_market_value, flow_market_value, pe_ratio, average_turnover_rate, date ";

    @Insert({"<script>"+
            "insert into " + TABLE_NAME + "(" + COLUMNS + ")" + " values " +
            "<foreach collection='list' index='index' item='item' separator=','>" +
            "(#{item.stockId}, #{item.totalTransactionAmount}, #{item.totalVolume}, #{item.totalShareCapital}, #{item.stockCirculationShareCapital} " +
            ", #{item.totalMarketValue}, #{item.flowMarketValue} " +
            ", #{item.peRatio} ,#{item.averageTurnoverRate} ,#{item.date})" +
            "</foreach>"+
            "</script>"})
    public void batchInsertStockInfo(List<StockInfo> list);

}
