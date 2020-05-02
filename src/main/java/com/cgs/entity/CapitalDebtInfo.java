package com.cgs.entity;

import lombok.Data;

@Data
public class CapitalDebtInfo {

    private String stockId;
    //货币资金
    private String monetaryFund;
    //应收票据及应收账款
    private String notesAndReceivables;
    //应收票据
    private String noteReceivable;
    //应收账款
    private String accountsReceivable;
    //预付款项
    private String advancePayment;
    //其他应收款合计
    private String totalOtherReceivables;
    //应收股利
    private String dividendReceivable;
    //其他应收款
    private String otherReceivable;
    //存货
    private String reminder;
    //其他流动资产
    private String otherLiquidAssets;
    //长期股权投资
    private String longTermEquityInvestment;
    //固定资产
    private String fixedAssets;
    //在建工程
    private String  constructionInProgress;
    //无形资产
    private String intangibleAssets;
    //递延所得税资产
    private String deferredTaxAssets;
    //其他非流动资产
    private String otherNonCurrentAssets;
    //非流动资产合计
    private String totalNonCurrentAssets;
    //资产总计
    private String totalAssets;
    //应付票据及应付账款
    private String notesPayableAndAccountsPayable;
    //应付票据
    private String billPayable;
    //应付账款
    private String accountsPayable;
    //预收款项
    private String unearnedRevenue;
    //应付职工薪酬
    private String accruedPayroll;
    //应交税费
    private String taxesPayable;
    //其他应付款合计
    private String totalOtherPayables;
    //其他应付款
    private String otherPayables;
    //其他流动负债
    private String otherCurrentLiabilities;
    //流动负债合计
    private String totalCurrentLiabilities;
    //递延所得税负债
    private String deferredTaxLiability;
    //非流动负债合计
    private String totalNonCurrentLiabilities;
    //负债合计
    private String totalLiabilities;
    //实收资本
    private String paidInCapital;
    //资本公积
    private String capitalReserves;
    //盈余公积
    private String surplusReserves;
    //未分配利润
    private String unappropriatedProfits;
    //归属于母公司股东权益合计
    private String totalShareholdersEquityAttributableToTheParentCompany;
    //股东权益合计
    private String totalEquity;
    //负债和股东权益合计
    private String totalLiabilitiesAndShareholdersEquity;


}
