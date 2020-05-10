package com.cgs.dao;

import com.cgs.entity.CompanyInfo;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyDAO {

    String TABLE_NAME = "company_info";

    String TABLE_COLUMNS = "";

    @Insert("")
    public void batchInsertCompanyInfo(List<CompanyInfo> list);
}
