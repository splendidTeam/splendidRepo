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

import java.math.BigDecimal;
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
 * 会员行为信息
 * id使用Member的关键
 * @author Justin
 *
 */
@Entity
@Table(name = "T_MEM_CONDUCT")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class MemberConduct extends BaseModel {


	/**
	 */
	private static final long serialVersionUID = 4950067082662307004L;

	/**
	 * id
	 */
	private Long id;
	
	/**
	 * 登录次数
	 */
	private Integer loginCount;
	
	/**
	 * 注册时间
	 */
	private Date registerTime;
	
	/**
	 * 登录时间(最近)
	 */
	private Date loginTime;
	
	/**
	 * 付款时间(最近)
	 */
	private Date payTime;
	/**
	 * 注册ip
	 */
	private String registerIp;
	
	/**
	 * 登录ip(最近)
	 */
	private String loginIp;
	
	/**
	 * 累积消费金额
	 * 这里是指整个交易完成的消费金额
	 * 当创建订单时，不会增加消费金额，只有在交易完成后才会增加消费金额
	 */
	private BigDecimal cumulativeConAmount;
	
	/**
	 * 登录客户端识别码
	 * 类似ip一样的信息
	 * @since 5.3.2.18
	 */
	private String clientIdentificationMechanisms;
	
	/**
     * 注册客户端识别码
     * 类似ip一样的信息
     * @since 5.3.2.18
     */
    private String registerClientIdentificationMechanisms;
	

	
	private Date version;

	@Id
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "LOGIN_COUNT")
	public Integer getLoginCount() {
		return loginCount;
	}

	public void setLoginCount(Integer loginCount) {
		this.loginCount = loginCount;
	}

	@Column(name = "REGISTER_TIME")
	public Date getRegisterTime() {
		return registerTime;
	}

	public void setRegisterTime(Date registerTime) {
		this.registerTime = registerTime;
	}

	@Column(name = "LOGIN_TIME")
	public Date getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	@Column(name = "PAY_TIME")
	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	@Column(name = "REGISTER_IP")
	public String getRegisterIp() {
		return registerIp;
	}

	public void setRegisterIp(String registerIp) {
		this.registerIp = registerIp;
	}

	@Column(name = "LOGIN_IP")
	public String getLoginIp() {
		return loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}
	
	@Column(name = "CUMULATIVE_CON_AMOUNT")
	public BigDecimal getCumulativeConAmount() {
		return cumulativeConAmount;
	}

	public void setCumulativeConAmount(BigDecimal cumulativeConAmount) {
		this.cumulativeConAmount = cumulativeConAmount;
	}

	@Version
	@Column(name = "VERSION")
	public Date getVersion() {
		return version;
	}

	public void setVersion(Date version) {
		this.version = version;
	}
	/**
	 * 获得 登录客户端识别码
	 * @return clientIdentificationMechanisms
	 * @since 5.3.2.18
	 */
	@Column(name = "CLIENT_IDENTIFICATION_MECHANISMS",nullable=true,unique=false)
    public String getClientIdentificationMechanisms(){
        return clientIdentificationMechanisms;
    }

    /**
     * 设置 登录客户端识别码
     * @param clientIdentificationMechanisms
     * @since 5.3.2.18
     */
    public void setClientIdentificationMechanisms(String clientIdentificationMechanisms){
        this.clientIdentificationMechanisms = clientIdentificationMechanisms;
    }
	
    /**
     * 获得 注册客户端识别码
     *      类似ip一样的信息
     * @return registerClientIdentificationMechanisms
     * @since 5.3.2.18
     */
    @Column(name = "REGISTER_CLIENT_IDENTIFICATION_MECHANISMS",nullable=true,unique=false)
    public String getRegisterClientIdentificationMechanisms(){
        return registerClientIdentificationMechanisms;
    }

    /**
     * 设置 注册客户端识别码
     *      类似ip一样的信息
     * @param registerClientIdentificationMechanisms
     * @since 5.3.2.18
     */
    public void setRegisterClientIdentificationMechanisms(String registerClientIdentificationMechanisms){
        this.registerClientIdentificationMechanisms = registerClientIdentificationMechanisms;
    }
	
	
}
