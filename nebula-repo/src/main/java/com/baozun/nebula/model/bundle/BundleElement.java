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
 * 捆绑类商品成员，用以表征捆绑类商品的组合关系
 * 
 * <ul>
 * 		<li>每一个捆绑类的商品（Bundle）都至少由两个以上的成员（BundleElement）组成</li>
 * 		<li>每一个成员可以是一个单品或一个款（目前是依据style，未来可能会由父商品的概念来替换）</li>
 * 		<li>一个款中可能包含多个单品，但对于捆绑类商品来说，它们仅是其中的一个成员</li>
 * 		<li>
 * 			由于各个商城针对商品的定义粒度不同，所以衍生出如下的几种组合（第一个为主卖品）：
 * 			<ol>
 * 				<li>n个单品</li>
 * 				<li>1个单品+n个款</li>
 * 				<li>n个款</li>
 * 				<li>1个款+n个单品</li>
 * 			</ol>
 * 			参考{@link com.baozun.nebula.model.bundle.Bundle#bundleType}
 * 		</li>
 * </ul>
 * 
 * @author yue.ch
 *
 */
@Entity
@Table(name = "t_pd_bundle_element")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class BundleElement extends BaseModel {

	private static final long serialVersionUID = -1119942699607530274L;

	/**
	 * PK
	 */
	private Long id;
	
	/**
	 * 捆绑类商品扩展信息ID
	 */
	private Long bundleId;
	
	/**
	 * 捆绑类商品中每个具体个体商品的ID（现阶段仅为预留字段，无实际意义。）
	 */
	private Long itemId;
	
	/**
	 * 捆绑装采用“固定价/一口价”价格模式设置的商品的价格，
	 * 如果非此价格模式，则此属性无意义。
	 */
	private BigDecimal salesPrice;
	
	/**
	 * 主卖品标志，一个捆绑商品仅以主卖品为依据加载捆绑成员信息<br/>
	 * 注意这里是主卖品而不是主商品的概念，是因为主卖品可以是一个具体的商品，也可以是以款汇聚的多个商品
	 */
	private Boolean isMainElement;
	
	/**
	 * 排序因子，决定了捆绑类商品中除主卖品外其它成员的排列顺序
	 */
	private Integer sortNo;
	
	/**
	 * version
	 */
	private Date version;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_PD_BUNDLE_ELEMENT",sequenceName = "S_T_PD_BUNDLE_ELEMENT",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_PD_BUNDLE_ELEMENT")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "BUNDLE_ID")
	@Index(name = "IDX_BUNDLE_ELEMENT_BUNDLE_ID")
	public Long getBundleId() {
		return bundleId;
	}

	public void setBundleId(Long bundleId) {
		this.bundleId = bundleId;
	}
	
	@Column(name = "ITEM_ID")
	@Index(name = "IDX_BUNDLE_ELEMENT_ITEM_ID")
	public Long getItemId() {
		return itemId;
	}
	
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	@Column(name = "SALES_PRICE")
	public BigDecimal getSalesPrice() {
		return salesPrice;
	}

	public void setSalesPrice(BigDecimal salesPrice) {
		this.salesPrice = salesPrice;
	}
	@Column(name = "IS_MAIN_ELEMENT")
	public Boolean getIsMainElement() {
		return isMainElement;
	}
	
	public void setIsMainElement(Boolean isMainElement) {
		this.isMainElement = isMainElement;
	}

	@Column(name = "SORT_NO")
	public Integer getSortNo() {
		return sortNo;
	}

	public void setSortNo(Integer sortNo) {
		this.sortNo = sortNo;
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
