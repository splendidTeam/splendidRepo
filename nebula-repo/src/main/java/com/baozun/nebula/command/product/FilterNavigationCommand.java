package com.baozun.nebula.command.product;

import java.io.Serializable;

import com.feilong.core.Validator;

/**
 * navigationFilter 中使用的NavigationCommand
 * @author jumbo
 *
 */
public class FilterNavigationCommand implements Serializable, Comparable<FilterNavigationCommand>{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5530985823633908063L;

	/** 导航id*/
	Long 	navId;
	
	/** 商品集合id*/
	Long 	collectionId;
	
	/**导航uri，从navigation url解析得来*/
	String	uri;
	
	/**导航 参数 从navigation url解析得来*/
	String	params;
	
	public FilterNavigationCommand() {
		// TODO Auto-generated constructor stub
	}
	
	public FilterNavigationCommand(Long navId, Long collectionId, String uri, String params) {
		super();
		this.navId = navId;
		this.collectionId = collectionId;
		this.uri = uri;
		this.params = params;
	}


	public Long getNavId() {
		return navId;
	}

	public Long getCollectionId() {
		return collectionId;
	}

	public void setNavId(Long navId) {
		this.navId = navId;
	}

	public void setCollectionId(Long collectionId) {
		this.collectionId = collectionId;
	}




	public String getUri() {
		return uri;
	}




	public String getParams() {
		return params;
	}




	public void setUri(String uri) {
		this.uri = uri;
	}
	
	public int compareTo(FilterNavigationCommand obj) {  
		if(Validator.isNullOrEmpty(obj.getParams())){
			return 1;
		}
		
		if(Validator.isNullOrEmpty(this.getParams())){
			return -1;
		}
		
		return this.getParams().split("&").length - obj.getParams().split("&").length;
		
    }  
}
