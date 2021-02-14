package com.cgs.esdao;

import org.springframework.stereotype.Repository;

/**
 * @author caoguangshu
 * @date 2021/2/13
 * @time 9:02 下午
 */
@Repository
public class StockItemEsDAO extends BaseEsDAO{

    private static String INDEX_NAME = "stock_item_index";

    private static String TYPE_NAME = "stock_item_type";

    public StockItemEsDAO() {
        super(INDEX_NAME, TYPE_NAME);
    }

}
