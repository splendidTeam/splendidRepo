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
 * 邮件拦截（告警邮件发送一定次数 次数自己设置 可停止发送）
 * @author lxy
 *
 */
@Entity
@Table(name = "T_SYS_EMAIL_INTERCEPTER")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class EmailIntercepter extends BaseModel implements Command{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 338588863010924298L;

	/**
	 * PK
	 */
	private Long				id;
	/**
	 * 模板编码
	 */
	private String				code;
	/**
	 * 特征内容
	 */
	private String				uniqueContent;
	/**
	 * 接收人的email
	 */
	private String 				receiverEmail;
	
	/**
	 * 最近发送时间
	 */
	private Date				sendTime;
	
	/**
	 * 发送次数
	 */
	private Integer 			count;
	
	

	private Date				version;


	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_SYS_EMAIL_INTERCEPTER",sequenceName = "S_T_SYS_EMAIL_INTERCEPTER",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_SYS_EMAIL_INTERCEPTER")
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "CODE")
    @Index(name = "IDX_EMAIL_INTERCEPTER_CODE")
	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "UNIQUE_CONTENT")
	public String getUniqueContent() {
		return uniqueContent;
	}


	public void setUniqueContent(String uniqueContent) {
		this.uniqueContent = uniqueContent;
	}

	@Column(name = "RECEIVER_EMAIL")
	public String getReceiverEmail() {
		return receiverEmail;
	}


	public void setReceiverEmail(String receiverEmail) {
		this.receiverEmail = receiverEmail;
	}


	@Column(name = "SEND_TIME")
	public Date getSendTime() {
		return sendTime;
	}


	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	@Column(name = "COUNT")
	public Integer getCount() {
		return count;
	}


	public void setCount(Integer count) {
		this.count = count;
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
