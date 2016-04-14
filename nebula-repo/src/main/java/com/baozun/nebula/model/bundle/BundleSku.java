/**
 
* Copyright (c) 2014 Baozun All Rights Reserved.
 
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
package com.baozun.nebula.model.bundle;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.OptimisticLockType;

import com.baozun.nebula.model.BaseModel;

/**
 * 捆绑类商品的单品sku信息
 * 
 * @author yue.ch
 *
 */
@Entity
@Table(name = "t_pd_bundle_sku")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class BundleSku extends BaseModel {

	private static final long serialVersionUID = 9052499209617339855L;
	
	/**
	 * PK
	 */
	private Long id;
	
	/**
	 * 捆绑类商品成员ID
	 */
	private Long bundleElementId;
	
	/**
	 * 捆绑类商品扩展信息ID
	 */
	private Long bundleId;
	
	/**
	 * sku id
	 */
	private Long skuId;
	
	/**
	 * 商品ID
	 */
	private Long itemId;
	
	/**
	 * 款号
	 */
	private String style;
	
	/**
	 * 捆绑装采用“定制价格”价格模式设置的sku的价格，
	 * 如果非此价格模式，则此属性无意义。
	 */
	private BigDecimal salesPrice;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_PD_BUNDLE_SKU",sequenceName = "S_T_PD_BUNDLE_SKU",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_PD_BUNDLE_SKU")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "BUNDLE_ELEMENT_ID")
	@Index(name = "IDX_BUNDLE_SKU_BUNDLE_ELEMENT_ID")
	public Long getBundleElementId() {
		return bundleElementId;
	}

	public void setBundleElementId(Long bundleElementId) {
		this.bundleElementId = bundleElementId;
	}
	
	@Column(name = "BUNDLE_ID")
	public Long getBundleId(){
		return bundleId;
	}
	
	public void setBundleId(Long bundleId) {
		this.bundleId = bundleId;
	}

	@Column(name = "SKU_ID")
	@Index(name = "IDX_BUNDLE_SKU_SKU_ID")
	public Long getSkuId() {
		return skuId;
	}

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}

	@Column(name = "ITEM_ID")
	@Index(name = "IDX_BUNDLE_SKU_ITEM_ID")
	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	@Column(name = "STYLE")
	@Index(name = "IDX_BUNDLE_SKU_STYLE")
	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	@Column(name = "SALES_PRICE")
	public BigDecimal getSalesPrice() {
		return salesPrice;
	}

	public void setSalesPrice(BigDecimal salesPrice) {
		this.salesPrice = salesPrice;
	}
}
