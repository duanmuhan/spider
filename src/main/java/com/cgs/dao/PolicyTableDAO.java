package com.cgs.dao;

import com.cgs.entity.PolicyInfo;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PolicyTableDAO {

    String TABLE_NAME = "policy_table";

    String COLUMNS = " title , target_plate , target_plate_id, source ,release_date";

    public void insertPolicyTable(PolicyInfo policyInfo);

    @Insert({"<script>"+
            "insert into " + TABLE_NAME + "(" + COLUMNS + ")" + " values " +
            "<foreach collection='list' index='index' item='item' separator=','>" +
            "(#{item.title}, #{item.targetPlate}, #{item.targetPlateId} ,  #{item.source}" +
            ", #{item.release_date})" +
            "</foreach>"+
            "</script>"})
    public void batchInsertPolicyTable(List<PolicyInfo> list);
}
