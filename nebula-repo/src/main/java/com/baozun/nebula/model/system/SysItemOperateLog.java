package com.baozun.nebula.model.system;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.OptimisticLockType;

import com.baozun.nebula.model.BaseModel;

/**
 * 商品操作日志相关
 * @author jay.yang
 *
 */
@Entity
@Table(name = "T_SYS_ITEM_OPERATE_LOG")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class SysItemOperateLog extends BaseModel{

	private static final long serialVersionUID = -7129217516762310008L;
	
	private Long id;
	/** 商品备份信息，JSON字符串 */
	private String context;
	/** 操作人ID */
	private Long optId;
	/** 1.单个操作，2.系统推送，3.导入 */
	private Long optType;
	/** 商品ID */
	private Long itemId;
	/** 操作时间 */
	private Date createTime;
	
	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_SYS_OPERATE_LOG", sequenceName = "S_T_SYS_OPERATE_LOG", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_SYS_OPERATE_LOG")
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "CONTEXT")
	@Lob
	public String getContext() {
		return context;
	}
	
	public void setContext(String context) {
		this.context = context;
	}
	
	@Column(name = "OPT_ID")
	public Long getOptId() {
		return optId;
	}
	
	public void setOptId(Long optId) {
		this.optId = optId;
	}
	
	@Column(name = "OPT_TYPE")
	public Long getOptType() {
		return optType;
	}
	
	public void setOptType(Long optType) {
		this.optType = optType;
	}
	
	@Column(name = "ITEM_ID")
	public Long getItemId() {
		return itemId;
	}
	
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}
	
	@Column(name = "CREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}
	
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
