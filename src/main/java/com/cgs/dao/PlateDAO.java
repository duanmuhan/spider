package com.cgs.dao;

import com.cgs.entity.PlateInfo;
import com.cgs.entity.StockItem;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlateDAO {

    String TABLE_NAME=" plate_info ";

    String COLUMNS = " plate_id, plate_name, type, date ";

    @Insert({"<script>"+
            "insert into" + TABLE_NAME + "(" + COLUMNS + ")" + "values " +
            "<foreach collection='list' index='index' item='item' separator=','>" +
            "(#{item.plateId}, #{item.plateName}, #{item.type}, #{item.date})" +
            "</foreach>"+
            "</script>"}
    )
    public void batchInsertPlateInfo(List<PlateInfo> list);

    @Select("select " + COLUMNS + " from " + TABLE_NAME)
    @Results(id="plateMap", value = {
            @Result(column = "id", property = "id", javaType = Long.class),
            @Result(column = "plate_id", property = "plateId", javaType = String.class),
            @Result(column = "plate_name", property = "plateName", javaType = String.class),
            @Result(column = "date", property = "date", javaType = String.class),
            @Result(column = "type", property = "type", javaType = Integer.class)
    })
    public List<PlateInfo> queryAllPlateInfo();

}
