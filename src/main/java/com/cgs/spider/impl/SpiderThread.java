package com.cgs.spider.impl;

import java.util.Iterator;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedTransferQueue;

import org.htmlparser.util.ParserException;

import com.cgs.util.HtmlParserUtil;

public class SpiderThread implements Runnable{
	
	private Queue<String> localUrlQueue = new LinkedTransferQueue<>();

	public SpiderThread(String startUrl){
		localUrlQueue.add(startUrl);
	}
	
	@SuppressWarnings("static-access")
	public void run() {
		try {
			while(!localUrlQueue.isEmpty()){
				String url = localUrlQueue.poll();
				getUrlQueue(url);
				System.out.println("url: " + url);
				Thread.sleep(100);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}
	
	public String testFunction(){
		return "";
	}
	
	private void getUrlQueue(String url) throws ParserException, InterruptedException{
		if(url != null && !url.equals("")){
			Set<String> urlSet = HtmlParserUtil.getContainedUrl(url);
			Iterator<String> iterator = urlSet.iterator();
			while(iterator.hasNext()){
				String crawUrl = iterator.next();
				//DataEntity.getQueue().put(crawUrl);
				localUrlQueue.add(crawUrl);
			}
		}
	}
	
	public static void main(String[] args) {
		ExecutorService fixedThreadPool = Executors.newFixedThreadPool(1);
		SpiderThread thread = new SpiderThread("http://www.baidu.com/");
		fixedThreadPool.execute(thread);
	}
}
