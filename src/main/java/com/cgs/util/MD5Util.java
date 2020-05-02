package com.cgs.util;

import java.security.MessageDigest;

import org.apache.commons.codec.binary.StringUtils;
import org.jsoup.helper.StringUtil;

public class MD5Util {
	
	private static MessageDigest md5 = null;
	
	static{
		try{
			md5 = MessageDigest.getInstance("MD5");
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	public static String getMd5(String str){
		return null;
	}

}
