package com.cgs.service.info;

import com.cgs.dao.PlateDAO;
import com.cgs.dao.PolicyTableDAO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.Queue;

@Service
@Slf4j
public class PartyCentralCommitteeService {

    @Value("${notice.industry.url}")
    private String requestUrl;

    private static String PREFIX = "http://www.miit.gov.cn";

    private static String NAME = "党中央";

    private static ThreadLocal<SimpleDateFormat> tl = new ThreadLocal<>();

    private Queue<String> areaQueue = new ArrayDeque<>();

    private Queue<String> contentQueue = new ArrayDeque<>();

    @Autowired
    private PolicyTableDAO policyTableDAO;
    @Autowired
    private PlateDAO plateDAO;


}
