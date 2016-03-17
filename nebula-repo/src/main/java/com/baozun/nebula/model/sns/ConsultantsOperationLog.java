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
package com.baozun.nebula.model.sns;

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
 * 咨询操作记录
 * 
 * @author Tianlong.Zhang
 * 
 */
@Entity
@Table(name = "t_sns_consultants_operationlog")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class ConsultantsOperationLog extends BaseModel {

	private static final long serialVersionUID = -4703258279192907470L;
	private Long id;
	private Long consultId;
	private String note;
	private Long consultOperationType;
	private Long operatorId;
	private Date operatetime;
	private Date version;

	/**
	 * @return the id
	 */
	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_SNS_CONSULTANTS_OPERATIONLOG", sequenceName = "S_T_SNS_CONSULTANTS_OPERATIONLOG", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_SNS_CONSULTANTS_OPERATIONLOG")
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
	 * @return the consultId
	 */
	@Column(name = "CONSULTID")
	public Long getConsultId() {
		return consultId;
	}

	/**
	 * @param consultId
	 *            the consultId to set
	 */
	public void setConsultId(Long consultId) {
		this.consultId = consultId;
	}

	/**
	 * @return the note
	 */
	@Column(name = "NOTE", length = 200)
	public String getNote() {
		return note;
	}

	/**
	 * @param note
	 *            the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * @return the operationTypeId
	 */
	@Column(name = "consultOperationType")
	public Long getConsultOperationType() {
		return consultOperationType;
	}

	/**
	 * @param operationTypeId
	 *            the operationTypeId to set
	 */
	public void setConsultOperationType(Long consultOperationType) {
		this.consultOperationType = consultOperationType;
	}

	/**
	 * @return the operatorId
	 */
	@Column(name = "OPERATORID")
	public Long getOperatorId() {
		return operatorId;
	}

	/**
	 * @param operatorId
	 *            the operatorId to set
	 */
	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}

	/**
	 * @return the operationTime
	 */
	@Column(name = "operatetime")
	public Date getOperatetime() {
		return operatetime;
	}

	/**
	 * @param operationTime
	 *            the operationTime to set
	 */
	public void setOperatetime(Date operatetime) {
		this.operatetime = operatetime;
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
