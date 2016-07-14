package com.baozun.nebula.utilities.integration.oauth;

import java.io.Serializable;

public class ThirdPartyMember implements Serializable{

	private static final long	serialVersionUID	= -3362531392821219816L;

	private String				uid;
	/**
	 * 集团用户ID, 常用于微信
	 */
	private String unionId;

	private String				userName;

	private String				nickName;

	/**
	 * 头像
	 */
	private String				avatar;

	private String				errorCode;

	private String				errorDescription;

	/**
	 * 性别
	 */
	private String				sex;

	public String getUid(){
		return uid;
	}

	public void setUid(String uid){
		this.uid = uid;
	}

	public String getUserName(){
		return userName;
	}

	public void setUserName(String userName){
		this.userName = userName;
	}

	public String getNickName(){
		return nickName;
	}

	public void setNickName(String nickName){
		this.nickName = nickName;
	}

	public String getErrorCode(){
		return errorCode;
	}

	public void setErrorCode(String errorCode){
		this.errorCode = errorCode;
	}

	public String getErrorDescription(){
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription){
		this.errorDescription = errorDescription;
	}

	public String getAvatar(){
		return avatar;
	}

	public void setAvatar(String avatar){
		this.avatar = avatar;
	}

	public String getSex(){
		return sex;
	}

	public void setSex(String sex){
		this.sex = sex;
	}

	public String getUnionId() {
		return unionId;
	}

	public void setUnionId(String unionId) {
		this.unionId = unionId;
	}

}
