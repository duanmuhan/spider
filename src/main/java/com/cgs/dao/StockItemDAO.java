package com.cgs.dao;

import com.cgs.entity.StockItem;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockItemDAO {

    String TABLE_NAME = "stock_item";

    public void batchInsertStockItem(List<StockItem> list);
}
