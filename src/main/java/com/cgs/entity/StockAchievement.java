package com.cgs.entity;

import lombok.Data;

@Data
public class StockAchievement {
    private String stockId;
    private String stockName;
    private String achievementType;
    private String achievementTitle;
    private Double profileChangeRate;
    private String profileLastYear;
    private String releaseDate;
}
