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

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.OptimisticLockType;

import com.baozun.nebula.model.BaseModel;

/**
 * 捆绑类商品扩展信息
 * 
 * <ul>
 * 		<li>捆绑类商品（Bundle）是一种特殊的商品，其由多个普通商品（Item）组合而成，以特定Sku组合的形式进行销售</li>
 * 		<li>捆绑类商品没有属性定义</li>
 * 		<li>捆绑类商品同样具有普通商品的某些特质，比如标题、描述、图片、seo相关信息等等</li>
 * 		<li>捆绑类商品既可以具有独立的库存，也可以共享普通商品的库存</li>
 * 		<li>捆绑类商品具有灵活的价格体系，既可以沿用普通商品的价格，也可以独立设置具体的商品或者sku的价格</li>
 * </ul>
 * 
 * @author yue.ch
 *
 */
@Entity
@Table(name = "t_pd_bundle", uniqueConstraints = { @UniqueConstraint(columnNames = { "ITEM_ID" }) })
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class Bundle extends BaseModel {

	private static final long serialVersionUID = 7037698425918949039L;
	
	/** 价格设置方式：按实际价格 */
	public static final int PRICE_TYPE_REALPRICE = 1;
	/** 价格设置方式：固定价/一口价 */
	public static final int PRICE_TYPE_FIXEDPRICE = 2;
	/** 价格设置方式：定制价格 */
	public static final int PRICE_TYPE_CUSTOMPRICE = 3;
	
	/** 捆绑装类型：商品对商品 */
	@Deprecated
	public static final int BUNDLE_TYPE_ITEMBYITEM = 1;
	/** 捆绑装类型：商品对款 */
	@Deprecated
	public static final int BUNDLE_TYPE_ITEMBYSTYLE = 2;
	/** 捆绑装类型：款对款 */
	@Deprecated
	public static final int BUNDLE_TYPE_STYLEBYSTYLE = 3;
	/** 捆绑装类型：款对商品 */
	@Deprecated
	public static final int BUNDLE_TYPE_STYLEBYITEM = 4;

	/**
	 * PK
	 */
	private Long id;
	
	/**
	 * 商品ID，捆绑装本身是一种特殊的商品
	 */
	private Long itemId;
	
	/**
	 * 价格设置方式
	 * 
	 * <ol>
	 * <li>按实际价格</li>
	 * <li>固定价/一口价</li>
	 * <li>定制价格</li>
	 * </ol>
	 * 
	 * 固定价与定制化价格的区别在于，固定价是针对Item的，定制价是针对Sku的。<br/>
	 * 在取价格的时候，固定价取BundleItem对象的salesPrice属性值；而定制价则是取BundleSku对象的salesPrice属性值。
	 */
	private Integer priceType;
	
	/**
	 * 捆绑装库存
	 * 
	 * <li>捆绑装库存是一个手工维护的逻辑库存；</li>
	 * <li>如果为空，则可用库存以捆绑装单品的实际可用库存为准。</li>
	 */
	private Integer availableQty;
	
	/**
	 * 是否同步扣减单品库存
	 * 
	 * <li>这个标志当且仅当捆绑装库存有值时才生效；</li>
	 * <li>如果为真，则说明当扣减捆绑装数量的同时，需要同步扣减单品的可用库存；</li>
	 * <li>如果为假，则扣减操作仅发生在捆绑装可用库存上，不影响单品的可用库存。</li>
	 */
	private Boolean syncWithInv;
	
	/**
	 * 捆绑装类型
	 * 
	 * <ol>
	 * <li>商品对商品，通常用于针对单品主卖品配置捆绑装，捆绑装中搭配的也是单品。</li>
	 * <li>商品对款，通常用于针对单品主卖品配置捆绑装，捆绑装中搭配的是一款或多款商品。</li>
	 * <li>款对款，通常用于针对某一款商品主卖品配置捆绑装，捆绑装中搭配的是一款或多款商品。</li>
	 * <li>款对商品，通常用于针对某一款商品主卖品配置捆绑装，捆绑装中搭配的是单品。</li>
	 * </ol>
	 */
	@Deprecated
	private Integer bundleType;
	
	/**
	 * 创建时间
	 */
	private Date createTime;
	
	/**
	 * 更新时间
	 */
	private Date modifyTime;

	/**
	 * version
	 */
	private Date version;
	
	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_PD_BUNDLE",sequenceName = "S_T_PD_BUNDLE",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_PD_BUNDLE")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "ITEM_ID")
	@Index(name = "IDX_BUNDLE_ITEM_ID")
	public Long getItemId(){
		return itemId;
	}
	
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	@Column(name = "PRICE_TYPE")
	public Integer getPriceType() {
		return priceType;
	}

	public void setPriceType(Integer priceType) {
		this.priceType = priceType;
	}

	@Column(name = "AVAILABLE_QTY")
	public Integer getAvailableQty() {
		return availableQty;
	}

	public void setAvailableQty(Integer availableQty) {
		this.availableQty = availableQty;
	}

	@Column(name = "SYNC_WITH_INV")
	public Boolean getSyncWithInv() {
		return syncWithInv;
	}

	public void setSyncWithInv(Boolean syncWithInv) {
		this.syncWithInv = syncWithInv;
	}

	@Column(name = "BUNDLE_TYPE")
	@Deprecated
	public Integer getBundleType() {
		return bundleType;
	}

	@Deprecated
	public void setBundleType(Integer bundleType) {
		this.bundleType = bundleType;
	}

	@Column(name = "CREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "MODIFY_TIME")
	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
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
