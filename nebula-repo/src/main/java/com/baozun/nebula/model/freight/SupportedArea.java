/**
 * Copyright (c) 2013 Baozun All Rights Reserved.
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
package com.baozun.nebula.model.freight;

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
 * 
 * 物流支持区域
 * @author Tianlong.Zhang
 *
 */
@Entity
@Table(name="T_SF_SUPPORTED_AREA")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class SupportedArea  extends BaseModel{

	private static final long serialVersionUID = -1924053874848718258L;
	
	public static final String WHITE_TYPE = "white";
	
	public static final String BLACK_TYPE = "black";

	/**
	 * id
	 */
	private Long id;

	/**
	 * 物流方式Id
	 */
	private Long distributionModeId;
	
	/**
	 * 类型 ， 黑名单，白名单 等
	 */
	private String type;
	
	/**
	 * 分组编号，用于确定同一个黑名单和白名单的组合
	 */
	private Long groupNo;
	
	/**
	 * 目的地id: 可代表地址的所有层级，如省，市，区，镇的id
	 */
	private String areaId;
	
	/**
	 * 代称，表示某些区域的代称 如：江浙沪   等
	 */
	private String designate;
	
	private Date version;

	/**
	 * @return the id
	 */
	@Id
	@Column(name="ID")
	@SequenceGenerator(name = "SEQ_T_SF_SUPPORTED_AREA",sequenceName = "SEQ_T_SF_SUPPORTED_AREA",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_SF_SUPPORTED_AREA")
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the distributionModeId
	 */
	@Column(name="DISTRIBUTION_MODE_ID")
	public Long getDistributionModeId() {
		return distributionModeId;
	}

	/**
	 * @param distributionModeId the distributionModeId to set
	 */
	public void setDistributionModeId(Long distributionModeId) {
		this.distributionModeId = distributionModeId;
	}

	/**
	 * @return the type
	 */
	@Column(name="TYPE",length=20)
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the groupNo
	 */
	@Column(name="GROUP_NO")
	public Long getGroupNo() {
		return groupNo;
	}

	/**
	 * @param groupNo the groupNo to set
	 */
	public void setGroupNo(Long groupNo) {
		this.groupNo = groupNo;
	}

	/**
	 * @return the areaId
	 */
	@Column(name="AREA_ID")
	public String getAreaId() {
		return areaId;
	}

	/**
	 * @param areaId the areaId to set
	 */
	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public void setDesignate(String designate) {
		this.designate = designate;
	}

	@Column(name = "DESIGNATE")
	public String getDesignate() {
		return designate;
	}

	public void setVersion(Date version) {
		this.version = version;
	}

	@Version
	@Column(name = "VERSION")
	public Date getVersion() {
		return version;
	}
	
}
