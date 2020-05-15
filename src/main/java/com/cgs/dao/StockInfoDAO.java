package com.cgs.dao;

import com.cgs.entity.StockInfo;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockInfoDAO {

    String TABLE_NAME = "stock_info";

    @Insert("")
    Integer addStockItem(StockInfo item);

    @Insert("")
    public void batchInsertStockInfo(List<StockInfo> list);

}
