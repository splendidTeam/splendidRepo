package com.baozun.nebula.command;


/**
 * @author shouqun.li
 * @version 2016年3月25日 下午1:38:33
 */
public class MessageCommand implements Command{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1647959758917172405L;
	
	/** 手机号 **/
	private String mobile;
	/** 手机验证码 **/
	private String securityCode;
	/** 发送内容 **/
	private String content;
	/** 发送时间 **/
	//private Date sendTime;
	/** 发送类型  (0、注册短信， 1、忘记密码， 2、订单类型短信， 3、接收短信)**/
	private Integer type;
	/** 发送结果  (0、代表成功， 1、失败)**/
	//private Integer result;
	/** 扩展码 (区分同一个用户发送多条短信)**/
	//private Integer ext; 
	/** 定时发送   当为空的时候是立即发送 格式为：XXX-XXX-XXX-XXXXX **/
	//private Date fixedSendTime;
	/** 接收短信时间  **/
	//private Date reciveTime;
	/** 发送信息的ip **/
	//private String clientIp;
	/** 短信的seq **/
	//private Integer seq;
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getSecurityCode() {
		return securityCode;
	}
	public void setSecurityCode(String securityCode) {
		this.securityCode = securityCode;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	/*public Date getFixedSendTime() {
		return fixedSendTime;
	}
	public void setFixedSendTime(Date fixedSendTime) {
		this.fixedSendTime = fixedSendTime;
	}*/
}
