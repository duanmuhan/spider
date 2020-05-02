package com.cgs.filter;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;

public class TagNodeFilter implements NodeFilter{

	
	public boolean accept(Node node) {
		// TODO Auto-generated method stub
		if(node.getText().startsWith("frame src=")){
			return true;
		}
		if(node.getText().contains("a href") && node.getText().contains("http")){
			return true;
		}
		return false;
	}

}
