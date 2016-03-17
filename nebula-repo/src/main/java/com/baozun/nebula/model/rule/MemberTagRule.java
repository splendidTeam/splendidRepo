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

package com.baozun.nebula.model.rule;

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
 * 会员标签规则实体类
 * 
 * @author - 项硕
 */
@Entity
@Table(name = "t_mem_tag_rule")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class MemberTagRule extends BaseModel implements TagRule {

	private static final long serialVersionUID = 679934041922514047L;
	
	/** 类型-会员 */
	public static final Integer TYPE_MEMBER = 1;
	/** 类型-会员分组 */
	public static final Integer TYPE_GROUP = 2;
	/** 类型-自定义 */
	public static final Integer TYPE_CUSTOM = 3;

	/** 表达式-会员 */
	public static final String EXP_MEMBER = "mid:in(?)";
	/** 表达式-会员分组 */
	public static final String EXP_GROUP = "grpid:in(?)";
	/** 表达式-会员全员 */
	public static final String EXP_ALLMEMBER = "grpall()";
	/** 表达式-自定义 */
	public static final String EXP_CUSTOM = "cgrpid:in(?)";
	
	/** 表达式前缀-会员 */
	public static final String EXP_PREFIX_MEMBER = "mid";
	/** 表达式前缀-会员分组 */
	public static final String EXP_PREFIX_MEMBER_GROUP = "grpid";
	/** 表达式前缀-会员全员 */
	public static final String EXP_PREFIX_ALLMEMBER = "grpall";
	/** 表达式前缀-自定义 */
	public static final String EXP_PREFIX_CUSTOM = "cgrpid";
	
	/** PK */
	private Long id;

	/** 会员组合类型：1会员列表，2会员分组，4组合 */
	private Integer type;

	/** 人群组合表达式对应的名称 */
	private String name;

	/** 人群组合表达式 */
	private String expression;

	/** 人群组合表达式中文解释 */
	private String text;
	
	/** 生命周期 */
	private Integer lifecycle = 1;

	/** 创建人 */
	private Long createId;

	/** 创建时间 */
	private Date createTime;

	private Date version;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_MEM_TAG_RULE",sequenceName = "S_T_MEM_TAG_RULE",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_MEM_TAG_RULE")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "TYPE")
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Column(name = "NAME", unique = true)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "EXPRESSION", length = 2000)
	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	@Column(name = "TEXT", length = 2000)
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Column(name = "LIFECYCLE")
	public Integer getLifecycle() {
		return lifecycle;
	}

	public void setLifecycle(Integer lifecycle) {
		this.lifecycle = lifecycle;
	}

	@Column(name = "CREATE_ID")
	public Long getCreateId() {
		return createId;
	}

	public void setCreateId(Long createId) {
		this.createId = createId;
	}

	@Column(name = "CREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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
