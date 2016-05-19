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
 * 商品上下架操作日志
 * @author dong.cheng  
 * @date  
 * @version
 */
@Entity
@Table(name = "T_PD_ITEM_OPERATE_LOG")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class ItemOperateLog extends BaseModel{
	
	private static final long serialVersionUID = -3243909857344596152L;

	/** PK. */
	private Long id;
	
	/**
	 * 商品
	 */
	private Long itemId;
	
	/**
	 * 商品编码
	 */
	private String code;
	
	/**
	 * 上架时间
	 */
	private Date pushTime;
	
	/**
	 * 下架时间
	 */
	private Date soldOutTime;
	
	/**
	 * 上架操作人员
	 */
	private Long pushOperatorId;
	
	/**
	 * 下架操作人员
	 */
	private Long soldOutOperatorId;
	
	/**
	 * 在架时长
	 */
	private Date activeTime;
	
	/**
	 * 创建时间
	 */
	private Date createTime;
	
	/**
	 * 更新时间
	 */
	private Date updateTime;
	
	
	
	@Id
	@Column(name="ID")
	@SequenceGenerator(name = "SEQ_T_PD_ITEM_OPERATE_LOG",sequenceName = "S_T_PD_ITEM_OPERATE_LOG",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_PD_ITEM_OPERATE_LOG")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name="ITEM_ID")
	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	@Column(name="CODE")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name="PUSH_TIME")
	public Date getPushTime() {
		return pushTime;
	}

	public void setPushTime(Date pushTime) {
		this.pushTime = pushTime;
	}

	@Column(name="SOLD_OUT_TIME")
	public Date getSoldOutTime() {
		return soldOutTime;
	}

	public void setSoldOutTime(Date soldOutTime) {
		this.soldOutTime = soldOutTime;
	}

	@Column(name="PUSH_OPERATOR_ID")
	public Long getPushOperatorId() {
		return pushOperatorId;
	}

	public void setPushOperatorId(Long pushOperatorId) {
		this.pushOperatorId = pushOperatorId;
	}

	@Column(name="SOLD_OUT_OPERATOR_ID")
	public Long getSoldOutOperatorId() {
		return soldOutOperatorId;
	}

	public void setSoldOutOperatorId(Long soldOutOperatorId) {
		this.soldOutOperatorId = soldOutOperatorId;
	}

	@Column(name="ACTIVE_TIME")
	public Date getActiveTime() {
		return activeTime;
	}

	public void setActiveTime(Date activeTime) {
		this.activeTime = activeTime;
	}

	@Column(name="CREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name="UPDATE_TIME")
	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
}
