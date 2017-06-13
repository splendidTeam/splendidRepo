package com.baozun.nebula.web.controller.member.form;

import java.util.Date;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baozun.nebula.command.MemberPersonalDataCommand;
import com.baozun.nebula.web.command.MemberFrontendCommand;
import com.baozun.nebula.web.controller.BaseForm;
import com.feilong.core.DatePattern;
import com.feilong.core.Validator;
import com.feilong.core.date.DateUtil;

/**
 * 会员注册提交的属性
 * 
 * @author D.C
 * @author Viktor Huang
 * @date 2016年3月21日 下午8:03:29
 */
public class RegisterForm extends BaseForm{

	private static final long	serialVersionUID	= 1265756122959961908L;

	private static final Logger	LOGGER				= LoggerFactory.getLogger(RegisterForm.class);

	private String				loginMobile;

	private String				loginEmail;

	private String				loginName;

	/** 密码 */
	private String				password;

	/** 再次输入密码 */
	private String				repassword;

	/**
	 * 验证码【此功能已由botdetect captcha替代】
	 */
	@Deprecated
	private String				randomCode;

	/** 是否接收AD邮件:1 接收 0 不接收 */
	private int					receiveMail;

	/**
	 * 性别 1为男,2为女[‘1&2’以MemberPersonalData中的注释为准]
	 */
	private int					sex;

	private String				birthdayFromPage;
	
	private Date                birthday;

	/*******************************************/
	/** 省 */
	private String				province;

	/** 市 */
	private String				city;

	/** 区 */
	private String				area;

	/** 省 id */
	private Long				provinceId;

	/** 市id */
	private Long				cityId;

	/** 区 id */
	private Long				areaId;

	/*******************************************/

	private String				nickname;

	/**
	 * 是否愿意接收推送信息 可选值：1.接受，2.不接受
	 */
	private String				receiveMessage;

	private String				age;
	
	/**
	 * Mobile 注册的时候的 email
	 */
	private String              email;
	
	
	/**
	 * PC 注册的时候的 mobile
	 */
	private String              mobile;
	
	/** 手机短信验证码*/
	private String				smsCode;
	
	

	/**
	 * RegisterForm 对象装换为 MemberFrontendCommand
	 * 
	 * @return
	 */
	public MemberFrontendCommand toMemberFrontendCommand(){
		MemberFrontendCommand memberFrontendCommand = new MemberFrontendCommand();
		MemberPersonalDataCommand memberPersonalDataCommand = new MemberPersonalDataCommand();
		// 数据转换
		try{
			if(Validator.isNotNullOrEmpty(this.birthdayFromPage)){
				Date birthday=DateUtil.toDate(this.birthdayFromPage, DatePattern.COMMON_DATE);
				this.setBirthday(birthday);
			}
			BeanUtils.copyProperties(memberFrontendCommand, this);
			BeanUtils.copyProperties(memberPersonalDataCommand, this);

			memberFrontendCommand.setMemberPersonalDataCommand(memberPersonalDataCommand);

		}catch (Exception e){
			LOGGER.error("", e);
		}

		return memberFrontendCommand;
	}

	public String getAge(){
		return age;
	}

	public void setAge(String age){
		this.age = age;
	}
	

	public String getBirthdayFromPage() {
		return birthdayFromPage;
	}

	public void setBirthdayFromPage(String birthdayFromPage) {
		this.birthdayFromPage = birthdayFromPage;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getReceiveMessage(){
		return receiveMessage;
	}

	public void setReceiveMessage(String receiveMessage){
		this.receiveMessage = receiveMessage;
	}

	public String getPassword(){
		return password;
	}

	public void setPassword(String password){
		this.password = password;
	}

	/**
	 * @return the repassword
	 */
	public String getRepassword(){
		return repassword;
	}

	/**
	 * @param repassword
	 *            the repassword to set
	 */
	public void setRepassword(String repassword){
		this.repassword = repassword;
	}

	/**
	 * @return the randomCode
	 */
	public String getRandomCode(){
		return randomCode;
	}

	/**
	 * @param randomCode
	 *            the randomCode to set
	 */
	public void setRandomCode(String randomCode){
		this.randomCode = randomCode;
	}

	/**
	 * @return the loginMobile
	 */
	public String getLoginMobile(){
		return loginMobile;
	}

	/**
	 * @param loginMobile
	 *            the loginMobile to set
	 */
	public void setLoginMobile(String loginMobile){
		this.loginMobile = loginMobile;
	}

	/**
	 * @return the loginEmail
	 */
	public String getLoginEmail(){
		return loginEmail;
	}

	/**
	 * @param loginEmail
	 *            the loginEmail to set
	 */
	public void setLoginEmail(String loginEmail){
		this.loginEmail = loginEmail;
	}

	/**
	 * @return the loginName
	 */
	public String getLoginName(){
		return loginName;
	}

	/**
	 * @param loginName
	 *            the loginName to set
	 */
	public void setLoginName(String loginName){
		this.loginName = loginName;
	}

	/**
	 * @return the receiveMail
	 */
	public int getReceiveMail(){
		return receiveMail;
	}

	/**
	 * @param receiveMail
	 *            the receiveMail to set
	 */
	public void setReceiveMail(int receiveMail){
		this.receiveMail = receiveMail;
	}

	/**
	 * @return the province
	 */
	public String getProvince(){
		return province;
	}

	/**
	 * @param province
	 *            the province to set
	 */
	public void setProvince(String province){
		this.province = province;
	}

	/**
	 * @return the city
	 */
	public String getCity(){
		return city;
	}

	/**
	 * @param city
	 *            the city to set
	 */
	public void setCity(String city){
		this.city = city;
	}

	/**
	 * @return the area
	 */
	public String getArea(){
		return area;
	}

	/**
	 * @param area
	 *            the area to set
	 */
	public void setArea(String area){
		this.area = area;
	}

	/**
	 * @return the provinceId
	 */
	public Long getProvinceId(){
		return provinceId;
	}

	/**
	 * @param provinceId
	 *            the provinceId to set
	 */
	public void setProvinceId(Long provinceId){
		this.provinceId = provinceId;
	}

	/**
	 * @return the cityId
	 */
	public Long getCityId(){
		return cityId;
	}

	/**
	 * @param cityId
	 *            the cityId to set
	 */
	public void setCityId(Long cityId){
		this.cityId = cityId;
	}

	/**
	 * @return the areaId
	 */
	public Long getAreaId(){
		return areaId;
	}

	/**
	 * @param areaId
	 *            the areaId to set
	 */
	public void setAreaId(Long areaId){
		this.areaId = areaId;
	}

	/**
	 * @return the sex
	 */
	public int getSex(){
		return sex;
	}

	/**
	 * @param sex
	 *            the sex to set
	 */
	public void setSex(int sex){
		this.sex = sex;
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

	/**
	 * @return the smsCode
	 */
	public String getSmsCode() {
		return smsCode;
	}

	/**
	 * @param smsCode the smsCode to set
	 */
	public void setSmsCode(String smsCode) {
		this.smsCode = smsCode;
	}
	
	
	
}
