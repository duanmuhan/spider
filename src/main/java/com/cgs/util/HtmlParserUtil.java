package com.cgs.util;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.NodeClassFilter;
import org.htmlparser.filters.OrFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import com.cgs.filter.TagNodeFilter;


public class HtmlParserUtil {

	public static Set<String> getContainedUrl(String url) throws ParserException{
		Set<String> links = new HashSet<String>();
		Parser parser = new Parser(url);
		parser.setEncoding("utf-8");
		NodeFilter frameFilter = new TagNodeFilter();
		OrFilter linkFilter = new OrFilter(new NodeClassFilter(LinkTag.class),frameFilter);
		NodeList list = parser.extractAllNodesThatMatch(linkFilter);
		for(int i=0; i<list.size(); i++){
			Node tag = list.elementAt(i);
			if(tag instanceof LinkTag){
				LinkTag link = (LinkTag)tag;
				String linkUrl = link.getLink();
				if(frameFilter.accept(tag)){
					links.add(linkUrl);
				}
			}
		}
		return links;
	}
	
	public static Set<String> getContainedUrlByContent(String content) throws ParserException{
		Set<String> links = new HashSet<String>();
		
		return links;
	}
	
	public static String getContainedWords(String url){
		String words = "";
		return words;
	}
	
	public static void main(String[] args) {
		try {
			Set<String> urlSet = HtmlParserUtil.getContainedUrl("http://bbs.tianya.cn/");
			Iterator<String> it = urlSet.iterator();
		} catch (ParserException e) {
			e.printStackTrace();
		}
	}
}
