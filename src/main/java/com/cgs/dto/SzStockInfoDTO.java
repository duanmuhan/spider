package com.cgs.dto;

import lombok.Data;
import org.springframework.util.StringUtils;

@Data
public class SzStockInfoDTO {

    private String now_cjbs;
    private String now_cjje;
    private String now_syl;
    private String now_hsl;
    private String now_sjzz;
    private String now_ltsz;
    private String now_zgb;
    private String now_ltgb;

    public boolean isEmpty(){
        return StringUtils.isEmpty(now_cjbs) || StringUtils.isEmpty(now_cjje) || StringUtils.isEmpty(now_syl) || StringUtils.isEmpty(now_hsl)
                || StringUtils.isEmpty(now_sjzz)
                || StringUtils.isEmpty(now_ltsz) || StringUtils.isEmpty(now_ltsz)
                || StringUtils.isEmpty(now_zgb) || StringUtils.isEmpty(now_ltgb);
    }
}
