package com.cgs.dao.index;

import com.cgs.entity.index.KDJItem;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface KDJDAO {
    String TABLE_NAME = "kdj_item";
    String COLUMNS = " stock_id, k_value, d_value, j_value, date ";

    @Insert({"<script>"+
            "insert into " + TABLE_NAME + "(" + COLUMNS + ")" + "values " +
            "<foreach collection='list' index='index' item='item' separator=','>" +
            "(#{item.stockId}, #{item.kValue}, #{item.dValue}, #{item.jValue}, #{item.date})" +
            "</foreach>"+
            "</script>"}
    )
    public void batchInsertKDJItems(List<KDJItem> list);

}
