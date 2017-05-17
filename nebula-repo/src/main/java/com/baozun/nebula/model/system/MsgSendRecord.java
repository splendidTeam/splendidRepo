package com.baozun.nebula.model.system;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.OptimisticLockType;

import com.baozun.nebula.model.BaseModel;

/**
 * 消息发送记录
 * 每一条发往SCM的消息都会添加一条记录，用于做补偿机制
 * @author Justin Hu
 *
 */

@Entity
@Table(name = "T_SYS_MSG_SEND_RECORD")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class MsgSendRecord extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1770600196077162095L;

	/**
	 * id
	 */
	private Long id;
	
	/**
	 * 接口标识(见接口列表)
	 */
	private String ifIdentify;
	
	/**
	 * 目标记录id,根据消息标识而保存不同的id，如订单id,支付id
	 */
	private Long targetId;
	
	/**
	 * 消息发送时间
	 */
	private Date sendTime;
	
	/**
	 * 消息反馈的时间
	 */
	private Date feedbackTime;
	
	/**
	 * 发送次数
	 */
	private Integer sendCount;
	
	/**
	 * 创建时间
	 */
	private Date createTime;
	
	/**
	 * 扩展信息(一些参数,如在售商品的分页信息)
	 */
	private String ext;
	
	
	private Date				version;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_SYS_MSG_SEND_RECORD",sequenceName = "S_T_SYS_MSG_SEND_RECORD",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_SYS_MSG_SEND_RECORD")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "IF_IDENTIFY",length = 50)
    @Index(name = "IDX_MSG_SEND_RECORD_IF_IDENTIFY")
	public String getIfIdentify() {
		return ifIdentify;
	}

	public void setIfIdentify(String ifIdentify) {
		this.ifIdentify = ifIdentify;
	}

	@Column(name = "TARGET_ID")
    @Index(name = "IDX_MSG_SEND_RECORD_TARGET_ID")
	public Long getTargetId() {
		return targetId;
	}

	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}

	@Column(name = "SEND_TIME")
	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	@Column(name = "FEEDBACK_TIME")
	public Date getFeedbackTime() {
		return feedbackTime;
	}

	public void setFeedbackTime(Date feedbackTime) {
		this.feedbackTime = feedbackTime;
	}

	@Column(name = "SEND_COUNT")
	public Integer getSendCount() {
		return sendCount;
	}

	public void setSendCount(Integer sendCount) {
		this.sendCount = sendCount;
	}

	@Column(name = "CREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
		
	@Column(name = "EXT")
	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	@Version
	@Column(name = "VERSION")
	public Date getVersion() {
		return version;
	}

	public void setVersion(Date version) {
		this.version = version;
	}
	
	
	
}
