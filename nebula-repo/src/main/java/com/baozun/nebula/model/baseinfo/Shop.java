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
package com.baozun.nebula.model.baseinfo;

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
 * 店铺,数据定义来源于Organization
 * 
 * @author 宋佃超
 */
@Entity
@Table(name = "T_MA_SHOP")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class Shop extends BaseModel{

	private static final long	serialVersionUID	= 5091175754486356066L;

	/**
	 * ID
	 */
	private Long				id;

	/**
	 * 组织编号
	 */
	private Long				orgId;

	/**
	 * VERSION
	 */
	private Date				version;

	private Date				createTime;

	private Date				modifyTime;

	@Column(name = "CREATE_TIME")
	public Date getCreateTime(){
		return createTime;
	}

	public void setCreateTime(Date createTime){
		this.createTime = createTime;
	}

	@Column(name = "MODIFY_TIME")
	public Date getModifyTime(){
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime){
		this.modifyTime = modifyTime;
	}

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_MA_SHOP",sequenceName = "S_T_MA_SHOP",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_MA_SHOP")
	public Long getId(){
		return id;
	}

	public void setId(Long id){
		this.id = id;
	}

	@Version
	@Column(name = "VERSION")
	public Date getVersion(){
		return version;
	}

	public void setVersion(Date version){
		this.version = version;
	}

	/**
	 * @param orgId
	 *            the orgId to set
	 */
	public void setOrgId(Long orgId){
		this.orgId = orgId;
	}

	/**
	 * @return the orgId
	 */
	@Column(name = "ORG_ID")
	@Index(name = "IDX_SHOP_ORGID")
	public Long getOrgId(){
		return orgId;
	}
}
