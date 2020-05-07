package com.cgs.service;

import com.cgs.dao.CompanyDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyFetchService {

    @Autowired
    private CompanyDAO companyDAO;

    public void fetchCompanyInfo(){

    }

}
