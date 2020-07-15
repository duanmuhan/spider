package com.cgs.dto;

import lombok.Data;

import java.util.List;

@Data
public class StockHolderTopTenOutDTO {
    private String rq;
    private List<StockHolderTopTenDTO> sdltgd;
}
