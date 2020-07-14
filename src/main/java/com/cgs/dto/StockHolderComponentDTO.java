package com.cgs.dto;

import lombok.Data;

@Data
public class StockHolderComponentDTO {
    //日期
    private String rq;
    //机构类型
    private String jglx;
    // 持仓家数
    private String ccjs;
    // 持仓股数(股)
    private String ccgs;
    // 占流通股比例
    private String zltgbl;
    // 占总股本比例
    private String zltgbbl;
}
