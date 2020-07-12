package com.cgs.dto;

import lombok.Data;

@Data
public class StockHolderTopTenDTO {
    //日期
    private String rq;
    //股东名称
    private String mc;
    //股东性质
    private String gdmc;
    //股份类型
    private String gflx;
    //持股数(股)
    private String cgs;
    //占总流通股本持股比例
    private String zltgbcgbl;
    //增减(股)
    private String zj;
    //变动比例
    private String bdbl;
}
