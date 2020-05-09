package com.cgs.dto;

import com.cgs.entity.CompanyInfo;
import lombok.Data;

@Data
public class CompanyInfoDTO {

    private String gsmc;
    private String ywmc;
    private String cym;
    private String agdm;
    private String aqjc;
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

    private CompanyInfo convertCompanyInfo(){
        CompanyInfo companyInfoDTO = new CompanyInfo();
        companyInfoDTO.setStockId(getAgdm());
        return companyInfoDTO;
    }

}
