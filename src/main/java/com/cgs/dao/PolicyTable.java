package com.cgs.dao;

import org.springframework.stereotype.Repository;

@Repository
public interface PolicyTable {

    String TABLE_NAME = "policy_table";

    String COLUMNS = " id, notice_brief , title , target_plate ,source ,release_date ";


}
