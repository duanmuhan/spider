package com.cgs.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class CompanyInfo implements Serializable {

    private String stockId;
    private String companyName;
    private String companyEnName;
    private String nameUsedBefore;
    private String aShareCode;
    private String aShareForShort;
    private String bShareCode;
    private String bShareForShort;
    private String hShareCode;
    private String hShareForShort;
    private String securitiesClassification;
    //所属东财行业
    private String dongcaiIndustry;
    //上市交易所
    private String ListedExchange;
    //所属证监会行业
    private String industryOfCSRC;
    //总经理
    private String president;
    private String corporateRepresentative;
    //董秘
    private String secretary;
    private String chairman;
    //证券事务代表
    private String securitiesRepresentative;


}
