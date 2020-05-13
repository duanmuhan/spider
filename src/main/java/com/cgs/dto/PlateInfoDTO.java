package com.cgs.dto;

import com.cgs.entity.PlateInfo;
import lombok.Data;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
public class PlateInfoDTO {
    private String f1;
    private String f2;
    private String f3;
    private String f4;
    private String f5;
    private String f6;
    private String f7;
    private String f8;
    private String f9;
    private String f10;
    private String f11;
    private String f12;
    private String f13;
    private String f14;
    private String f15;
    private String f16;
    private String f17;
    private String f18;
    private String f20;
    private String f21;
    private String f22;
    private String f23;
    private String f24;
    private String f25;
    private String f26;
    private String f33;
    private String f62;
    private String f128;
    private String f136;
    private String f115;
    private String f152;
    private String f124;
    private String f107;
    private String f104;
    private String f105;
    private String f140;
    private String f141;
    private String f207;
    private String f222;

    public PlateInfo convertToPlateInfo(int type){
        PlateInfo plateInfo = new PlateInfo();
        plateInfo.setPlateId(getF12());
        plateInfo.setPlateName(getF14());
        plateInfo.setType(type);
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        plateInfo.setDate(sdf.format(new Date()));
        return plateInfo;
    }

}
