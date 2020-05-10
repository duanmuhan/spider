package com.cgs.dao;

import com.cgs.entity.CompanyInfo;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompanyDAO {

    String TABLE_NAME = "company_info";

    String COLUMNS = "stock_id, company_name, company_en_name, named_used_before, a_share_code, a_share_for_short, b_share_code, b_share_for_short, " +
            "securities_classification, dong_cai_industry, listed_exchange, industry_of_csrc, president, corporate_representative, secretary, chairman, " +
            "securities_representative, independent_director, phone, email, fax, company_website, office_address, registered_address, area, postal_code, " +
            "registered_capital, business_registration, number_of_employees, number_of_managers, law_firm, accounting_firm, business_scope";

    @Insert({"<script>"+
            "insert into " + TABLE_NAME + "(" + COLUMNS + ")" + "values " +
            "<foreach collection='list' index='index' item='item' separator=','>" +
            "(#{item.stockId}, #{item.companyName}, #{item.companyEnName}, #{item.namedUsedBefore}, #{item.aShareCode}, #{item.bShareCode}, " +
            "#{item.bShareForShort}, #{item.securitiesClassification}, #{item.dongCaiIndustry}, #{item.listedExchange}, #{item.industryOfCsrc}, #{item.president}," +
            "#{item.corporateRepresentative}, #{item.secretary}, #{item.chairman}, #{item.securitiesRepresentative}, #{item.independentDirector}, " +
            "#{item.phone}, #{item.email}, #{item.fax}, #{item.companyWebsite}, #{item.officeAddress}, #{item.registeredAddress}, #{item.area}, #{item.postalCode}, #{item.registeredCapital}, #{item.businessRegistration}," +
            " #{item.numberOfEmployees}, #{item.numberOfManagers}, #{item.lawFirm}, #{item.accountingFirm}, #{item.businessScope} )" +
            "</foreach>"+
            "</script>"}
    )
    public void batchInsertCompanyInfo(List<CompanyInfo> list);
}
