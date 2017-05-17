/**
 * Copyright (c) 2012 Baozun All Rights Reserved.
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

package com.baozun.nebula.model.promotion;

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
 * @author - 项硕
 */
@Entity
@Table(name = "T_SC_LIMIT_CONDITION")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class LimitCondition extends BaseModel {

	private static final long serialVersionUID = 8135311794210946272L;

	/** PK */
	private Long id;
	
	/** 限购头部ID */
	private Long limitId;
	
	/** 1:有效0：无效 */
	private Integer activeMark = 1;

	/** 表达式中文文本 */
	private String text;
	
	/** 表达式 */
	private String expression;
	
	/** version. */
	private Date version;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_SC_LIMIT_CONDITION",sequenceName = "S_T_SC_LIMIT_CONDITION",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_SC_LIMIT_CONDITION")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "LIMIT_ID")
    @Index(name = "IDX_LIMIT_CONDITION_LIMIT_ID")
	public Long getLimitId() {
		return limitId;
	}

	public void setLimitId(Long limitId) {
		this.limitId = limitId;
	}

	@Column(name = "ACTIVE_MARK")
	public Integer getActiveMark() {
		return activeMark;
	}

	public void setActiveMark(Integer activeMark) {
		this.activeMark = activeMark;
	}

	@Column(name = "TEXT", length = 2000)
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Column(name = "EXPRESSION", length = 2000)
	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
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
