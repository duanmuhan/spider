package com.cgs.dao;

import com.cgs.entity.PlateInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlateDAO {

    String TABLE_NAME="plate_info";

    public void batchInsertPlateInfo(List<PlateInfo> list);

}
