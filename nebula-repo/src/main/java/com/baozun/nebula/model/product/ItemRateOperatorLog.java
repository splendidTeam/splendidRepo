package com.baozun.nebula.model.product;

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
 * 商品评价操作日志
 * @author xingyu  
 * @date  
 * @version
 */
@Entity
@Table(name = "T_PD_ITEM_RATE_OPERATOR_LOG")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class ItemRateOperatorLog extends BaseModel{
	
	private static final long serialVersionUID = 2412315782144300460L;
	/**PK*/
	private Long id;
	/**评价Id*/
	private Long rateId;
	/**操作类型*/
	private Integer type;
	/**记录回复内容*/
	private String note;
	/**操作员Id*/
	private Long operatorId;
	/**操作时间*/
	private Date operatorTime;


	
	@Id
	@Column(name="ID")
	@SequenceGenerator(name = "SEQ_T_PD_ITEM_RATE_OPERATOR_LOG",sequenceName = "S_T_PD_ITEM_RATE_OPERATOR_LOG",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_PD_ITEM_RATE_OPERATOR_LOG")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Column(name="TYPE")
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	@Column(name="NOTE")
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	@Column(name="OPERATOR_ID")
	public Long getOperatorId() {
		return operatorId;
	}
	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}
	@Column(name="OPERATOR_TIME")
	public Date getOperatorTime() {
		return operatorTime;
	}
	public void setOperatorTime(Date operatorTime) {
		this.operatorTime = operatorTime;
	}
	@Column(name="RATE_ID")
	public Long getRateId() {
		return rateId;
	}
	public void setRateId(Long rateId) {
		this.rateId = rateId;
	}
	

	
	
	

}
