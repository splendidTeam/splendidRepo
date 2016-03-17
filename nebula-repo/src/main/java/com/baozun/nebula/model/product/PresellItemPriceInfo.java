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

import org.hibernate.annotations.OptimisticLockType;

import com.baozun.nebula.model.BaseModel;

/**
 * 预售商品付款价格信息
 * 
 * @author dianchao.song
 */
@Entity
@Table(name = "t_sto_presell_item_price_Info")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class PresellItemPriceInfo extends BaseModel{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	/** primary key Id. */
	private Long				id;

	/** 商品编码 */
	private String				itemCode;

	/** 商品ItemId */
	private Long				itemId;

	/** 商家编码 */
	private String				extentionCode;

	/** 订金 */
	private BigDecimal			earnest;

	/** 尾款 */
	private BigDecimal			balance;

	/** 全款 */
	private BigDecimal			fullmoney;

	/** 创建时间 */
	private Date				createTime;

	/** 最近修改时间 */
	private Date				modifyTime;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_STO_PRESELL_ITEM_PRICE_INFO",sequenceName = "S_T_STO_PRESELL_ITEM_PRICE_INFO",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_STO_PRESELL_ITEM_PRICE_INFO")
	public Long getId(){
		return id;
	}

	public void setId(Long id){
		this.id = id;
	}

	@Column(name = "ITEMCODE")
	public String getItemCode(){
		return itemCode;
	}

	public void setItemCode(String itemCode){
		this.itemCode = itemCode;
	}

	@Column(name = "EXTENTIONCODE")
	public String getExtentionCode(){
		return extentionCode;
	}

	public void setExtentionCode(String extentionCode){
		this.extentionCode = extentionCode;
	}

	@Column(name = "EARNEST")
	public BigDecimal getEarnest(){
		return earnest;
	}

	public void setEarnest(BigDecimal earnest){
		this.earnest = earnest;
	}

	@Column(name = "BALANCE")
	public BigDecimal getBalance(){
		return balance;
	}

	public void setBalance(BigDecimal balance){
		this.balance = balance;
	}

	@Column(name = "FULLMONEY")
	public BigDecimal getFullmoney(){
		return fullmoney;
	}

	public void setFullmoney(BigDecimal fullmoney){
		this.fullmoney = fullmoney;
	}

	@Column(name = "CREATETIME")
	public Date getCreateTime(){
		return createTime;
	}

	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}

	@Column(name = "MODIFYTIME")
	public Date getModifyTime(){
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime){
		this.modifyTime = modifyTime;
	}

	@Column(name = "ITEMID")
	public Long getItemId(){
		return itemId;
	}

	public void setItemId(Long itemId){
		this.itemId = itemId;
	}

}
