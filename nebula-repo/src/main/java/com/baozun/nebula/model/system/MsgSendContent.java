package com.baozun.nebula.model.system;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.OptimisticLockType;

import com.baozun.nebula.model.BaseModel;

/**
 * 消息发送内容
 * 与MsgSendContent进行关联
 * @author Justin Hu
 *
 */
@Entity
@Table(name = "T_SYS_MSG_SEND_CONTENT")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class MsgSendContent extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7559558734241773936L;
	
	/**
	 * 关联消息记录表的id，与记录表
	 */
	private Long 	id;
	
	/**
	 * 消息内容,存储时在这里的数据是加密的
	 */
	private String 	msgBody;
	
	
	private Date				version;

	
	@Id
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	@Column(name = "MSG_BODY")
	@Lob
	public String getMsgBody() {
		return msgBody;
	}

	
	public void setMsgBody(String msgBody) {
		this.msgBody = msgBody;
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
