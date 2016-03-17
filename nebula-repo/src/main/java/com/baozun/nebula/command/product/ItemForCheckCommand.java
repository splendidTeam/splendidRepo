package com.baozun.nebula.command.product;

import java.util.List;

import com.baozun.nebula.command.Command;

public class ItemForCheckCommand implements Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7251490659119406463L;
	
	private Long itemId;
	
	private List<Long> categoryIds;

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public List<Long> getCategoryIds() {
		return categoryIds;
	}

	public void setCategoryIds(List<Long> categoryIds) {
		this.categoryIds = categoryIds;
	}
	
	

}
