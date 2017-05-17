package com.baozun.nebula.model.system;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.OptimisticLockType;

import com.baozun.nebula.model.BaseModel;

/**
 * 系统审计日志历史表
 * @author xingyu.liu
 *
 */
@Entity
@Table(name = "T_SYS_AUDITLOG_HISTORY")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class SysAuditLogHistory extends BaseModel{

	private static final long serialVersionUID = 3944171883584905936L;

	private Long		id;
	
	/**
	 * 请求的uri
	 */
	private String		uri;
	
	/**
	 * 请求的参数
	 */
	private String		parameters;

	/**
	 * 请求的类型（post，get）
	 */
	private String		method;

	/**
	 * 操作人IP
	 */
	private String		ip;
	
	/**
	 * 返回的结果码
	 */
	private String		responseCode;
	
	/**
	 * 返回的异常
	 */
	private String      exception;
	
	/**
	 * 操作人
	 */
	private Long        operaterId;
	
	/**
	 * 操作时间
	 */
	private Date		createTime;
	
	/**
	 * 归档时间
	 */
	private Date        archiveTime;
	
	
	@Id
	@Column(name = "ID")
//	@SequenceGenerator(name = "SEQ_T_SYS_AUDITLOG_HISTORY",sequenceName = "S_T_SYS_AUDITLOG_HISTORY",allocationSize = 1)
//	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_SYS_AUDITLOG_HISTORY")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "URI")
    @Index(name = "IDX_AUDITLOG_HISTORY_URI")
	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}
	
	@Column(name = "PARAMETERS")
	@Lob
	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	@Column(name = "METHOD")
	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	@Column(name = "IP")
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Column(name = "RESPONSE_CODE")
	public String getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}

	@Column(name = "EXCEPTION",length=1000)
	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

	@Column(name = "OPERATER_ID")
	public Long getOperaterId() {
		return operaterId;
	}

	public void setOperaterId(Long operaterId) {
		this.operaterId = operaterId;
	}

	@Column(name = "CREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "ARCHIVE_TIME")
	public Date getArchiveTime() {
		return archiveTime;
	}

	public void setArchiveTime(Date archiveTime) {
		this.archiveTime = archiveTime;
	}
	
	
	
}
