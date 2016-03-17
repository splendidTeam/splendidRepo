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
import javax.persistence.Version;

import org.hibernate.annotations.OptimisticLockType;

import com.baozun.nebula.model.BaseModel;
/**
 * 接收自SCM的消息内容
 * @author Justin Hu
 *
 */
@Entity
@Table(name = "T_SYS_MSG_RECEIVE_CONTENT")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class MsgReceiveContent extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6465981355362134357L;

	/**
	 * id
	 */
	private Long id;
	
	
	/**
	 * 接口标识(见接口列表)
	 */
	private String ifIdentify;
	
		
	/**
	 * 消息发起时间
	 */
	private Date sendTime;
	
	/**
	 * 消息唯一ID,可用于标志消息，以及去重
	 */
	private String msgId;
	
	/**
	 * 消息正文(密文)
	 */
	private String msgBody;
	
	/**
	 * 是否已经处理过，对于处理过的消息，可以进行定期转储
	 */
	private Boolean isProccessed;
	
	private Date				version;

	
	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_SYS_MSG_RECEIVE_CONTENT",sequenceName = "S_T_SYS_MSG_RECEIVE_CONTENT",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_SYS_MSG_RECEIVE_CONTENT")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	

	@Column(name = "IF_IDENTIFY",length = 50)
	public String getIfIdentify() {
		return ifIdentify;
	}

	public void setIfIdentify(String ifIdentify) {
		this.ifIdentify = ifIdentify;
	}
	
	@Column(name = "SEND_TIME")
	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}


	@Column(name = "MSG_BODY")
	@Lob
	public String getMsgBody() {
		return msgBody;
	}

	
	public void setMsgBody(String msgBody) {
		this.msgBody = msgBody;
	}
		

	@Column(name = "MSG_ID",length = 50)
	public String getMsgId() {
		return msgId;
	}

	public void setMsgId(String msgId) {
		this.msgId = msgId;
	}

	@Version
	@Column(name = "VERSION")
	public Date getVersion() {
		return version;
	}

	public void setVersion(Date version) {
		this.version = version;
	}

	@Column(name = "IS_PROCCESSED")
	public Boolean getIsProccessed() {
		return isProccessed;
	}

	public void setIsProccessed(Boolean isProccessed) {
		this.isProccessed = isProccessed;
	}
	
	
}
