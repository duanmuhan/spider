package com.cgs.entity.index;

import lombok.Data;

@Data
public class KDJItem {
    private String stockId;
    private Integer kValue;
    private Integer dValue;
    private Integer jValue;
    private String date;
}
