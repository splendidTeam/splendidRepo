package com.baozun.nebula.utilities.integration.oauth;

import java.io.Serializable;

public class ThirdPartyMember implements Serializable {


	private static final long serialVersionUID = -3362531392821219816L;

	private String uid;
	
	private String userName;
	
	private String nickName;
	

	private String errorCode;
	
	private String errorDescription;


	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}
	
	
}
