package com.baozun.nebula.model.system;

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

import com.baozun.nebula.command.Command;
import com.baozun.nebula.model.BaseModel;

/**
 * 邮件订阅
 * @author Justin Hu
 *
 */
@Entity
@Table(name = "T_SYS_EMAIL_SUBSCRIBE")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class EmailSubscribe extends BaseModel implements Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3789271918965033529L;

	/**
	 * PK
	 */
	private Long				id;
	
	/**
	 * 收件人
	 */
	private String				receiver;
	
	/**
	 * 模板code
	 */
	private String				templateCode;
	
	/**
	 * 数据(json)
	 */
	private String 				data;
	
	/**
	 * 发送时间
	 */
	private Date				date; 
	
	/**
	 * 类型
	 */
	private Integer				type;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_SYS_EMAIL_SUBSCRIBE",sequenceName = "S_T_SYS_EMAIL_SUBSCRIBE",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_SYS_EMAIL_SUBSCRIBE")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "RECEIVER",length = 200)
    @Index(name = "IDX_EMAIL_SUBSCRIBE_RECEIVER")
	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	@Column(name = "TEMPLATE_CODE",length = 50)
    @Index(name = "IDX_EMAIL_SUBSCRIBE_TEMPLATE_CODE")
	public String getTemplateCode() {
		return templateCode;
	}

	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}

	@Column(name = "DATA",length = 1000)
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	@Column(name = "DATE")
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Column(name = "TYPE")
    @Index(name = "IDX_EMAIL_SUBSCRIBE_TYPE")
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
	
	
}
