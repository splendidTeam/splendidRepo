package com.baozun.nebula.command.wechat;

import java.io.Serializable;

public class WechatJsApiConfigCommand implements Serializable {
	private static final long serialVersionUID = -2735450144368257538L;
	private String appId;
	private String signature;
	private String timeStamp;
	private String nonceStr;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getNonceStr() {
		return nonceStr;
	}

	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}

}
