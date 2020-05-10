package com.cgs.entity;

import lombok.Data;

@Data
public class PlateInfo {

    private Long id;
    private String plateId;
    private String plateName;
    //更新时间
    private String date;
    private Integer type;
}
