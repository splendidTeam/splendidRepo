/**
 * Copyright (c) 2010 Jumbomart All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Jumbomart.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Jumbo.
 *
 * JUMBOMART MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. JUMBOMART SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.web.controller.product.viewcommand;

import java.util.Date;
import java.util.List;

import com.baozun.nebula.web.controller.BaseViewCommand;

/**
 * 商品评论
 *
 * @see com.baozun.nebula.web.controller.product.viewcommand.ReviewMemberViewCommand
 */
public class ItemReviewViewCommand extends BaseViewCommand {

	private static final long serialVersionUID = 6722284519719024559L;

	/** 评分 */
	private Integer score;
	
	/** 评论内容 */
	private String content;
	
	/** 评论晒图url，缩略图和大图应该是一套，页面上根据需要拼接不同的规格 */
	private List<String> images;
	
	/** 评论时间（评论的创建时间） */
	private Date reviewTime;
	
	/** 评论会员 */
	private ReviewMemberViewCommand	member;
	
	/** 回复内容 */
	private String reply;
	
	/** 回复时间 */
	private Date replyTime;

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

	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}

	public Date getReviewTime() {
		return reviewTime;
	}

	public void setReviewTime(Date reviewTime) {
		this.reviewTime = reviewTime;
	}

	public ReviewMemberViewCommand getMember() {
		return member;
	}

	public void setMember(ReviewMemberViewCommand member) {
		this.member = member;
	}

	public String getReply() {
		return reply;
	}

	public void setReply(String reply) {
		this.reply = reply;
	}

	public Date getReplyTime() {
		return replyTime;
	}

	public void setReplyTime(Date replyTime) {
		this.replyTime = replyTime;
	}
	
}
