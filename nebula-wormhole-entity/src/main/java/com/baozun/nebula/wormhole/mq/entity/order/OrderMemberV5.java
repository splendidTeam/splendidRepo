package com.baozun.nebula.wormhole.mq.entity.order;

import java.io.Serializable;

/**
 * 订单会员
 * @author Justin Hu
 *
 */

public class OrderMemberV5 implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8629588575799300410L;
	
	
	/**
	 * 商城会员ID
	 */
	@Deprecated
	private Long memberId;
	
	/**
	 * 登录名称，对应于member中的loginName字段
	 */
	private String loginName;
	
	/**
	 * 商城会员VIP CODE
	 */
	private String vipCode;
	
	/**
	 * 会员email
	 */
	private String email;


	public Long getMemberId() {
		return memberId;
	}


	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}
	
	


	public String getLoginName() {
		return loginName;
	}


	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}

	public String getVipCode() {
		return vipCode;
	}

	public void setVipCode(String vipCode) {
		this.vipCode = vipCode;
	}
	
}
