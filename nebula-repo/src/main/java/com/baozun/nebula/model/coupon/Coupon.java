package com.baozun.nebula.model.coupon;

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
 * 优惠劵实体类
 * @author qiang.yang
 * @createtime 2013-12-16 PM16:25
 *
 */
@Entity
@Table(name = "t_act_card")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class Coupon extends BaseModel{
	
	private static final long serialVersionUID = 5169305223572275313L;
	
	
	/**PK*/
	private Long id;
	/**优惠劵号*/
	private String cardNo;
	/**优惠劵密码*/
	private String cardPassword;
	/**使用时间*/
	private Date usedTime;
	/**优惠劵类型*/
	private Long cardTypeId;
	/**会员Id*/
	private Long memberId;
	/**创建时间*/
	private Date createTime;
	
	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_ACT_CARD",sequenceName = "S_T_ACT_CARD",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_ACT_CARD")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name="CARD_NO")
    @Index(name = "IDX_ACT_CARD_CARD_NO")
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	
	@Column(name="CARD_PWD")
	public String getCardPassword() {
		return cardPassword;
	}
	public void setCardPassword(String cardPassword) {
		this.cardPassword = cardPassword;
	}
	
	@Column(name="USED_TIME")
	public Date getUsedTime() {
		return usedTime;
	}
	public void setUsedTime(Date usedTime) {
		this.usedTime = usedTime;
	}
	
	@Column(name="CARD_TYPE_ID")
    @Index(name = "IDX_ACT_CARD_CARD_TYPE_ID")
	public Long getCardTypeId() {
		return cardTypeId;
	}
	public void setCardTypeId(Long cardTypeId) {
		this.cardTypeId = cardTypeId;
	}
	
	@Column(name="MEMBER_ID")
    @Index(name = "IDX_ACT_CARD_MEMBER_ID")
	public Long getMemberId() {
		return memberId;
	}
	
	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}
	
	@Column(name="CREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
	
	
}
