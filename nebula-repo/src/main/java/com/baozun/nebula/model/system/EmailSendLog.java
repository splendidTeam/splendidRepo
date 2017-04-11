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

import com.baozun.nebula.command.Command;
import com.baozun.nebula.model.BaseModel;


/**
 * 
 * 邮件模板
 * @author Justin Hu
 *
 */
@Entity
@Table(name = "T_SYS_EMAILSENDLOG")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class EmailSendLog extends BaseModel  implements Command{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8508168695659158393L;

	
	/**
	 * PK
	 */
	private Long				id;
	
	/**
	 * 模板id
	 */
	private Long				templateId;
	
	
	private Date				sendTime;
	
	/**
	 * 接收人的email
	 */
	private String 				receiverEmail;
	
	

	private Date				version;


	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_SYS_EMAILSENDLOG",sequenceName = "S_T_SYS_EMAILSENDLOG",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_SYS_EMAILSENDLOG")
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}

	
	@Column(name = "TEMPLATE_ID")
    @Index(name = "IDX_EMAILSENDLOG_TEMPLATE_ID")
	public Long getTemplateId() {
		return templateId;
	}


	public void setTemplateId(Long templateId) {
		this.templateId = templateId;
	}


	@Column(name = "SEND_TIME")
	public Date getSendTime() {
		return sendTime;
	}


	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}


	@Column(name = "RECEIVER_EMAIL")
	public String getReceiverEmail() {
		return receiverEmail;
	}


	public void setReceiverEmail(String receiverEmail) {
		this.receiverEmail = receiverEmail;
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
