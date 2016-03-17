package com.baozun.nebula.solr.command;

import com.baozun.nebula.command.Command;

public class ItemRateForSolrCommand implements Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3669208792600607187L;
	
	private Long itemId;
	
	private Float count;
	
	private Float sum;

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public Float getCount() {
		return count;
	}

	public void setCount(Float count) {
		this.count = count;
	}

	public Float getSum() {
		return sum;
	}

	public void setSum(Float sum) {
		this.sum = sum;
	}
	
	

}
