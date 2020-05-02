package com.cgs.dao;

import com.cgs.entity.StockItem;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

@Repository
public interface StockInfoDAO {

    @Insert("")
    Integer addStockItem(StockItem item);

}
