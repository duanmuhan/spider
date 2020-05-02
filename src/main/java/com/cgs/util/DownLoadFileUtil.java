package com.cgs.util;

public class DownLoadFileUtil {

	public String getFileNameByUrl(String url,String contentType){
		url = url.substring(7);
		if(contentType.indexOf("html") != -1){
			url = url.replaceAll("[\\?/:*|<>\"]", "_") + ".html";
			return url;
		}else{
			  return url.replaceAll("[\\?/:*|<>\"]", "_") + "." + contentType.substring(contentType.lastIndexOf("/") + 1);
		}
	}
	
	private void saveToLocal(byte[] data,String filePath){
		
	}
}
