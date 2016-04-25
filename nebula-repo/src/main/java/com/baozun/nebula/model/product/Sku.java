/**
 * Copyright (c) 2010 Jumbomart All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Jumbomart.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Jumbo.
 *
 * JUMBOMART MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. JUMBOMART SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.model.product;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.OptimisticLockType;

import com.baozun.nebula.model.BaseModel;

/**
 * Sku
 * 
 * @author dianchao.song
 */
@Entity
@Table(name = "t_pd_sku")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class Sku extends BaseModel {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5833258263181441457L;

	public static final Integer LIFE_CYCLE_ENABLE = 1;

	/** PK. */
	private Long id;

	/**
	 * 所属商品
	 */
	private Long itemId;

	/**
	 * 属性值，用于表征商品和SKU基于销售属性的划分对照，其标准格式目前定义为：[([商品属性值id])([,商品属性值id])], 比如[19,23]
	 * 这里的商品属性值id为ItemProperties实体的id
	 */
	private String properties;

	/**
	 * @deprecated sku所对应的销售属性的中文名字串，格式如：pid1:vid1:pid_name1:vid_name1;pid2:vid2
	 *             :pid_name2: vid_name2……
	 */
	private String propertiesName;

	/** 创建时间. */
	private Date createTime;

	/** 修改时间 */
	private Date modifyTime;

	/** version. */
	private Date version;

	/**
	 * 外部编码
	 */
	private String outid;

	/**
	 * 生命周期
	 */
	private Integer lifecycle;

	/**
	 * 销售价
	 */
	private BigDecimal salePrice;

	/**
	 * 吊牌价(原单价)
	 */
	private BigDecimal listPrice;

	/**
	 * 1 商品已上架 0 商品未上架
	 */
	private String state;
	/**
	 * 商品条码
	 * by D.C 2016/4/12
	 */
	private String barcode;

	/**
	 * Gets the pK.
	 * 
	 * @return the pK
	 */
	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_PD_SKU", sequenceName = "S_T_PD_SKU", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_PD_SKU")
	public Long getId() {
		return id;
	}

	/**
	 * Sets the pK.
	 * 
	 * @param id
	 *            the new pK
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets the version.
	 * 
	 * @return the version
	 */
	@Version
	@Column(name = "VERSION")
	public Date getVersion() {
		return version;
	}

	/**
	 * Sets the version.
	 * 
	 * @param version
	 */
	public void setVersion(Date version) {
		this.version = version;
	}

	/**
	 * Gets the 创建时间.
	 * 
	 * @return the createTime
	 */
	@Column(name = "CREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * Sets the 创建时间.
	 * 
	 * @param createTime
	 *            the createTime to set
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	@Column(name = "MODIFY_TIME")
	public Date getModifyTime() {
		return modifyTime;
	}

	public void setLifecycle(Integer lifecycle) {
		this.lifecycle = lifecycle;
	}

	@Column(name = "LIFECYCLE")
	public Integer getLifecycle() {
		return lifecycle;
	}

	@Column(name = "ITEM_ID")
	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	@Column(name = "PROPERTIES")
	public String getProperties() {
		return properties;
	}

	public void setProperties(String properties) {
		this.properties = properties;
	}

	@Column(name = "PROPERTIES_NAME")
	public String getPropertiesName() {
		return propertiesName;
	}

	public void setPropertiesName(String propertiesName) {
		this.propertiesName = propertiesName;
	}

	@Column(name = "OUT_ID")
	public String getOutid() {
		return outid;
	}

	public void setOutid(String outid) {
		this.outid = outid;
	}

	@Column(name = "SALE_PRICE")
	public BigDecimal getSalePrice() {
		salePrice = salePrice.setScale(2, BigDecimal.ROUND_HALF_UP);
		return salePrice;
	}

	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}

	@Column(name = "LIST_PRICE")
	public BigDecimal getListPrice() {
		if (listPrice != null) {
			listPrice = listPrice.setScale(2, BigDecimal.ROUND_HALF_UP);
		}

		return listPrice;
	}

	public void setListPrice(BigDecimal listPrice) {
		this.listPrice = listPrice;
	}

	@Column(name = "STATE")
	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Column(name = "BARCODE")
	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

}
