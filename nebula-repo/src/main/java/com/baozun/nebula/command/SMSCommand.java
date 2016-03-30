package com.baozun.nebula.command;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author D.C
 * @version 2016年3月25日 下午1:38:33
 */
public class SMSCommand implements Command{
	private static final long serialVersionUID = 1647959758917172405L;
	
	/** 手机号 **/
	private String mobile;
	
	/** 模版编号 **/
	private String templateCode;
	/**
	 * 模版参数
	 */
	private Map<String, Object> vars = new HashMap<String, Object>();
	
	/** 发送时间， 定时发送请填写未来时间 **/
	private Date sendTime;
	
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getTemplateCode() {
		return templateCode;
	}
	public void setTemplateCode(String templateCode) {
		this.templateCode = templateCode;
	}
	public Map<String, Object> getVars() {
		return vars;
	}
	public void setVars(Map<String, Object> vars) {
		this.vars = vars;
	}
	public Date getSendTime() {
		return sendTime;
	}
	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
	
	public void addVar(String key, Object value) {
		this.getVars().put(key, value);
	}
}
