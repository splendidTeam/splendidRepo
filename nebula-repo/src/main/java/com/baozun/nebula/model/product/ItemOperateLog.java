package com.baozun.nebula.model.product;

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
	 * 商品Id
	 */
	private Long itemId;
	
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
	private String pushOperatorName;
	
	/**
	 * 下架操作人员
	 */
	private String soldOutOperatorName;
	
	/**
	 * 在架时长(按秒存)
	 */
	private Long activeTime;
	
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
	@Index(name = "IDX_ITEM_OPLOG_ITEMID")
	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	@Column(name="PUSH_TIME")
	@Index(name = "IDX_ITEM_OPLOG_PUSHTIME")
	public Date getPushTime() {
		return pushTime;
	}

	public void setPushTime(Date pushTime) {
		this.pushTime = pushTime;
	}

	@Column(name="SOLD_OUT_TIME")
	@Index(name = "IDX_ITEM_OPLOG_SOLDOUTTIME")
	public Date getSoldOutTime() {
		return soldOutTime;
	}

	public void setSoldOutTime(Date soldOutTime) {
		this.soldOutTime = soldOutTime;
	}

	@Column(name="PUSH_OPERATOR_NAME")
	public String getPushOperatorName() {
		return pushOperatorName;
	}

	public void setPushOperatorName(String pushOperatorName) {
		this.pushOperatorName = pushOperatorName;
	}

	@Column(name="SOLD_OUT_OPERATOR_NAME")
	public String getSoldOutOperatorName() {
		return soldOutOperatorName;
	}

	public void setSoldOutOperatorName(String soldOutOperatorName) {
		this.soldOutOperatorName = soldOutOperatorName;
	}

	@Column(name="ACTIVE_TIME")
	public Long getActiveTime() {
		return activeTime;
	}

	public void setActiveTime(Long activeTime) {
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
