package com.cgs.entity.index;

import lombok.Data;

@Data
public class KDJItem {
    private String stockId;
    private Double kValue;
    private Double dValue;
    private Double jValue;
    private String date;
}
