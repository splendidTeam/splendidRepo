/**
 * Copyright (c) 2015 Baozun All Rights Reserved.
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
 */
package com.baozun.nebula.model.product;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.OptimisticLockType;

/**
 * @Title: ItemColorValueRef 
 * @date: 2015年12月23日  
 * @Description: 商品筛选色颜色对照关系表 此表独立
 * @author Arvin.Chang
 */

@Entity
@Table(name = "T_PD_ITEM_COLOR_VALUE_REFERENCE")
@org.hibernate.annotations.Entity(optimisticLock=OptimisticLockType.VERSION)
public class ItemColorValueRef implements Serializable {

	/**
	 * serialVersionUID:
	 */
	private static final long serialVersionUID = 5500436822792248053L;
	
	
	/**  PK 序列ID  无外键关系 */
	private Long  id;
	
	/** 筛选色 名称*/
	private String filterColor;
	
	/** 筛选色 色值*/
	private String filterColorValue;

	/** 商品色 名称*/
	private String itemColor;
	
	/** 商品色 色值*/
	private String itemtColorValue;
	
	/** 创建时间. */
	private Date createTime;
	
	/**
	 * Creates a new instance of ItemColorRef.
	 */
	public ItemColorValueRef(){
		
	}
	
	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_PD_ITEM_COLOR_VALUE_REFERENCE", sequenceName = "S_T_PD_ITEM_COLOR_VALUE_REFERENCE", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_PD_ITEM_COLOR_VALUE_REFERENCE")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "FILTER_COLOR")
	public String getFilterColor() {
		return filterColor;
	}

	public void setFilterColor(String filterColor) {
		this.filterColor = filterColor;
	}
	
	@Column(name = "FILTER_COLOR_VALUE")
	public String getFilterColorValue() {
		return filterColorValue;
	}

	public void setFilterColorValue(String filterColorValue) {
		this.filterColorValue = filterColorValue;
	}
	
	@Column(name = "ITEM_COLOR")
	public String getItemColor() {
		return itemColor;
	}

	public void setItemColor(String itemColor) {
		this.itemColor = itemColor;
	}
	
	@Column(name = "ITEM_COLOR_VALUE")
	public String getItemtColorValue() {
		return itemtColorValue;
	}

	public void setItemtColorValue(String itemtColorValue) {
		this.itemtColorValue = itemtColorValue;
	}
	
	@Column(name = "CREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
