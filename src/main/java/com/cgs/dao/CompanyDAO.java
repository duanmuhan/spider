package com.cgs.dao;

import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyDAO {

    String TABLE_NAME = "company_info";
    @Insert("")
    public void batchInsertCompanyInfo();
}
