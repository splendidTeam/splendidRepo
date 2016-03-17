package com.baozun.nebula.solr.command;

import java.io.Serializable;

public class SuggestDetailCommand implements Serializable {
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1525967344986535117L;

	private String title;

	private Long count;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}


}
