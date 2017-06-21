package com.baozun.nebula.command;

import java.math.BigDecimal;
import java.util.Date;

public class MemberPersonalDataCommand implements Command{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1221341154541433431L;

	private Long				id;

	private Long				memberId;

	private String				loginName;

	private Integer				lifecycle;

	private String				loginEmail;

	private String				loginMobile;

	private Integer				source;

	private Integer				type;

	private String				userPic;

	private String				nickname;

	private String				localRealName;

	private String				intelRealName;

	private Integer				sex;

	private String				bloodType;

	private Date				birthday;

	private String				marriage;

	private String				country;

	private String				province;

	private String				city;

	private String				district;

	/**
	 * 县(区)
	 */
	private String				area;

	/**
	 * 国家id
	 */
	private Long				countryId;

	/** 省 id */
	private Long				provinceId;

	/** 市id */
	private Long				cityId;

	/** 区 id */
	private Long				areaId;

	private String				address;

	private Integer				credentialsType;

	private String				credentialsNo;

	private String				typeName;

	private String				sexName;

	private String				credentialsTypeName;

	private String				lifeCycleName;

	private String				postCode;

	private String				company;

	private String				interest;
	/**
	 * @Deprecated 发现MemberPersonalData中没有short1字段,在这里就Deprecated掉
	 */
	@Deprecated
	private String				short1;

	private String				short2;

	private String				short3;

	private Integer				long1;

	private Integer				long2;

	private Integer				long3;

	private Integer				loginCount;

	private Date				registerTime;

	private Date				loginTime;

	private Date				payTime;

	private String				registerIp;

	private String				loginIp;

	private BigDecimal			cumulativeConAmount;

	public String getLifeCycleName(){
		return lifeCycleName;
	}

	public void setLifeCycleName(String lifeCycleName){
		this.lifeCycleName = lifeCycleName;
	}

	public String getCredentialsTypeName(){
		return credentialsTypeName;
	}

	public void setCredentialsTypeName(String credentialsTypeName){
		this.credentialsTypeName = credentialsTypeName;
	}

	public String getSexName(){
		return sexName;
	}

	public void setSexName(String sexName){
		this.sexName = sexName;
	}

	public String getTypeName(){
		return typeName;
	}

	public void setTypeName(String typeName){
		this.typeName = typeName;
	}

	public String getSourceName(){
		return sourceName;
	}

	public void setSourceName(String sourceName){
		this.sourceName = sourceName;
	}

	private String	sourceName;

	public String getCredentialsNo(){
		return credentialsNo;
	}

	public void setCredentialsNo(String credentialsNo){
		this.credentialsNo = credentialsNo;
	}

	public String getPostCode(){
		return postCode;
	}

	public void setPostCode(String postCode){
		this.postCode = postCode;
	}

	private String	email;

	private String	mobile;

	private String	QQ;

	private String	weibo;

	private String	weixin;

	private String	edu;

	private String	industy;

	private String	position;

	private String	salary;

	private String	workingLife;

	public String getLoginName(){
		return loginName;
	}

	public void setLoginName(String loginName){
		this.loginName = loginName;
	}

	public Integer getLifecycle(){
		return lifecycle;
	}

	public void setLifecycle(Integer lifecycle){
		this.lifecycle = lifecycle;
	}

	public String getLoginEmail(){
		return loginEmail;
	}

	public void setLoginEmail(String loginEmail){
		this.loginEmail = loginEmail;
	}

	public String getLoginMobile(){
		return loginMobile;
	}

	public void setLoginMobile(String loginMobile){
		this.loginMobile = loginMobile;
	}

	public Integer getSource(){
		return source;
	}

	public void setSource(Integer source){
		this.source = source;
	}

	public Integer getType(){
		return type;
	}

	public void setType(Integer type){
		this.type = type;
	}

	public Long getId(){
		return id;
	}

	public void setId(Long id){
		this.id = id;
	}

	public Long getMemberId(){
		return memberId;
	}

	public void setMemberId(Long memberId){
		this.memberId = memberId;
	}

	public String getUserPic(){
		return userPic;
	}

	public void setUserPic(String userPic){
		this.userPic = userPic;
	}

	public String getNickname(){
		return nickname;
	}

	public void setNickname(String nickname){
		this.nickname = nickname;
	}

	public String getLocalRealName(){
		return localRealName;
	}

	public void setLocalRealName(String localRealName){
		this.localRealName = localRealName;
	}

	public String getIntelRealName(){
		return intelRealName;
	}

	public void setIntelRealName(String intelRealName){
		this.intelRealName = intelRealName;
	}

	public Integer getSex(){
		return sex;
	}

	public void setSex(Integer sex){
		this.sex = sex;
	}

	public String getBloodType(){
		return bloodType;
	}

	public void setBloodType(String bloodType){
		this.bloodType = bloodType;
	}

	public Date getBirthday(){
		return birthday;
	}

	public void setBirthday(Date birthday){
		this.birthday = birthday;
	}

	public String getMarriage(){
		return marriage;
	}

	public void setMarriage(String marriage){
		this.marriage = marriage;
	}

	public String getCountry(){
		return country;
	}

	public void setCountry(String country){
		this.country = country;
	}

	public String getProvince(){
		return province;
	}

	public void setProvince(String province){
		this.province = province;
	}

	public String getCity(){
		return city;
	}

	public void setCity(String city){
		this.city = city;
	}

	public String getDistrict(){
		return district;
	}

	public void setDistrict(String district){
		this.district = district;
	}

	public String getAddress(){
		return address;
	}

	public void setAddress(String address){
		this.address = address;
	}

	public Integer getCredentialsType(){
		return credentialsType;
	}

	public void setCredentialsType(Integer credentialsType){
		this.credentialsType = credentialsType;
	}

	public String getEmail(){
		return email;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getMobile(){
		return mobile;
	}

	public void setMobile(String mobile){
		this.mobile = mobile;
	}

	public String getQQ(){
		return QQ;
	}

	public void setQQ(String qQ){
		QQ = qQ;
	}

	public String getWeibo(){
		return weibo;
	}

	public void setWeibo(String weibo){
		this.weibo = weibo;
	}

	public String getWeixin(){
		return weixin;
	}

	public void setWeixin(String weixin){
		this.weixin = weixin;
	}

	public String getEdu(){
		return edu;
	}

	public void setEdu(String edu){
		this.edu = edu;
	}

	public String getIndusty(){
		return industy;
	}

	public void setIndusty(String industy){
		this.industy = industy;
	}

	public String getPosition(){
		return position;
	}

	public void setPosition(String position){
		this.position = position;
	}

	public String getSalary(){
		return salary;
	}

	public void setSalary(String salary){
		this.salary = salary;
	}

	public String getWorkingLife(){
		return workingLife;
	}

	public void setWorkingLife(String workingLife){
		this.workingLife = workingLife;
	}

	public String getCompany(){
		return company;
	}

	public void setCompany(String company){
		this.company = company;
	}

	public String getInterest(){
		return interest;
	}

	public void setInterest(String interest){
		this.interest = interest;
	}

	public String getShort1(){
		return short1;
	}

	public void setShort1(String short1){
		this.short1 = short1;
	}

	public String getShort2(){
		return short2;
	}

	public void setShort2(String short2){
		this.short2 = short2;
	}

	public String getShort3(){
		return short3;
	}

	public void setShort3(String short3){
		this.short3 = short3;
	}

	public Integer getLong1(){
		return long1;
	}

	public void setLong1(Integer long1){
		this.long1 = long1;
	}

	public Integer getLong2(){
		return long2;
	}

	public void setLong2(Integer long2){
		this.long2 = long2;
	}

	public Integer getLong3(){
		return long3;
	}

	public void setLong3(Integer long3){
		this.long3 = long3;
	}

	public Integer getLoginCount(){
		return loginCount;
	}

	public void setLoginCount(Integer loginCount){
		this.loginCount = loginCount;
	}

	public Date getRegisterTime(){
		return registerTime;
	}

	public void setRegisterTime(Date registerTime){
		this.registerTime = registerTime;
	}

	public Date getLoginTime(){
		return loginTime;
	}

	public void setLoginTime(Date loginTime){
		this.loginTime = loginTime;
	}

	public Date getPayTime(){
		return payTime;
	}

	public void setPayTime(Date payTime){
		this.payTime = payTime;
	}

	public String getRegisterIp(){
		return registerIp;
	}

	public void setRegisterIp(String registerIp){
		this.registerIp = registerIp;
	}

	public String getLoginIp(){
		return loginIp;
	}

	public void setLoginIp(String loginIp){
		this.loginIp = loginIp;
	}

	public BigDecimal getCumulativeConAmount(){
		return cumulativeConAmount;
	}

	public void setCumulativeConAmount(BigDecimal cumulativeConAmount){
		this.cumulativeConAmount = cumulativeConAmount;
	}

	public Long getCountryId(){
		return countryId;
	}

	public void setCountryId(Long countryId){
		this.countryId = countryId;
	}

	public Long getProvinceId(){
		return provinceId;
	}

	public void setProvinceId(Long provinceId){
		this.provinceId = provinceId;
	}

	public Long getCityId(){
		return cityId;
	}

	public void setCityId(Long cityId){
		this.cityId = cityId;
	}

	public String getArea(){
		return area;
	}

	public void setArea(String area){
		this.area = area;
	}

	public Long getAreaId(){
		return areaId;
	}

	public void setAreaId(Long areaId){
		this.areaId = areaId;
	}

}
