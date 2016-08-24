package com.baozun.nebula.sdk.command.member;

import com.baozun.nebula.command.Command;

public class MemberCommand implements Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3285230772055781744L;

	/**
	 * id
	 */
	private Long id;

	/**
	 * 登录名
	 */
	private String loginName;

	/**
	 * 登录邮箱
	 */
	private String loginEmail;

	/**
	 * 登录手机
	 */
	private String loginMobile;
	
	/**
	 * 盐值
	 * 密码加密使用了新的迭代加盐hash算法进行加密
	 * 这里需要将盐值进行存储
	 */
	private String salt;

	/**
	 * 密码
	 */
	private String password;
	/**
	 * BrandStore迁移的密码
	 */
	private String oldPassword;
	/**
	 * 第三方标识
	 */
	private String thirdPartyIdentify;

	/**
	 * 来源： 自注册 微博登录 QQ登录 等。 value在ChooseOption中配置
	 */
	private Integer source;

	/**
	 * 类型: 自有会员 自注册会员 第三方会员 等。 value在ChooseOption中配置
	 */
	private Integer type;

	/**
	 * 是否已绑定分类:1表示已加入 0表示未加入
	 */
	private Integer isaddgroup;

	/**
	 * 性别 ：0表示男 1表示女
	 */
	private Integer sex;

	/**
	 * 生日
	 */
	private String birthday;

	private String repassword;

	/**
	 * 生命周期
	 */
	private Integer lifecycle;

	/**
	 * 真实姓名
	 */
	private String realName;

	/**
	 * 1 接收 0 不接收
	 */
	private Integer receiveMail;

	private String newPassword;
	
	/**
	 * 分组ID，用于处理会员间绑定关系 
	 */
	private Long groupId;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getLoginEmail() {
		return loginEmail;
	}

	public void setLoginEmail(String loginEmail) {
		this.loginEmail = loginEmail;
	}

	public String getLoginMobile() {
		return loginMobile;
	}

	public void setLoginMobile(String loginMobile) {
		this.loginMobile = loginMobile;
	}
	
	public String getSalt() {
		return salt;
	}
	
	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getThirdPartyIdentify() {
		return thirdPartyIdentify;
	}

	public void setThirdPartyIdentify(String thirdPartyIdentify) {
		this.thirdPartyIdentify = thirdPartyIdentify;
	}

	public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getIsaddgroup() {
		return isaddgroup;
	}

	public void setIsaddgroup(Integer isaddgroup) {
		this.isaddgroup = isaddgroup;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getRepassword() {
		return repassword;
	}

	public void setRepassword(String repassword) {
		this.repassword = repassword;
	}

	public Integer getLifecycle() {
		return lifecycle;
	}

	public void setLifecycle(Integer lifecycle) {
		this.lifecycle = lifecycle;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public Integer getReceiveMail() {
		return receiveMail;
	}

	
	/**   
	 * get groupId  
	 * @return groupId  
	 */
	public Long getGroupId(){
		return groupId;
	}

	
	/**
	 * set groupId 
	 * @param groupId
	 */
	public void setGroupId(Long groupId){
		this.groupId = groupId;
	}

	public void setReceiveMail(Integer receiveMail) {
		this.receiveMail = receiveMail;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

}
