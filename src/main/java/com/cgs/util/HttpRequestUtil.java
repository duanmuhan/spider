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

	public static String getRequestWithRefer(String url, String refer) throws IOException {
		String pageContent = "";
		HttpGet httpget = new HttpGet(url);
		httpget.setHeader("Accept", " */*");
		httpget.setHeader("Cache-Control"," no-cache");
		httpget.setHeader("Accept-Encoding","gzip, deflate");
		httpget.setHeader("Accept-Languange","zh-CN,zh;q=0.9");
		httpget.setHeader("User-Agent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/81.0.4044.138 S");
		httpget.setHeader("Connection","keep-alive");
		httpget.setHeader("Cookie"," yfx_c_g_u_id_10000042=_ck20050714094311525767701141514; yfx_mr_10000042=%3A%3Amarket_type_free_search%3A%3A%3A%3Abaidu%3A%3A%3A%3A%3A%3A%3A%3Awww.baidu.com%3A%3A%3A%3Apmf_from_free_search; yfx_mr_f_10000042=%3A%3Amarket_type_free_search%3A%3A%3A%3Abaidu%3A%3A%3A%3A%3A%3A%3A%3Awww.baidu.com%3A%3A%3A%3Apmf_from_free_search; yfx_key_10000042=; VISITED_COMPANY_CODE=%5B%22600031%22%2C%22603999%22%5D; VISITED_STOCK_CODE=%5B%22600031%22%2C%22603999%22%5D; seecookie=%5B600031%5D%3A%u4E09%u4E00%u91CD%u5DE5%2C%5B603999%5D%3A%u8BFB%u8005%u4F20%u5A92; JSESSIONID=DB8717FE9293D47889619C18BD2DA723; yfx_f_l_v_t_10000042=f_t_1588831783060__r_t_1588905742767__v_t_1588911635840__r_c_1; VISITED_MENU=%5B%228504%22%2C%229062%22%2C%229055%22%2C%228451%22%2C%228528%22%5D");
		httpget.setHeader("Host"," query.sse.com.cn");
		httpget.setHeader("Pragma"," no-cache");
		httpget.setHeader("Referer",refer);
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
}
