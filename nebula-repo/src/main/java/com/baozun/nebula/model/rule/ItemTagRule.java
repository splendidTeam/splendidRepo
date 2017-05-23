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

package com.baozun.nebula.model.rule;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.OptimisticLockType;

import com.baozun.nebula.model.BaseModel;

/**
 * 商品标签规则实体类
 * 
 * @author - 项硕
 */
@Entity
@Table(name = "T_PD_TAG_RULE")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class ItemTagRule extends BaseModel implements TagRule {

	private static final long serialVersionUID = 6041045996264004146L;
	
	/** ‘0’代表‘全体’ */
	public static final String ALL_ITEM_ID = "0";
	
	/** 类型-商品 */
	public static final Integer TYPE_PRODUCT = 1;
	/** 类型-商品分类 */
	public static final Integer TYPE_CATEGORY = 2;
	/** 类型-自定义 */
	public static final Integer TYPE_CUSTOM = 3;
	
	/** 表达式-商品 */
	public static final String EXP_PRODUCT = "pid:in(?)";
	/** 表达式-商品分类 */
	public static final String EXP_CATEGORY = "cid:in(?)";
	/** 表达式-商品全场 */
	public static final String EXP_ALLPRODUCT = "call()";
	/** 表达式-自定义分类 */
	public static final String EXP_CUSTOM = "cctg:in(?)";
	
	/** 表达式前缀-商品 */
	public static final String EXP_PREFIX_PRODUCT = "pid";
	/** 表达式前缀-商品分类 */
	public static final String EXP_PREFIX_CATEGORY = "cid";
	/** 表达式前缀-商品全场 */
	public static final String EXP_PREFIX_ALLPRODUCT = "call";
	/** 表达式前缀-自定义分类 */
	public static final String EXP_PREFIX_CUSTOM = "cctg";
	
	/** PK */
	private Long id;

	/** 商品范围类型 */
	private Integer type;

	/** 商品组合名称 */
	private String name;

	/** 商品组合表达式 */
	private String expression;
	
	/** 商品组合表达式中文解释 */
	private String text;
	
	/** 生命周期 */
	private Integer lifecycle = 1;
	
	/** 创建人 */
	private Long createId;

	/** 创建时间 */
	private Date createTime;

	/** 店铺ID */
	private Long shopId;

	private Date version;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_PD_TAG_RULE",sequenceName = "S_T_PD_TAG_RULE",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_PD_TAG_RULE")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "TYPE")
    @Index(name = "IDX_PD_TAG_RULE_TYPE")
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Column(name = "NAME", unique = true)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "EXPRESSION", length = 2000)
	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	@Column(name = "TEXT", length = 2000)
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Column(name = "LIFECYCLE")
    @Index(name = "IDX_PD_TAG_RULE_LIFECYCLE")
	public Integer getLifecycle() {
		return lifecycle;
	}

	public void setLifecycle(Integer lifecycle) {
		this.lifecycle = lifecycle;
	}

	@Column(name = "CREATE_ID")
    @Index(name = "IDX_PD_TAG_RULE_CREATE_ID")
	public Long getCreateId() {
		return createId;
	}

	public void setCreateId(Long createId) {
		this.createId = createId;
	}

	@Column(name = "CREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "SHOP_ID")
    @Index(name = "IDX_PD_TAG_RULE_SHOP_ID")
	public Long getShopId() {
		return shopId;
	}

	public void setShopId(Long shopId) {
		this.shopId = shopId;
	}

	@Version
	@Column(name = "VERSION")
	public Date getVersion() {
		return version;
	}

	public void setVersion(Date version) {
		this.version = version;
	}
	 

}
