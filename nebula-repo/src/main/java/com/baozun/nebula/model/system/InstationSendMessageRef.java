package com.baozun.nebula.model.system;

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
 * @ClassName: InstationMessageTemplate
 * @Description:(站内信息发送记录)
 * @author GEWEI.LU
 * @date 2016年1月15日 下午3:11:10
 */
@Entity
@Table(name = "T_SYS_INSTATION_SEND_MESSAGE")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class InstationSendMessageRef extends BaseModel {
	private static final long serialVersionUID = -2425364684161912204L;
	/**
	 * 主键
	 */
	private Long id;
	/**
	 * 会员ID
	 */
	private Long memberid;
	/**
	 * 站内模板ID
	 */
	private Long MessageTemplateid;
	/**
	 * 创建时间
	 */
	private Date createTime;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_SYS_INSTATION_SEND_MESSAGE", sequenceName = "S_T_SYS_INSTATION_SEND_MESSAGE", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_SYS_INSTATION_SEND_MESSAGE")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "CREATETIME")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "MEMBERID")
	public Long getMemberid() {
		return memberid;
	}

	public void setMemberid(Long memberid) {
		this.memberid = memberid;
	}

	@Column(name = "MESSAGETEMPLATEID")
	public Long getMessageTemplateid() {
		return MessageTemplateid;
	}

	public void setMessageTemplateid(Long messageTemplateid) {
		MessageTemplateid = messageTemplateid;
	}
}
