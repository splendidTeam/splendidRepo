package com.baozun.nebula.command;

public class ItemTagCommand implements Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5227991923214410390L;

	/**
	 * 商品
	 */
	private Long itemId;
	/**
	 * 标签名
	 */
	private String name;

	/**
	 * 类型名称
	 */
	private Long tag_id;

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getTag_id() {
		return tag_id;
	}

	public void setTag_id(Long tag_id) {
		this.tag_id = tag_id;
	}

}
