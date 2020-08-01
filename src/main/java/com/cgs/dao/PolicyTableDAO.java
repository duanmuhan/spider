package com.cgs.dao;

import com.cgs.entity.PolicyInfo;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PolicyTableDAO {

    String TABLE_NAME = "policy_table";

    String COLUMNS = " title , target_plate , target_plate_id, source , plateform,release_date";


    @Insert({"<script>"+
            "insert into " + TABLE_NAME + "(" + COLUMNS + ")" + " values " +
            "<foreach collection='list' index='index' item='item' separator=','>" +
            "(#{item.title}, #{item.targetPlate}, #{item.targetPlateId} , #{item.source}" +
            ", #{item.platform} ,#{item.release_date})" +
            "</foreach>"+
            "</script>"})
    public void batchInsertPolicyTable(List<PolicyInfo> list);
}
