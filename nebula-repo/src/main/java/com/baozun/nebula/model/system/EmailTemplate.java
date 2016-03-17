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
 * 
 * 邮件模板
 * @author Justin Hu
 *
 */
@Entity
@Table(name = "T_SYS_EMAIL_TEMPLATE")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class EmailTemplate extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8508168695659158393L;

	
	/**
	 * PK
	 */
	private Long				id;
	
	/**
	 * 模板编码
	 */
	private String				code;
	/**
	 * 模板名称
	 */
	private String				name;
	
	/**
	 * 模板描述
	 */
	private String				description;

	/**
	 * 后台操作员id
	 */
	private Long				optUserId;
	
	/**
	 * type:1为html
	 * type:2为text
	 */
	private Integer				type;

	/**
	 * 发件人
	 */
	private String				senderEmail;

	/**
	 * 主题
	 */
	private String				subject;

	/**
	 * 消息正文
	 */
	private String				body;


	/**
	 * 生命周期
	 */
	private Integer 			lifecycle;
	
	
	/**
	 * 创建时间
	 */
	private Date				createTime			= new Date();
	
	/**
	 * 修改时间
	 */
	private Date				modifyTime			= new Date();
	

	private Date				version;
	
	/**
	 * 发件人别名
	 */
	private String				senderAlias;


	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_SYS_EMAIL_TEMPLATE",sequenceName = "S_T_SYS_EMAIL_TEMPLATE",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_SYS_EMAIL_TEMPLATE")
	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}

	
	@Column(name = "CODE",length = 50)
	public String getCode() {
		return code;
	}


	public void setCode(String code) {
		this.code = code;
	}


	@Column(name = "NAME",length = 50)
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	@Column(name = "DESCRIPTION",length = 256)
	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	@Column(name = "OPT_USERID")
	public Long getOptUserId() {
		return optUserId;
	}


	public void setOptUserId(Long optUserId) {
		this.optUserId = optUserId;
	}


	@Column(name = "TYPE")
	public Integer getType() {
		return type;
	}


	public void setType(Integer type) {
		this.type = type;
	}


	@Column(name = "SENDER_EMAIL",length = 64)
	public String getSenderEmail() {
		return senderEmail;
	}


	public void setSenderEmail(String senderEmail) {
		this.senderEmail = senderEmail;
	}

	@Column(name = "SUBJECT",length = 64)
	public String getSubject() {
		return subject;
	}


	public void setSubject(String subject) {
		this.subject = subject;
	}


	@Column(name = "BODY")
	@Lob
	public String getBody() {
		return body;
	}


	public void setBody(String body) {
		this.body = body;
	}


	@Column(name = "LIFECYCLE")
	public Integer getLifecycle() {
		return lifecycle;
	}


	public void setLifecycle(Integer lifecycle) {
		this.lifecycle = lifecycle;
	}


	@Column(name = "CREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}


	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}


	@Column(name = "MODIFY_TIME")
	public Date getModifyTime() {
		return modifyTime;
	}


	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	
	@Column(name = "SENDER_ALIAS")
	public String getSenderAlias() {
		return senderAlias;
	}


	public void setSenderAlias(String senderAlias) {
		this.senderAlias = senderAlias;
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
