package com.cgs.dao;

import com.cgs.entity.PlateInfo;
import com.cgs.entity.StockItem;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlateDAO {

    String TABLE_NAME="plate_info";

    String COLUMNS = "";

    @Insert({"<script>"+
            "insert into " + TABLE_NAME + "(" + COLUMNS + ")" + "values " +
            "<foreach collection='list' index='index' item='item' separator=','>" +
            "(#{item.stockId}, #{item.exchangeId}, #{item.name}, #{item.listingDate}, #{item.updateTime} )" +
            "</foreach>"+
            "</script>"}
    )
    public void batchInsertPlateInfo(List<PlateInfo> list);

}
