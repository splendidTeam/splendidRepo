package com.baozun.nebula.solr.utils;

import java.io.Serializable;

public class SolrOrderSort implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8188752536464297137L;
	
	public static final String ASC = "asc";
	public static final String DESC = "desc";
	
	public SolrOrderSort(String field,String type){
		this.setField(field);
		this.setType(type);
	}
	
	/**
	 * 排序名称
	 */
	private String field;
	
	/**
	 * 排序规则
	 */
	private String type;

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		if(SolrOrderSort.ASC.equalsIgnoreCase(type) || SolrOrderSort.DESC.equalsIgnoreCase(type)){
			this.type = type;
		}else{
			throw new IllegalArgumentException("Wrong sort type definition, only asc or desc are supported.");
		}
	}
	
	
}
