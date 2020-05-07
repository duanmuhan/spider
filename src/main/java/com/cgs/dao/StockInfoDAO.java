package com.cgs.dao;

import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

@Repository
public interface StockInfoDAO {

    @Insert("")
    Integer addStockItem(StockItem item);

}