package com.cgs.dto;

import lombok.Data;

@Data
public class StockHolderDTO {
    //日期
    private String rq;
    //股东人数
    private String gdrs;
    //人均流通股
    private String rjltg;
    //较上期变化(%)
    private String rjltg_jsqbh;
    //筹码集中度
    private String cmjzd;
    //股价(元)
    private String gj;
    //人均持股金额(元)
    private String rjcgje;
    //前十大股东持股合计(%)
    private String qsdgdcghj;
    //前十大流通股东持股合计(%)
    private String qsdltgdcghj;
}
