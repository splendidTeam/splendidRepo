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
package com.baozun.nebula.command.product;

import java.util.Date;

import com.baozun.nebula.command.Command;

/**
 * @author Tianlong.Zhang
 * 
 */
public class ConsultantOperationlogCommand implements Command {

	private static final long serialVersionUID = 8232708307957307806L;

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
	 * @return the consultId
	 */
	public Long getConsultId() {
		return consultId;
	}
	/**
	 * @param consultId the consultId to set
	 */
	public void setConsultId(Long consultId) {
		this.consultId = consultId;
	}
	/**
	 * @return the note
	 */
	public String getNote() {
		return note;
	}
	/**
	 * @param note the note to set
	 */
	public void setNote(String note) {
		this.note = note;
	}
	/**
	 * @return the consultOperationType
	 */
	public Long getConsultOperationType() {
		return consultOperationType;
	}
	/**
	 * @param consultOperationType the consultOperationType to set
	 */
	public void setConsultOperationType(Long consultOperationType) {
		this.consultOperationType = consultOperationType;
	}
	/**
	 * @return the operatorId
	 */
	public Long getOperatorId() {
		return operatorId;
	}
	/**
	 * @param operatorId the operatorId to set
	 */
	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}
	/**
	 * @return the operatetime
	 */
	public Date getOperatetime() {
		return operatetime;
	}
	/**
	 * @param operatetime the operatetime to set
	 */
	public void setOperatetime(Date operatetime) {
		this.operatetime = operatetime;
	}
	/**
	 * @return the version
	 */
	public Date getVersion() {
		return version;
	}
	/**
	 * @param version the version to set
	 */
	public void setVersion(Date version) {
		this.version = version;
	}
	
}
