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
package com.baozun.nebula.command;

import java.util.Date;
import java.util.List;

import com.baozun.nebula.model.product.ItemImage;

/**
 * 商品评论Command
 * 
 * @author xingyu
 * @date
 * @version
 */
public class RateCommand implements Command {

	private static final long	serialVersionUID	= -2151999748651929377L;
	private Long				id;
	/** 评分 */
	private Integer				score;
	/** 评论内容 */
	private String				content;
	/** 回复内容 */
	private String				reply;
	/** 会员Id */
	private Long				memberId;
	/** 订单行id */
	private Long				orderLineId;
	/** 登录名 */
	private String				loginName;
	/** 商品code */
	private String				itemCode;
	/** 商品name */
	private String				itemName;
	/** 商品Id */
	private Long				itemId;
	/** 生命周期 */
	private Long				lifecycle;
	/** 操作员Id */
	private Long				operatorId;
	/** 操作员登陆用户名 */
	private String				operatorRealName;
	/** 回复人员id */
	private Long				replierId;
	/** 回复人员登陆用户 */
	private String				replierRealName;
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

	private List<ItemImage>		itemPicUrlList;
	
	/** 添加字段--施工配送分数 */
	private Integer				score2;

	public Integer getScore2() {
		return score2;
	}

	public void setScore2(Integer score2) {
		this.score2 = score2;
	}

	public String getImg_names() {
		return img_names;
	}

	public void setImg_names(String img_names) {
		this.img_names = img_names;
	}

	/** 添加字段---图片名称 */
	private String				img_names;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getReply() {
		return reply;
	}

	public void setReply(String reply) {
		this.reply = reply;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Long getLifecycle() {
		return lifecycle;
	}

	public void setLifecycle(Long lifecycle) {
		this.lifecycle = lifecycle;
	}

	public Long getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(Long operatorId) {
		this.operatorId = operatorId;
	}

	public String getOperatorRealName() {
		return operatorRealName;
	}

	public void setOperatorRealName(String operatorRealName) {
		this.operatorRealName = operatorRealName;
	}

	public Long getReplierId() {
		return replierId;
	}

	public void setReplierId(Long replierId) {
		this.replierId = replierId;
	}

	public String getReplierRealName() {
		return replierRealName;
	}

	public void setReplierRealName(String replierRealName) {
		this.replierRealName = replierRealName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getPassTime() {
		return passTime;
	}

	public void setPassTime(Date passTime) {
		this.passTime = passTime;
	}

	public Date getDisableTime() {
		return disableTime;
	}

	public void setDisableTime(Date disableTime) {
		this.disableTime = disableTime;
	}

	public Date getReplyTime() {
		return replyTime;
	}

	public void setReplyTime(Date replyTime) {
		this.replyTime = replyTime;
	}

	public Date getLastReplyTime() {
		return lastReplyTime;
	}

	public void setLastReplyTime(Date lastReplyTime) {
		this.lastReplyTime = lastReplyTime;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public Long getOrderLineId() {
		return orderLineId;
	}

	public void setOrderLineId(Long orderLineId) {
		this.orderLineId = orderLineId;
	}

	public List<ItemImage> getItemPicUrlList() {
		return itemPicUrlList;
	}

	public void setItemPicUrlList(List<ItemImage> itemPicUrlList) {
		this.itemPicUrlList = itemPicUrlList;
	}

}
