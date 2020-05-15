package com.cgs.dao;

import com.cgs.entity.FinanceInfo;
import com.cgs.entity.StockItem;
import org.apache.ibatis.annotations.Insert;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FinanceInfoDAO {

    String TABLE_NAME = "finance_info";
    String COLUMNS = " stock_id, basic_earning_per_common_share," +
            " deduction_of_non_eps, diluted_eps, net_asset_value_per_share, per_share_reserve, udpps," +
            " operating_cash_flow_per_share, gross_revenue, gross_profit, attributable_net_profit, deduct_non_net_profit, year_on_year_growth_of_total_operate_revenue, attributable_net_profit_increased_year_on_year, " +
            " deduction_of_non_net_profit_increased_year_on_year, attributable_net_profit_increases_on_arollingbasis, deduction_of_non_net_profit, weighted_return_on_equity, diluted_return_on_equity," +
            " diluted_return_on_total_assets, rate_of_gross_profit, rate_of_margin, net_margin, etr, items_received_in_advance, cash_flow_from_sales, operation_cash_flow, total_assets_turnover, days_sales_outstanding," +
            " days_in_inventory, debt, accrued_liabilities, current_ratio, quick_ratio, release_date, date ";

    @Insert({"<script>"+
            "insert into " + TABLE_NAME + "(" + COLUMNS + ")" + "values " +
            "<foreach collection='list' index='index' item='item' separator=','>" +
            "(#{item.stockId}, #{item.basicEarningsPerCommonShare}, #{item.deductionOfNonEPS}, #{item.dilutedEPS}, #{item.netAssetValuePerShare},#{item.perShareReserve}, #{item.UDPPS}, #{item.operatingCashFlowPerShare}, " +
            " #{item.grossRevenue}, #{item.grossProfit}, #{item.attributableNetProfit}, #{item.deductNonNetProfit}, #{item.yearOnYearGrowthOfTotalOperatingRevenue}, #{item.attributableNetProfitIncreasedYearOnYear}, #{item.deductionOfNonNetProfitIncreasedYearOnYear}, #{item.attributableNetProfitIncreasesOnARollingBasis}," +
            " #{item.deductionOfNonNetProfit}, #{item.weightedReturnOnEquity}, #{item.dilutedReturnOnEquity}, " +
            " #{item.dilutedReturnOnTotalAssets}, #{item.rateOfGrossProfit}, #{item.rateOfMargin}, #{item.netMargin}, #{item.ETR}, #{item.itemsReceivedInAdvance}, #{item.cashFlowFromSales}, #{item.operationCashFlow}, #{item.totalAssetsTurnover}, #{item.daysSalesOutstanding}, #{item.daysInInventory}, #{item.DEBT}, " +
            " #{item.accruedLiabilities}, #{item.currentRatio}, #{item.quickRatio}, #{item.releaseDate}, #{item.date})" +
            "</foreach>"+
            "</script>"}
    )
    public void batchInsertFinanceInfo(List<FinanceInfo> list);
}
