/**
 * 
 */
package com.baozun.nebula.command.sales;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import com.baozun.nebula.command.Command;

/**
 * @author xianze.zhang
 * @creattime 2013-11-27
 */
public class ShoppingCartLineCommand implements Command{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -423227208998561781L;

	/** The list price. */
	private BigDecimal		listPrice;

	/** extentioncode */
	private String			extentionCode;

	private Long			itemId;

	/** 商品编码 */
	private String			code;

	/** 商品名称 */
	private String			name;

	/** 商品图片 */
	private String			pic;

	/** The add time. */
	private Long			addTime;

	// 原单价
	/** The original unit price. */
	private BigDecimal		originalUnitPrice;

	// 活动单价
	/** The activity unit price. */
	private BigDecimal		activityUnitPrice;

	// 数量
	/** The quantity. */
	private Integer			quantity;

	// 库存可用量
	/** The inv available qty. */
	private Integer			invAvailableQty;

	// 赠品列表,行赠品列表
	private List<GiftBean>	giftList	= new ArrayList<GiftBean>();

	/**
	 *  是否选中
	 *  作用：
	 *  1、用户可以选择一部分商品进行提交订单
	 *  2、当是赠品行的时候可以用于标识用户选中了哪些赠品
	 */
	private Boolean			isChecked;

	// 活动对象，封装了所有活动信息
	// private SalesActivityBean activityBean = new SalesActivityBean();

	/**
	 * Cal original total amount.
	 * 
	 * @return the big decimal
	 */
	public BigDecimal calOriginalTotalAmount(){
		BigDecimal originalTotalAmount = this.originalUnitPrice.multiply(new BigDecimal(this.quantity)).setScale(2, RoundingMode.HALF_UP);
		return originalTotalAmount.doubleValue() > 0 ? originalTotalAmount : BigDecimal.ZERO;
	}

	/**
	 * Cal activity total amount.
	 * 
	 * @return the big decimal
	 */
	public BigDecimal calActivityTotalAmount(){
		if (activityUnitPrice == null || quantity == null)
			throw new RuntimeException("ActivityUntiPrice & quantity should be set at this moment");
		return this.activityUnitPrice.multiply(new BigDecimal(this.quantity));
	}

	public BigDecimal getListPrice(){
		return listPrice;
	}

	public void setListPrice(BigDecimal listPrice){
		this.listPrice = listPrice;
	}

	public String getExtentionCode(){
		return extentionCode;
	}

	public void setExtentionCode(String extentionCode){
		this.extentionCode = extentionCode;
	}

	public String getCode(){
		return code;
	}

	public void setCode(String code){
		this.code = code;
	}

	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getPic(){
		return pic;
	}

	public void setPic(String pic){
		this.pic = pic;
	}

	public Long getAddTime(){
		return addTime;
	}

	public void setAddTime(Long addTime){
		this.addTime = addTime;
	}

	public BigDecimal getOriginalUnitPrice(){
		return originalUnitPrice;
	}

	public void setOriginalUnitPrice(BigDecimal originalUnitPrice){
		this.originalUnitPrice = originalUnitPrice;
	}

	public BigDecimal getActivityUnitPrice(){
		return activityUnitPrice;
	}

	public void setActivityUnitPrice(BigDecimal activityUnitPrice){
		this.activityUnitPrice = activityUnitPrice;
	}

	public Integer getQuantity(){
		return quantity;
	}

	public void setQuantity(Integer quantity){
		this.quantity = quantity;
	}

	public Integer getInvAvailableQty(){
		return invAvailableQty;
	}

	public void setInvAvailableQty(Integer invAvailableQty){
		this.invAvailableQty = invAvailableQty;
	}

	public List<GiftBean> getGiftList(){
		return giftList;
	}

	public void setGiftList(List<GiftBean> giftList){
		this.giftList = giftList;
	}

	public Boolean getIsChecked(){
		return isChecked;
	}

	public void setIsChecked(Boolean isChecked){
		this.isChecked = isChecked;
	}

	public Long getItemId(){
		return itemId;
	}

	public void setItemId(Long itemId){
		this.itemId = itemId;
	}

}
