package com.cgs.dto;

import lombok.Data;

import java.util.List;

@Data
public class StockTechnologyDTO {
    private String special;
    private String query;
    private List<String> tag;
    private String desc;
}
