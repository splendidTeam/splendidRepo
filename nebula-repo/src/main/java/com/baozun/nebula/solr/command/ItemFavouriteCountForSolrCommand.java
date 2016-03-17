package com.baozun.nebula.solr.command;

import com.baozun.nebula.command.Command;

public class ItemFavouriteCountForSolrCommand implements Command {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5503383181402191260L;

	private Long itemId;
	
	private Integer count;

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

}
