package com.cgs.dao.index;

import com.cgs.entity.index.AverageItem;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AverageDAO {

    String TABLE_NAME = " average_item ";

    String COLUMNS = " stock_id, price, type, date ";

    @Insert({"<script>"+
            "insert into " + TABLE_NAME + "(" + COLUMNS + ")" + "values " +
            "<foreach collection='list' index='index' item='item' separator=','>" +
            "(#{item.stockId}, #{item.price}, #{item.type}, #{item.date})" +
            "</foreach>"+
            "</script>"}
    )
    public void batchInsertAverageItem(List<AverageItem> averageItems);

    @Select(" select * from " + TABLE_NAME + "where stock_id=#{stockId} and type=#{type}" )
    public List<AverageItem> queryAverageItemByStockId(@Param("stockId") String stockId,@Param("type") Integer type);
}
