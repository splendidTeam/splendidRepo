/**
 * Copyright (c) 2013 Baozun All Rights Reserved.
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
package com.baozun.nebula.model.sns;

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
 * 用户咨询实体类
 * 
 * @author Tianlong.Zhang
 * 
 */
@Entity
@Table(name = "T_SNS_CONSULTANTS")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class Consultants extends BaseModel {

	private static final long	serialVersionUID	= 6258582694506221527L;

	/** 状态 */
	public static final Integer	STATUS_UNRESPONSED	= 1;
	public static final Integer	STATUS_RESPONSED	= 3;
	public static final Integer	STATUS_DELAY		= 2;

	/** 公示 */
	public static final Integer	PUBLISH_MARK_YES	= 1;
	public static final Integer	PUBLISH_MARK_NO		= 0;

	/** 咨询序号 **/
	private Long				id;

	/** 商品编号 **/
	private Long				itemId;

	/** 咨询类型编号 **/
	private Integer				consultype;

	/** 咨询内容 **/
	private String				content;

	/** 创建会员编号 **/
	private Long				memberId;

	/** 创建时间 **/
	private Date				createTime;

	/** 状态编号 **/
	private Integer				lifeCycle;

	/** **/
	private Integer				publishMark;

	/** 延时人员 **/
	private Long				responseId;

	/** 延时时间 **/
	private Date				responseTime;

	/** 回复内容 **/
	private String				resolveNote;

	/** 回复人员编号 **/
	private Long				resolveId;

	/** 回复时间 **/
	private Date				resolveTime;

	/** 更新人员编号 **/
	private Long				lastUpdateId;

	/** 更新时间 **/
	private Date				lastUpdateTime;

	/** 公示人员编号 **/
	private Long				publishId;

	/** 公示时间 **/
	private Date				publishTime;

	/** 取消公示人员编号 **/
	private Long				unPublishId;

	/** 取消公示时间 **/
	private Date				unPublishTime;

	private Date				version;

	/**
	 * @return the id
	 */
	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_SNS_CONSULTANTS", sequenceName = "S_T_SNS_CONSULTANTS", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_SNS_CONSULTANTS")
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the itemCode
	 */
	@Column(name = "itemId")
    @Index(name = "IDX_SNS_CONSULTANTS_ITEMID")
	public Long getItemId() {
		return itemId;
	}

	/**
	 * @param itemCode
	 *            the itemCode to set
	 */
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	/**
	 * @return the consultTypeId
	 */
	@Column(name = "consultype")
	public Integer getConsultype() {
		return consultype;
	}

	/**
	 * @param consultTypeId
	 *            the consultTypeId to set
	 */
	public void setConsultype(Integer consultype) {
		this.consultype = consultype;
	}

	/**
	 * @return the content
	 */
	@Column(name = "CONTENT", length = 200)
	public String getContent() {
		return content;
	}

	/**
	 * @param content
	 *            the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the memberId
	 */
	@Column(name = "MEMBERID")
    @Index(name = "IDX_SNS_CONSULTANTS_MEMBERID")
	public Long getMemberId() {
		return memberId;
	}

	/**
	 * @param memberId
	 *            the memberId to set
	 */
	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	/**
	 * @return the createTime
	 */
	@Column(name = "CREATETIME")
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime
	 *            the createTime to set
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return the statusId
	 */
	@Column(name = "lifeCycle")
    @Index(name = "IDX_SNS_CONSULTANTS_LIFECYCLE")
	public Integer getLifeCycle() {
		return lifeCycle;
	}

	/**
	 * @param statusId
	 *            the statusId to set
	 */
	public void setLifeCycle(Integer lifeCycle) {
		this.lifeCycle = lifeCycle;
	}

	/**
	 * @return the publishMark
	 */
	@Column(name = "PUBLISHMARK")
	public Integer getPublishMark() {
		return publishMark;
	}

	/**
	 * @param publishMark
	 *            the publishMark to set
	 */
	public void setPublishMark(Integer publishMark) {
		this.publishMark = publishMark;
	}

	/**
	 * @return the responseId
	 */
	@Column(name = "RESPONSEID")
	public Long getResponseId() {
		return responseId;
	}

	/**
	 * @param responseId
	 *            the responseId to set
	 */
	public void setResponseId(Long responseId) {
		this.responseId = responseId;
	}

	/**
	 * @return the responseTime
	 */
	@Column(name = "RESPONSETIME")
	public Date getResponseTime() {
		return responseTime;
	}

	/**
	 * @param responseTime
	 *            the responseTime to set
	 */
	public void setResponseTime(Date responseTime) {
		this.responseTime = responseTime;
	}

	/**
	 * @return the responseNote
	 */
	@Column(name = "resolveNote", length = 200)
	public String getResolveNote() {
		return resolveNote;
	}

	/**
	 * @param responseNote
	 *            the responseNote to set
	 */
	public void setResolveNote(String resolveNote) {
		this.resolveNote = resolveNote;
	}

	/**
	 * @return the resolveId
	 */
	@Column(name = "RESOLVEID")
	public Long getResolveId() {
		return resolveId;
	}

	/**
	 * @param resolveId
	 *            the resolveId to set
	 */
	public void setResolveId(Long resolveId) {
		this.resolveId = resolveId;
	}

	/**
	 * @return the resolveTime
	 */
	@Column(name = "RESOLVETIME")
	public Date getResolveTime() {
		return resolveTime;
	}

	/**
	 * @param resolveTime
	 *            the resolveTime to set
	 */
	public void setResolveTime(Date resolveTime) {
		this.resolveTime = resolveTime;
	}

	/**
	 * @return the updateId
	 */
	@Column(name = "lastUpdateId")
	public Long getLastUpdateId() {
		return lastUpdateId;
	}

	/**
	 * @param updateId
	 *            the updateId to set
	 */
	public void setLastUpdateId(Long lastUpdateId) {
		this.lastUpdateId = lastUpdateId;
	}

	/**
	 * @return the updateTime
	 */
	@Column(name = "lastUpdateTime")
	public Date getLastUpdateTime() {
		return lastUpdateTime;
	}

	/**
	 * @param updateTime
	 *            the updateTime to set
	 */
	public void setLastUpdateTime(Date lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	/**
	 * @return the publishId
	 */
	@Column(name = "PUBLISHID")
	public Long getPublishId() {
		return publishId;
	}

	/**
	 * @param publishId
	 *            the publishId to set
	 */
	public void setPublishId(Long publishId) {
		this.publishId = publishId;
	}

	/**
	 * @return the publishTime
	 */
	@Column(name = "PUBLISHTIME")
	public Date getPublishTime() {
		return publishTime;
	}

	/**
	 * @param publishTime
	 *            the publishTime to set
	 */
	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
	}

	/**
	 * @return the unPublishId
	 */
	@Column(name = "UNPUBLISHID")
	public Long getUnPublishId() {
		return unPublishId;
	}

	/**
	 * @param unPublishId
	 *            the unPublishId to set
	 */
	public void setUnPublishId(Long unPublishId) {
		this.unPublishId = unPublishId;
	}

	/**
	 * @return the unPublishTime
	 */
	@Column(name = "UNPUBLISHTIME")
	public Date getUnPublishTime() {
		return unPublishTime;
	}

	/**
	 * @param unPublishTime
	 *            the unPublishTime to set
	 */
	public void setUnPublishTime(Date unPublishTime) {
		this.unPublishTime = unPublishTime;
	}

	public void setVersion(Date version) {
		this.version = version;
	}

	@Version
	@Column(name = "VERSION")
	public Date getVersion() {
		return version;
	}

}
