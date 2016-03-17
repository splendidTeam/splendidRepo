package com.baozun.nebula.command.coupon;

import java.util.Date;

import com.baozun.nebula.command.Command;
/**
 * 存储优惠劵的全部信息
 * @author qiang.yang
 *
 */
public class CouponCommand implements Command{

	private static final long serialVersionUID = 2144601314024235919L;
	/**优惠劵Id*/
	private Long cardId;
	/**优惠劵号*/
	private String cardNo;
	/**优惠劵密码*/
	private String cardPassword;
	/**使用时间*/
	private Date usedTime;
	/**优惠劵类型Id*/
	private Long cardTypeId;
	/**开始时间*/
	private Date beginTime;
	/**结束时间*/
	private Date endTime;
	/**优惠劵的值*/
	private Double factor;
	/**优惠劵类型*/
	private String factorType;
	/**优惠劵名称*/
	private String name;
	/**是否多次使用*/
	private boolean useNum;
	/**会员id*/
	private Long memberId;
	/**创建时间*/
	private Date createTime;
	/**状态*/
	private String status;
	
	public Long getCardId() {
		return cardId;
	}
	public void setCardId(Long cardId) {
		this.cardId = cardId;
	}
	public String getCardNo() {
		return cardNo;
	}
	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}
	public String getCardPassword() {
		return cardPassword;
	}
	public void setCardPassword(String cardPassword) {
		this.cardPassword = cardPassword;
	}
	public Date getUsedTime() {
		return usedTime;
	}
	public void setUsedTime(Date usedTime) {
		this.usedTime = usedTime;
	}
	public Long getCardTypeId() {
		return cardTypeId;
	}
	public void setCardTypeId(Long cardTypeId) {
		this.cardTypeId = cardTypeId;
	}
	public Date getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Double getFactor() {
		return factor;
	}
	public void setFactor(Double factor) {
		this.factor = factor;
	}
	public String getFactorType() {
		return factorType;
	}
	public void setFactorType(String factorType) {
		this.factorType = factorType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isUseNum() {
		return useNum;
	}
	public void setUseNum(boolean useNum) {
		this.useNum = useNum;
	}
	public Long getMemberId() {
		return memberId;
	}
	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	
	
	
}
