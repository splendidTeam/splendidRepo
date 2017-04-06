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
package com.baozun.nebula.model.member;

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
 * 用户行为状态登记表 用于记录用户行为状态，比如： 1.邮箱已激活 2.手机已激活 3.信息已完善 4.XX已绑定 诸如此类，此表中只记录完成的状态
 * 
 * @author
 * @since 2016/4/1 11:16 AM
 */
@Entity
@Table(name = "T_MEM_MEMBER_BEHAVIOR_STATUS")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class MemberBehaviorStatus extends BaseModel {

	private static final long serialVersionUID = -2228442057462141217L;

	/** 邮箱激活 **/
	public static String BEHAVIOR_TYPE_EMAIL_ACTIVY = "1";
	/** 手机激活 **/
	public static String BEHAVIOR_TYPE_MOBILE_ACTIVY = "2";
	/** 完善个人信息 **/
	public static String BEHAVIOR_TYPE_FINISH_PROFILE = "3";
	/** 绑定QQ **/
	public static String BEHAVIOR_TYPE_BINDING_QQ = "4";
	/** 绑定WeChat **/
	public static String BEHAVIOR_TYPE_BINDING_WECHAT = "5";
	/** 绑定sina **/
	public static String BEHAVIOR_TYPE_BINDING_SINA = "6";
	/** 绑定alipay **/
	public static String BEHAVIOR_TYPE_BINDING_ALIPAY = "7";
	/** 绑定facebook **/
	public static String BEHAVIOR_TYPE_BINDING_FACEBOOK = "8";
	/** 绑定liveid **/
	public static String BEHAVIOR_TYPE_BINDING_LIVEID = "9";
	/** 绑定品牌用户 **/
	public static String BEHAVIOR_TYPE_BINDING_VIP = "10";

	/**
	 * id
	 */
	private Long id;

	/** 会员ID */
	private Long memberId;

	/**
	 * 行为类型:邮箱，手机
	 */
	private String type;

	/**
	 * 生命周期
	 */
	private Integer lifecycle;

	private Date version;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_MEM_MEMBER_BEHAVIOR_STATUS", sequenceName = "S_T_MEM_MEMBER_BEHAVIOR_STATUS", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_MEM_MEMBER_BEHAVIOR_STATUS")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Version
	@Column(name = "VERSION")
	public Date getVersion() {
		return version;
	}

	public void setVersion(Date version) {
		this.version = version;
	}

	@Column(name = "MEMBER_ID")
    @Index(name = "IDX_MEMBER_BEHAVIOR_STATUS_MEMBER_ID")
	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	@Column(name = "TYPE")
    @Index(name = "IDX_MEMBER_BEHAVIOR_STATUS_TYPE")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "LIFECYCLE")
	public Integer getLifecycle() {
		return lifecycle;
	}

	public void setLifecycle(Integer lifecycle) {
		this.lifecycle = lifecycle;
	}

}
