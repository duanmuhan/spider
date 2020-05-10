package com.cgs.dto;

import com.cgs.entity.CompanyInfo;
import lombok.Data;

@Data
public class CompanyInfoDTO {

    private String gsmc;
    private String ywmc;
    private String cym;
    private String agdm;
    private String agjc;
    private String bgdm;
    private String bqjc;
    private String hgdm;
    private String hgjc;
    private String zqlb;
    private String sshy;
    private String ssjys;
    private String sszjhhy;
    private String zjl;
    private String frdb;
    private String dm;
    private String dsz;
    private String zqswdb;
    private String dlds;
    private String lxdh;
    private String dzxx;
    private String cz;
    private String gswz;
    private String bgdz;
    private String zcdz;
    private String qy;
    private String yzbm;
    private String zczb;
    private String gsdj;
    private String gyrs;
    private String glryrs;
    private String lssws;
    private String kjsws;
    private String gsjj;
    private String jyfw;

    public CompanyInfo convertCompanyInfo(){
        CompanyInfo companyInfoDTO = new CompanyInfo();
        companyInfoDTO.setStockId(getAgdm());
        companyInfoDTO.setCompanyName(getGsmc());
        companyInfoDTO.setCompanyEnName(getYwmc());
        companyInfoDTO.setNameUsedBefore(getCym());
        companyInfoDTO.setAShareCode(getAgdm());
        companyInfoDTO.setAShareForShort(getAgjc());
        companyInfoDTO.setBShareCode(getBgdm());
        companyInfoDTO.setBShareForShort(getBqjc());
        companyInfoDTO.setHShareCode(getHgdm());
        companyInfoDTO.setHShareForShort(getHgjc());
        companyInfoDTO.setSecuritiesClassification(getZqlb());
        companyInfoDTO.setDongcaiIndustry(getSshy());
        companyInfoDTO.setListedExchange(getSsjys());
        companyInfoDTO.setIndustryOfCSRC(getSszjhhy());
        companyInfoDTO.setPresident(getZjl());
        companyInfoDTO.setCorporateRepresentative(getFrdb());
        companyInfoDTO.setSecretary(getDm());
        companyInfoDTO.setChairman(getDsz());
        companyInfoDTO.setSecuritiesRepresentative(getZqswdb());
        companyInfoDTO.setIndependentDirector(getDlds());
        companyInfoDTO.setPhone(getLxdh());
        companyInfoDTO.setEmail(getDzxx());
        companyInfoDTO.setFax(getCz());
        companyInfoDTO.setCompanyWebsite(getGswz());
        companyInfoDTO.setOfficeAddress(getBgdz());
        companyInfoDTO.setRegisteredAddress(getZcdz());
        companyInfoDTO.setArea(getQy());
        companyInfoDTO.setPostalCode(getYzbm());
        companyInfoDTO.setRegisteredCapital(getZczb());
        companyInfoDTO.setBusinessRegistration(getGsdj());
        companyInfoDTO.setNumberOfEmployees(getGyrs());
        companyInfoDTO.setNumberOfManagers(getGlryrs());
        companyInfoDTO.setLawFirm(getLssws());
        companyInfoDTO.setAccountingFirm(getKjsws());
        companyInfoDTO.setBusinessScope(getJyfw());
        return companyInfoDTO;
    }

}
