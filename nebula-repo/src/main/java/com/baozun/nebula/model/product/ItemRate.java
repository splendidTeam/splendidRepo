package com.baozun.nebula.model.product;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.OptimisticLockType;

import com.baozun.nebula.model.BaseModel;

/**
 * 商品评论
 * 
 * @author xingyu
 * @date
 * @version
 */
@Entity
@Table(name = "T_PD_ITEM_RATE")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class ItemRate extends BaseModel {

	/**
	 * 待审核状态
	 */
	public static final Long LIFECYCLE_PENDING=5l;
	
	/**
	 * 审核通过
	 */
	public static final Long LIFECYCLE_PASS=1l;
	
	/**
	 * 删除
	 */
	public static final Long LIFECYCLE_DELETE=0l;
	
	private static final long	serialVersionUID	= 3378089752193285980L;
	/** PK */
	private Long				id;
	/** 评分 */
	private Integer				score;
	/** 评论内容 */
	private String				content;
	/** 回复内容 */
	private String				reply;
	/** 会员Id */
	private Long				memberId;
	/** 商品Id */
	private Long				itemId;
	/** 订单行id */
	private Long				orderLineId;
	/** 生命周期 5待审核中1代表通过0代表已删除 */
	private Long				lifecycle;
	/** 操作员Id */
	private Long				operatorId;
	/** 回复人员id */
	private Long				replierId;
	/** 创建时间 */
	private Date				createTime;
	/** 审核通过时间 */
	private Date				passTime;
	/** 删除时间(内部是禁用)id */
	private Date				disableTime;
	/** 回复时间 */
	private Date				replyTime;
	/** 最后回复时间id */
	private Date				lastReplyTime;

	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_PD_ITEM_RATE", sequenceName = "S_T_PD_ITEM_RATE", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_T_PD_ITEM_RATE")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "SCORE")
	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	@Column(name = "CONTENT")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "REPLY")
	public String getReply() {
		return reply;
	}

	public void setReply(String reply) {
		this.reply = reply;
	}

	@Column(name = "MEMBER_ID")
	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	@Column(name = "ITEM_ID")
	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	@Column(name = "LIFECYCLE")
	public Long getLifecycle() {
		return lifecycle;
	}

	public void setLifecycle(Long lifecycle) {
		this.lifecycle = lifecycle;
	}

	@Column(name = "OPERATOR_ID")
	public Long getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}

	@Column(name = "REPLIER_ID")
	public Long getReplierId() {
		return replierId;
	}

	public void setReplierId(Long replierId) {
		this.replierId = replierId;
	}

	@Column(name = "CREATE_TIME")
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Column(name = "PASS_TIME")
	public Date getPassTime() {
		return passTime;
	}

	public void setPassTime(Date passTime) {
		this.passTime = passTime;
	}

	@Column(name = "DISABLE_TIME")
	public Date getDisableTime() {
		return disableTime;
	}

	public void setDisableTime(Date disableTime) {
		this.disableTime = disableTime;
	}

	@Column(name = "REPLY_TIME")
	public Date getReplyTime() {
		return replyTime;
	}

	public void setReplyTime(Date replyTime) {
		this.replyTime = replyTime;
	}

	@Column(name = "LAST_REPLY_TIME")
	public Date getLastReplyTime() {
		return lastReplyTime;
	}

	public void setLastReplyTime(Date lastReplyTime) {
		this.lastReplyTime = lastReplyTime;
	}

	@Column(name = "ORDER_LINE_ID")
	public Long getOrderLineId() {
		return orderLineId;
	}

	public void setOrderLineId(Long orderLineId) {
		this.orderLineId = orderLineId;
	}

}
