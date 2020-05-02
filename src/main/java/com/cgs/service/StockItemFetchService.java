package com.cgs.service;

import com.cgs.dao.StockItemDAO;
import com.cgs.entity.StockItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StockItemFetchService {

    @Autowired
    private StockItemDAO stockDAO;

}
