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

import org.hibernate.annotations.OptimisticLockType;

import com.baozun.nebula.model.BaseModel;

/**
 * 模板供应商
 * @author Tianlong.Zhang
 * 
 */
@Deprecated
@Entity
@Table(name = "T_SF_TEMEPALTE_PROVIDER")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class TemeplateProvider extends BaseModel {

	private static final long serialVersionUID = -1903407209267043743L;
	private Long id;
	private Long temeplateId;
	private Long providerId;
	private Date version;

	public TemeplateProvider() {

	}

	/**
	 * @return the id
	 */
	@Id
	@Column(name="ID")
	@SequenceGenerator(name = "SEQ_T_SF_TEMEPALTE_PROVIDER",sequenceName = "SEQ_T_SF_TEMEPALTE_PROVIDER",allocationSize = 1,initialValue = 1001)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_SF_TEMEPALTE_PROVIDER")
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the temeplateId
	 */
	public Long getTemeplateId() {
		return temeplateId;
	}

	/**
	 * @param temeplateId
	 *            the temeplateId to set
	 */
	public void setTemeplateId(Long temeplateId) {
		this.temeplateId = temeplateId;
	}

	/**
	 * @return the providerId
	 */
	public Long getProviderId() {
		return providerId;
	}

	/**
	 * @param providerId
	 *            the providerId to set
	 */
	public void setProviderId(Long providerId) {
		this.providerId = providerId;
	}

	/**
	 * @return the version
	 */
	public Date getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(Date version) {
		this.version = version;
	}

}
