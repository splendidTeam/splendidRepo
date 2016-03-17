/**
 * Copyright (c) 2012 Baozun All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Baozun.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Baozun.
 *
 * BAOZUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. BAOZUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.command;

/**
 * @author jun.lu
 */
public class ItemColorCommand implements Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2915837648032691747L;

	/** 
	 *	系统颜色Id
	 */
	private Long sysId;
	
	/** 
	 *	商品颜色属性值
	 */
	private Long itemPropertyValueId;

	/**
	 * 系统颜色名称
	 */
	private String name;
	
	/**
	 * 序列号
	 */
	private Integer sortNo;
	
	/**
	 * 
	 */
	private Long itemId;

	public Long getSysId() {
		return sysId;
	}

	public void setSysId(Long sysId) {
		this.sysId = sysId;
	}

	public Long getItemPropertyValueId() {
		return itemPropertyValueId;
	}

	public void setItemPropertyValueId(Long itemPropertyValueId) {
		this.itemPropertyValueId = itemPropertyValueId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSortNo() {
		return sortNo;
	}

	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}


}
