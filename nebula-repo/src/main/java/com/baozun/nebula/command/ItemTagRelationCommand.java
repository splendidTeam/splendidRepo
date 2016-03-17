package com.baozun.nebula.command;


public class ItemTagRelationCommand implements Command{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1607126536809267114L;
	
	/** 商品id */
	private Long				itemId;

	/**
	 * 商品标签名
	 */
	private String			    tagName;

	
	public Long getItemId(){
		return itemId;
	}

	
	public void setItemId(Long itemId){
		this.itemId = itemId;
	}


	
	public String getTagName(){
		return tagName;
	}


	
	public void setTagName(String tagName){
		this.tagName = tagName;
	}

}
