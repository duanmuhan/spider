package com.cgs.dao;

import com.cgs.entity.StockPlateInfoMapping;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockPlateInfoMappingDAO {

    String TABLE_NAME = "plate_stock_mapping";
    String COLUMNS = " stock_id, stock_name, plate_id, plate_name, date ";

    @Results(id="stockPlateInfoMapping", value = {
            @Result(column = "id", property = "id", javaType = Long.class),
            @Result(column = "stock_id", property = "stockId", javaType = String.class),
            @Result(column = "stock_name", property = "stockName", javaType = String.class),
            @Result(column = "plate_id", property = "plateId", javaType = String.class),
            @Result(column = "plate_name", property = "plateName", javaType = String.class),
            @Result(column = "date", property = "date", javaType = String.class)
    })

    @Insert({"<script>"+
            "insert into " + TABLE_NAME + "(" + COLUMNS + ")" + " values " +
            "<foreach collection='list' index='index' item='item' separator=','>" +
            "(#{item.stockId}, #{item.stockName}, #{item.plateId}, #{item.plateName}, #{item.date})" +
            "</foreach>"+
            "</script>"}
            )
    public void batchInsertStockPlateInfoMapping(List<StockPlateInfoMapping> list);

    @Delete("delete from " + TABLE_NAME)
    public void  deleteStockPlateInfoMappingDAO();

    @Select(" select * from " + TABLE_NAME + " where stock_id = #{stockId}")
    public List<StockPlateInfoMapping> queryPlateInfoByStockId(@Param("stockId") String stockId);

    @Select(" select * from " + TABLE_NAME + " where plate_id = #{plateId}")
    public List<StockPlateInfoMapping> queryPlateInfoById(@Param("plateId") String plateId);
}
