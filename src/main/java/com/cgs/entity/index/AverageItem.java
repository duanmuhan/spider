package com.cgs.entity.index;

import lombok.Data;

@Data
public class AverageItem {
    private String stockId;
    private Double price;
    private Integer averageTye;
    private String date;
}
