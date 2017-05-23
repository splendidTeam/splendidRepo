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

import org.hibernate.annotations.Index;
import org.hibernate.annotations.OptimisticLockType;

import com.baozun.nebula.model.BaseModel;

/**
 * @ClassName: InstationMessageTemplate
 * @Description:(站内信息模板)
 * @author GEWEI.LU
 * @date 2016年1月15日 下午3:11:10
 */
@Entity
@Table(name = "T_SYS_COUPON_LOG")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class CouponSendUserLog extends BaseModel {
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
	 * 优惠券id
	 */
	private Long promotioncouponcodeid;
	/**
	 * 优惠券code
	 */
	private String promotioncouponname;
	/**
	 * 优惠券类型id
	 */
	private Long promotioncouponid;
	/**
	 * 优惠券类型名字
	 */
	private String promotioncouponcode;
	/**
	 * 创建时间
	 */
	private Date createTime;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_SYS_COUPON_LOG", sequenceName = "S_T_SYS_COUPON_LOG", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_SYS_COUPON_LOG")
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
    @Index(name = "IDX_COUPON_LOG_MEMBERID")
	public Long getMemberid() {
		return memberid;
	}

	public void setMemberid(Long memberid) {
		this.memberid = memberid;
	}

	@Column(name = "PROMOTIONCOUPONCODEID")
    @Index(name = "IDX_COUPON_LOG_PROMOTIONCOUPONCODEID")
	public Long getPromotioncouponcodeid() {
		return promotioncouponcodeid;
	}

	public void setPromotioncouponcodeid(Long promotioncouponcodeid) {
		this.promotioncouponcodeid = promotioncouponcodeid;
	}

	@Column(name = "PROMOTIONCOUPONNAME")
	public String getPromotioncouponname() {
		return promotioncouponname;
	}

	public void setPromotioncouponname(String promotioncouponname) {
		this.promotioncouponname = promotioncouponname;
	}

	@Column(name = "PROMOTIONCOUPONID")
    @Index(name = "IDX_COUPON_LOG_PROMOTIONCOUPONID")
	public Long getPromotioncouponid() {
		return promotioncouponid;
	}

	public void setPromotioncouponid(Long promotioncouponid) {
		this.promotioncouponid = promotioncouponid;
	}

	@Column(name = "PROMOTIONCOUPONCODE")
	public String getPromotioncouponcode() {
		return promotioncouponcode;
	}

	public void setPromotioncouponcode(String promotioncouponcode) {
		this.promotioncouponcode = promotioncouponcode;
	}

}
