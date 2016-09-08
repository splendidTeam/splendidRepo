package com.baozun.nebula.command.product;

import java.util.Date;

import com.baozun.nebula.command.Command;

/**
*
* @Title: MsgSendRecordCommand.java
* @author: weijun.zhang
* @date: 2016年8月10日 下午4:02:24
* @version 
*/

public class MsgSendRecordCommand implements Command{
	
	private static final long serialVersionUID = 1L;

	/**
	 * id
	 */
	private Long id;
	
	/**
	 * 接口标识(见接口列表)
	 */
	private String ifIdentify;
	
	/**
	 * 目标记录id,根据消息标识而保存不同的id，如订单id,支付id
	 */
	private Long targetId;
	
	/**
	 * 消息发送时间
	 */
	private Date sendTime;
	
	/**
	 * 消息反馈的时间
	 */
	private Date feedbackTime;
	
	/**
	 * 发送次数
	 */
	private Integer sendCount;
	
	/**
	 * 创建时间
	 */
	private Date createTime;
	
	/**
	 * 扩展信息(一些参数,如在售商品的分页信息)
	 */
	private String ext;
	
	/**
	 * 消息内容,存储时在这里的数据是加密的
	 */
	private String 	msgBody;
	
	
	private Date				version;


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getIfIdentify() {
		return ifIdentify;
	}


	public void setIfIdentify(String ifIdentify) {
		this.ifIdentify = ifIdentify;
	}


	public Long getTargetId() {
		return targetId;
	}


	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}


	public Date getSendTime() {
		return sendTime;
	}


	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}


	public Date getFeedbackTime() {
		return feedbackTime;
	}


	public void setFeedbackTime(Date feedbackTime) {
		this.feedbackTime = feedbackTime;
	}


	public Integer getSendCount() {
		return sendCount;
	}


	public void setSendCount(Integer sendCount) {
		this.sendCount = sendCount;
	}


	public Date getCreateTime() {
		return createTime;
	}


	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}


	public String getExt() {
		return ext;
	}


	public void setExt(String ext) {
		this.ext = ext;
	}


	public String getMsgBody() {
		return msgBody;
	}


	public void setMsgBody(String msgBody) {
		this.msgBody = msgBody;
	}


	public Date getVersion() {
		return version;
	}


	public void setVersion(Date version) {
		this.version = version;
	}


	
	
}
