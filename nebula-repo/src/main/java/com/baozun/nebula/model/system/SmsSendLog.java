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
 * 短信发送日志
 * @author jeally
 * @version 2016年4月26日
 */
@Entity
@Table(name = "T_SYS_SMSSENDLOG")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class SmsSendLog extends BaseModel{

	private static final long serialVersionUID = 1L;

	private Long		id;
	/* hub接口的日志id*/
	private Long		logId;
	/* 短信模版code*/
	private String		templateCode;
	/* 接受人mobile*/
	private String		mobile;
	/* 返回码 */
	private String		resultCode;
	/* 发送时间*/
	private Date		sendTime;

	private Date		version;
	
	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_SYS_SMSSENDLOG",sequenceName = "S_T_SYS_SMSSENDLOG",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_SYS_SMSSENDLOG")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "LOG_ID")
    @Index(name = "IDX_SMSSENDLOG_LOG_ID")
	public Long getLogId() {
		return logId;
	}

	public void setLogId(Long logId) {
		this.logId = logId;
	}

	@Column(name = "TEMPLATE_CODE")
    @Index(name = "IDX_SMSSENDLOG_TEMPLATE_CODE")
	public String getTemplateCode() {
		return templateCode;
	}

	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}

	@Column(name = "MOBILE")
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Column(name = "RESULT_CODE")
	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	@Column(name = "SEND_TIME")
	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
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
