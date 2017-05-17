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
 * 优惠劵类型实体
 * @author qiang.yang
 *
 */
@Entity
@Table(name="t_act_card_type")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class CouponType extends BaseModel{
	private static final long serialVersionUID = 7864825854599780250L;
	
	
	/**PK*/
	private Long id;
	/**开始时间*/
	private Date beginTime;
	/**结束时间*/
	private Date endTime;
	/**优惠劵的值*/
	private Double factor;
	/**优惠劵值类型（Code）*/
	private String factorType;
	/**优惠劵名称*/
	private String name;
	/**是否多次使用*/
	private boolean usenum;
	
	@Id
	@Column(name="ID")
	@SequenceGenerator(name="SEQ_T_ACT_CARD_TYPE",sequenceName="S_T_ACT_CARD_TYPE",allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="SEQ_T_ACT_CARD_TYPE")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name="BEGIN_TIME")
	public Date getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	
	@Column(name="END_TIME")
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	@Column(name="FACTOR")
	public Double getFactor() {
		return factor;
	}
	public void setFactor(Double factor) {
		this.factor = factor;
	}
	
	@Column(name="FACTOR_TYPE")
	public String getFactorType() {
		return factorType;
	}
	public void setFactorType(String factorType) {
		this.factorType = factorType;
	}
	
	@Column(name="NAME")
    @Index(name = "IDX_ACT_CARD_TYPE_NAME")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name="USENUM")
	public boolean isUsenum() {
		return usenum;
	}
	public void setUsenum(boolean usenum) {
		this.usenum = usenum;
	}
	
	
	
}
