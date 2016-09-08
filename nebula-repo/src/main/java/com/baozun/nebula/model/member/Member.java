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
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

import org.hibernate.annotations.OptimisticLockType;

import com.baozun.nebula.model.BaseModel;

/**
 * 会员标识信息
 * 
 * @author Justin
 */
@Entity
@Table(name = "T_MEM_MEMBER",uniqueConstraints = { @UniqueConstraint(columnNames = { "LOGIN_NAME", "LOGIN_EMAIL", "LOGIN_MOBILE" }) })
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class Member extends BaseModel{

	/**
	 */
	private static final long	serialVersionUID				= -1517061062798791408L;

	/** 自有会员 **/
	public static int			MEMBER_TYPE_OWNS_MEMBERS		= 1;

	/** 自注册会员 **/
	public static int			MEMBER_TYPE_SINCE_REG_MEMBERS	= 2;

	/** 第三方会员 **/
	public static int			MEMBER_TYPE_THIRD_PARTY_MEMBER	= 3;

	/** 自注册 **/
	public static int			MEMBER_SOURCE_SINCE_REG_MEMBERS	= 1;

	/** QQ登录 **/
	public static int			MEMBER_SOURCE_QQ				= 2;

	/** 新浪登录 **/
	public static int			MEMBER_SOURCE_SINA				= 3;

	/** 支付宝登录 **/
	public static int			MEMBER_SOURCE_ALIPAY			= 4;

	/** 来自微信 */
	public static int			MEMBER_SOURCE_WECHAT			= 5;

	/**
	 * id
	 */
	private Long				id;

	/**
	 * 登录名
	 */
	private String				loginName;

	/**
	 * 登录邮箱
	 */
	private String				loginEmail;

	/**
	 * 登录手机
	 */
	private String				loginMobile;
	
	/**
	 * 盐值
	 * 密码加密使用了新的迭代加盐hash算法进行加密
	 * 这里需要将盐值进行存储
	 */
	private String				salt;
	
	/**
	 * 密码
	 */
	private String				password;

	/**
	 * BrandStore迁移的密码
	 */
	private String				oldPassword;

	/**
	 * 第三方标识
	 */
	private String				thirdPartyIdentify;

	/**
	 * 来源： 自注册 微博登录 QQ登录 等。 value在ChooseOption中配置
	 */
	private Integer				source;

	/**
	 * 生命周期
	 */
	private Integer				lifecycle;

	/**
	 * 类型: 自有会员 自注册会员 第三方会员 等。 value在ChooseOption中配置
	 */
	private Integer				type;

	private Date				version;

	/**
	 * 分组ID，用于处理会员间绑定关系
	 * by D.C 20160325
	 */
	private Long groupId;

	/**
	 * 性别 ：0表示男 1表示女
	 */
	// private Integer sex;

	/**
	 * 生日
	 */
	// private Date birthday;

	/**
	 * 真实姓名
	 */
	// private String realName;

	/**
	 * 1 接收 0 不接收
	 */
	private Integer				receiveMail;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_MEM_IDENTIFY",sequenceName = "S_T_MEM_IDENTIFY",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_MEM_IDENTIFY")
	public Long getId(){
		return id;
	}

	public void setId(Long id){
		this.id = id;
	}

	@Column(name = "LOGIN_NAME",length = 255)
	public String getLoginName(){
		return loginName;
	}

	public void setLoginName(String loginName){
		this.loginName = loginName;
	}

	@Column(name = "LOGIN_EMAIL",length = 255)
	public String getLoginEmail(){
		return loginEmail;
	}

	public void setLoginEmail(String loginEmail){
		this.loginEmail = loginEmail;
	}

	@Column(name = "LOGIN_MOBILE",length = 255)
	public String getLoginMobile(){
		return loginMobile;
	}

	public void setLoginMobile(String loginMobile){
		this.loginMobile = loginMobile;
	}
	
	@Column(name = "SALT",length = 255)
	public String getSalt() {
		return salt;
	}
	
	public void setSalt(String salt) {
		this.salt = salt;
	}
	
	@Column(name = "PASSWORD",length = 255)
	public String getPassword(){
		return password;
	}

	public void setPassword(String password){
		this.password = password;
	}

	@Column(name = "THIRD_PARTY_IDENTIFY",length = 512)
	public String getThirdPartyIdentify(){
		return thirdPartyIdentify;
	}

	public void setThirdPartyIdentify(String thirdPartyIdentify){
		this.thirdPartyIdentify = thirdPartyIdentify;
	}

	@Column(name = "SOURCE")
	public Integer getSource(){
		return source;
	}

	public void setSource(Integer source){
		this.source = source;
	}

	@Column(name = "LIFECYCLE")
	public Integer getLifecycle(){
		return lifecycle;
	}

	public void setLifecycle(Integer lifecycle){
		this.lifecycle = lifecycle;
	}

	@Column(name = "TYPE")
	public Integer getType(){
		return type;
	}

	public void setType(Integer type){
		this.type = type;
	}

	@Version
	@Column(name = "VERSION")
	public Date getVersion(){
		return version;
	}

	public void setVersion(Date version){
		this.version = version;
	}


	// @Column(name = "SEX")
	// public Integer getSex() {
	// return sex;
	// }
	//
	// public void setSex(Integer sex) {
	// this.sex = sex;
	// }
	//
	// @Column(name = "BIRTHDAY")
	// public Date getBirthday() {
	// return birthday;
	// }
	//
	// public void setBirthday(Date birthday) {
	// this.birthday = birthday;
	// }
	//
	// @Column(name = "REALNAME")
	// public String getRealName() {
	// return realName;
	// }
	//
	// public void setRealName(String realName) {
	// this.realName = realName;
	// }

	@Column(name = "RECEIVE_MAIL")
	public Integer getReceiveMail(){
		return receiveMail;
	}

	public void setReceiveMail(Integer receiveMail){
		this.receiveMail = receiveMail;
	}

	@Column(name = "OLDPASSWORD",length = 255)
	public String getOldPassword(){
		return oldPassword;
	}

	public void setOldPassword(String oldPassword){
		this.oldPassword = oldPassword;
	}
	@Column(name = "GROUP_ID")
	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

}
