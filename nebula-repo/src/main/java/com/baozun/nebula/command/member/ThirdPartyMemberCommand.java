package com.baozun.nebula.command.member;

/**
 * 第三方登录成功后，基于第三方返回信息构造的会员对象
 * @author Benjamin.Liu
 *
 */
public class ThirdPartyMemberCommand {

	/**
	 * 第三方用户唯一标识
	 * */
	private String openId;
	
	/**
	 * Email
	 */
	private String email;
	
	/**
	 * 手机
	 */
	private String mobile;
	
	/**
	 * 昵称
	 */
	private String nickName;
	
	/**
	 * 年龄
	 */
	private String age;
	
	/**
	 * 性别
	 */
	private String sex;
	
	/**
	 * 会员来源，请参考Member中的相关常量定义
	 */
	private Integer source;
	
	/**
	 * 头像
	 */
	private String avatar;

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

	
	public String getAvatar(){
		return avatar;
	}

	
	public void setAvatar(String avatar){
		this.avatar = avatar;
	}
	
	
}
