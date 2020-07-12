package com.cgs.entity;

import lombok.Data;

@Data
public class InstitutionalPosition {
    private String stockId;
    private String orginationType;
    private String numberOfHoldingPosition;
    private String proportionOfTradableShares;
    private String proportionInTotalEquity;
    private String releaseDate;
    private String date;
}
