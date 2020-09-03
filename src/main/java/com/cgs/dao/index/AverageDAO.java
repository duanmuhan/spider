package com.cgs.dao.index;

import com.cgs.entity.index.AverageItem;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.cache.annotation.Cacheable;
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

    @Select(" select max(date) as date from " + TABLE_NAME + "where stock_id=#{stockId} and type=#{type}")
    @Cacheable(value = "kitem:queryAverageLatestDateByStockId",key = "#stockId + '-' + #type")
    public Integer queryAverageLatestDateByStockId(String stockId,Integer type);

    @Delete(" delete from " + TABLE_NAME + " where stock_id=#{stockId} and type=#{type} and date=#{date} ")
    public void deleteAverageItemByDateAndType(@Param("stockId") String stockId,
                                               @Param("type") Integer type,
                                               @Param("date") Integer date);

    @Select(" select * from " + TABLE_NAME + "where stock_id=#{stockId} and type=#{type}" )
    public List<AverageItem> queryAverageItemByStockId(@Param("stockId") String stockId,@Param("type") Integer type);
}
