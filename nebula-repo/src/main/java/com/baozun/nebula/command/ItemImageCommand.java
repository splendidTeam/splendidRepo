package com.baozun.nebula.command;

import java.util.List;

import com.baozun.nebula.model.product.ItemImage;
import com.baozun.nebula.model.product.ItemImageLang;

public class ItemImageCommand implements Command, Comparable<ItemImageCommand> {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 5227991923214410390L;

	private Long id;
	/**
	 * 商品
	 */
	private Long				itemId;

	/**
	 * 
	 */
	private Long				itemProId;
	/**
	 * 图片url
	 */
	private String				picUrl;

	/**
	 * 颜色
	 */
	private Long				color;

	/**
	 * 序列号
	 */
	private Integer				position;

	/**
	 * 颜色排序
	 * 
	 * @return
	 */
	private Integer				sort_no;

	/**
	 * 列表页上的 sku 小图url
	 */
	private String				smallPicUrl;

	/**
	 * 列表页上的sku 中图url
	 */
	private String				middlePicUrl;

	private List<ItemImage>		itemIamgeList;
	
	private List<ItemImageLang>	itemImageLangs;

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public Integer getPosition() {
		return position;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public Long getColor() {
		return color;
	}

	public void setColor(Long color) {
		this.color = color;
	}

	public Integer getSort_no() {
		return sort_no;
	}

	public void setSort_no(Integer sort_no) {
		this.sort_no = sort_no;
	}

	public Long getItemProId() {
		return itemProId;
	}

	public void setItemProId(Long itemProId) {
		this.itemProId = itemProId;
	}

	/**
	 * @return the smallPicUrl
	 */
	public String getSmallPicUrl() {
		return smallPicUrl;
	}

	/**
	 * @param smallPicUrl
	 *            the smallPicUrl to set
	 */
	public void setSmallPicUrl(String smallPicUrl) {
		this.smallPicUrl = smallPicUrl;
	}

	/**
	 * @return the middlePicUrl
	 */
	public String getMiddlePicUrl() {
		return middlePicUrl;
	}

	/**
	 * @param middlePicUrl
	 *            the middlePicUrl to set
	 */
	public void setMiddlePicUrl(String middlePicUrl) {
		this.middlePicUrl = middlePicUrl;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(ItemImageCommand o) {

		return this.getSort_no().compareTo(o.getSort_no());
	}

	public List<ItemImage> getItemIamgeList() {
		return itemIamgeList;
	}

	public void setItemIamgeList(List<ItemImage> itemIamgeList) {
		this.itemIamgeList = itemIamgeList;
	}

	public List<ItemImageLang> getItemImageLangs() {
		return itemImageLangs;
	}

	public void setItemImageLangs(List<ItemImageLang> itemImageLangs) {
		this.itemImageLangs = itemImageLangs;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	
}
