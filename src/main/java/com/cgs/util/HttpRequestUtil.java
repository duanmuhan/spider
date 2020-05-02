package com.cgs.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class HttpRequestUtil {
	
	static CloseableHttpClient  httpClient =  HttpClients.createDefault();;
	
	public static String getRequest(String url) throws ClientProtocolException, IOException{
		String pageContent = "";
		HttpGet httpget = new HttpGet(url);
		httpget.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		httpget.setHeader("Accept-Languange","zh-CN,zh;q=0.8");
		httpget.setHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.106 Safari/537.36");
		httpget.setHeader("Connection","keep-alive");
		httpget.setHeader("Host",url);
		httpget.setHeader("Upgrade-Insecure-Requests","1");
		httpget.setHeader("Accept-Encoding","gzip,deflate,sdch");
		CloseableHttpResponse response = httpClient.execute(httpget);
		HttpEntity entity = response.getEntity();
		if(entity != null){
			pageContent = EntityUtils.toString(entity);
			EntityUtils.consume(entity);
		}
		return pageContent;
	}
	
	public static String getRequestDirectly(String url) throws ClientProtocolException, IOException{
		String pageContent = "";
		HttpGet httpget = new HttpGet(url);
		CloseableHttpResponse response = httpClient.execute(httpget);
		HttpEntity entity = response.getEntity();
		if(entity != null){
			pageContent = EntityUtils.toString(entity);
			EntityUtils.consume(entity);
		}
		return pageContent;
	}
	
	public static String postRequest(String url) throws ClientProtocolException, IOException{
		HttpPost httpPost = new HttpPost(url);
		List<BasicNameValuePair> formParams = new ArrayList<BasicNameValuePair>();
		formParams.add(new BasicNameValuePair("type","house"));
		UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(formParams,"utf-8");
		httpPost.setEntity(uefEntity);
		CloseableHttpResponse response = httpClient.execute(httpPost);
		HttpEntity httpEntity = response.getEntity();
		if(httpEntity != null){
			return "";
		}
		String responseContent = EntityUtils.toString(httpEntity);
		return responseContent;
	}
	
	public static Set<String> getDomainName(String content){
		
		return null;
	}
	
	
	public void fileUploadRequest(String url,File file){
		HttpPost httpPost = new HttpPost(url);
	}
	
	
	public static void main(String[] args) {
		try {
			String content = HttpRequestUtil.getRequestDirectly("http://www.icbc.com.cn/icbc/");
			System.out.print(content);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

}
