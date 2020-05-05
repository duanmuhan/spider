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
    //独立董事
    private String independentDirector;
    //联系电话
    private String phone;
    //电子信箱
    private String email;
    //传真
    private String fax;
    //公司网址
    private String companyWebsite;
    //办公地址
    private String officeAddress;
    //注册地址
    private String registeredAddress;
    //区域
    private String area;
    //邮政编码
    private String postalCode;
    //注册资本
    private String registeredCapital;
    //工商登记
    private String businessRegistration;
    //雇员人数
    private String numberOfEmployees;
    //管理人员人数
    private String numberOfManagers;
    //律师事务所
    private String lawFirm;
    //会计师事务所
    private String accountingFirm;
    //经营范围
    private String businessScope;




}
