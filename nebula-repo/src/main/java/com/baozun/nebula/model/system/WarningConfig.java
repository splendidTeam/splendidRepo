/**
 * Copyright (c) 2012 Baozun All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Baozun.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Baozun.
 *
 * BAOZUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. BAOZUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
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

import org.hibernate.annotations.OptimisticLockType;

import com.baozun.nebula.command.Command;
import com.baozun.nebula.model.BaseModel;

/**
 * 警告设置
 * 
 * @author Justin
 *
 */
@Entity
@Table(name = "T_SYS_WARNING_CONFIG")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class WarningConfig extends BaseModel implements Command  {

	private static final long serialVersionUID = -8595879505664196069L;

	/**
     * 告警code
     */
    public static final String ORDER_NO_CHANGE_WARNING_CODE = "payOrderNoChangStatus";
    
    public static final String NOT_CANCEL_ORDER_NO_FINISH_WARNING_CODE = "notCancelOrderNoFinish";
    
    public static final String FULL_INVENTORY_SYNC_LATER_WARNING_CODE = "fullInventorySyncLater";
    
    public static final String SYNC_LOGISTIC_ERROR_NOT_CODE_WARNING_CODE = "syncLogisticErrorByNotCode";
	/**
	 * PK
	 */
	private Long				id;
	
	/**
	 * 编码
	 * 作为唯一编码
	 */
	private String				code;
	
	/**
	 * 名称
	 */
	private String				name;
	
	/**
	 * 描述
	 */
	private String				warningDesc;
	
	/**
	 * 时间参数 后缀说明 
	 * y表示年 M表示月 d表示天
	 * h表示小时 m表示分钟 s表示秒
	 */
	private String				timeParam;
	
	/**
	 * 接收者多个,使用逗号分隔
	 */
	private String				receivers;
	
	/**
	 * 如使用邮件还是短信
	 * 1为邮件2为短信
	 */
	private Integer				type;
	
	/**
	 * 模板code, 邮件或短信的模板code
	 */
	private String				templateCode;
	
	/**
	 * 告警级别 1-低 2-中 3-高   数字越大代表着越紧急
	 */
	private Integer				level;
	
	
	/**
	 * 告警的次数(针对同一个消息，同一个收件人)
	 */
	private Integer				count;
	
	/**
	 * 1表示启用，0表示未启用，未启用状态时则不发送任何告警信息
	 */
	private Integer 			lifecycle;
	
	/**
	 * VERSION
	 */
	private Date 				version;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_SYS_WARNING_CONFIG",sequenceName = "S_T_SYS_WARNING_CONFIG",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_SYS_WARNING_CONFIG")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
	@Column(name = "CODE")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	

	@Column(name = "COUNT")
	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	@Column(name = "TIMEPARAM")
	public String getTimeParam() {
		return timeParam;
	}

	@Column(name = "WARNING_DESC")
	public String getWarningDesc() {
		return warningDesc;
	}

	
	public void setWarningDesc(String warningDesc) {
		this.warningDesc = warningDesc;
	}

	public void setTimeParam(String timeParam) {
		this.timeParam = timeParam;
	}

	@Column(name = "RECEIVERS")
	public String getReceivers() {
		return receivers;
	}

	public void setReceivers(String receivers) {
		this.receivers = receivers;
	}

	@Column(name = "TYPE")
	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	@Column(name = "TEMPLATE_CODE")
	public String getTemplateCode() {
		return templateCode;
	}

	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}

	@Column(name = "LEVEL")
	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	@Column(name = "LIFECYCLE")
	public Integer getLifecycle() {
		return lifecycle;
	}

	public void setLifecycle(Integer lifecycle) {
		this.lifecycle = lifecycle;
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
