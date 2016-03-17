package com.baozun.nebula.command;

public class ItemCategoryCommand implements Command{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1607126536809267114L;

	/** 商品id */
	private Long				itemId;

	/**
	 * 商品分类名
	 */
	private String				categoryName;
	
	/**
	 * 商品分类编码
	 */
	private String 				categoryCode;

	private Long				categoryId;
	
	/**
	 * 是否默认分类
	 */
	private Boolean				isDefault;
	

	public Long getItemId(){
		return itemId;
	}

	public void setItemId(Long itemId){
		this.itemId = itemId;
	}

	public String getCategoryName(){
		return categoryName;
	}

	public void setCategoryName(String categoryName){
		this.categoryName = categoryName;
	}

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public Long getCategoryId(){
		return categoryId;
	}

	
	public void setCategoryId(Long categoryId){
		this.categoryId = categoryId;
	}
	
	public Boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}
}
