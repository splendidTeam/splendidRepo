/**
 * Copyright (c) 2012 Baozun All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Baozun.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Baozun.
 *
 * BAOZUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. BAOZUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.model.member;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.OptimisticLockType;

import com.baozun.nebula.model.BaseModel;

/**
 * 会员个人信息
 * id使用Member的关键
 * @author Justin
 *
 */
@Entity
@Table(name = "T_MEM_PERSONAL_DATA")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class MemberPersonalData extends BaseModel {



	/**
	 */
	private static final long serialVersionUID = 6571970111783325260L;

	/**
	 * id
	 */
	private Long id;
	
	

	private Date version;
	
	/**
	 * 用户头像
	 */
	private String userPic;
	
	/**
	 * 昵称
	 */
	private String nickname;
	
	
	/**
	 * 本地真实名
	 */
	private String localRealName;
	
	/**
	 * 国际真实名
	 */
	private String intelRealName;

	/**
	 * 性别 1为男,2为女
	 */
	private Integer sex;
	
	/**
	 * 血型
	 */
	private String bloodType;
	
	/**
	 * 生日
	 */
	private Date birthday;
	
	/**
	 * 婚姻状况
	 */
	private String marriage;
	
	/**
	 * 国家
	 */
	private String country;
	
	/**
	 * 省
	 */
	private String province;
	
	/**
	 * 市
	 */
	private String city;
	
	/**
	 * 县(区)
	 */
	private String area;
	
	/** 镇*/
	private String				town;
	
	/**
	 * 国家id 
	 */
	private Long 				countryId;
	
	/** 省 id*/
	private Long				provinceId;
	
	/** 市id */
	private Long			    cityId;
	
	/** 区 id*/
	private Long			    areaId;
	
	/** 镇id*/
	private Long				townId;

	/**
	 * 详细地址
	 */
	private String address;
	
	/**
	 * 证件的类型
	 * 1 身分证
	 * 2 学生证
	 * 3 军官证
	 */
	private Integer credentialsType;
	
	/**
	 * 证件号码
	 */
	private String credentialsNo;
	
	/**
	 * 邮箱
	 */
	private String email;
	
	/**
	 * 手机
	 */
	private String mobile;
	
	/**
	 * qq
	 */
	private String qq;
	
	/**
	 * 微博号
	 */
	private String weibo;
	
	/**
	 * 微信
	 */
	private String weixin;
	
	/**
	 * 学历
	 */
	private String edu;
	
	/**
	 * 行业
	 */
	private String industy;
	
	/**
	 * 职位
	 */
	private String position;
	
	/**
	 * 薪资
	 */
	private String salary;
	
	/**
	 * 工作年限
	 */
	private String workingLife;
	
	/**
	 * 公司名称
	 */
	private String company;
	
	/**
	 * 兴趣
	 */
	private String interest;
	
	
	/**
	 * 邮政编码
	 */
	private String postCode;
	
	/**
	 * 1 接收
	 * 0 不接收
	 */
	private Integer receiveMail;
	
	/**
	 * 保留字段短
	 */
	private String short2;
	
	/**
	 * 保留字段短
	 */
	private String short3;
	
	/**
	 * 保留字段短
	 */
	private String short4;
	
	/**
	 * 保留字段短
	 */
	private String short5;
	
	/**
	 * 保留字段长
	 */
	private String long1;
	
	/**
	 * 保留字段长
	 */
	private String long2;
	
	/**
	 * 保留字段长
	 */
	private String long3;
	
	/**
	 * 保留字段长
	 */
	private String long4;
	
	/**
	 * 保留字段长
	 */
	private String long5;
	

	
	
	@Id
	@Column(name = "ID")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	
	@Version
	@Column(name = "VERSION")
	public Date getVersion() {
		return version;
	}

	public void setVersion(Date version) {
		this.version = version;
	}

	@Column(name = "USER_PIC")
	public String getUserPic() {
		return userPic;
	}

	public void setUserPic(String userPic) {
		this.userPic = userPic;
	}

	@Column(name = "NICKNAME")
	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	@Column(name = "LOCAL_REAL_NAME")
	public String getLocalRealName() {
		return localRealName;
	}

	public void setLocalRealName(String localRealName) {
		this.localRealName = localRealName;
	}

	@Column(name = "INTEL_REAL_NAME")
	public String getIntelRealName() {
		return intelRealName;
	}

	public void setIntelRealName(String intelRealName) {
		this.intelRealName = intelRealName;
	}

	@Column(name = "SEX")
	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	@Column(name = "BLOOD_TYPE")
	public String getBloodType() {
		return bloodType;
	}

	public void setBloodType(String bloodType) {
		this.bloodType = bloodType;
	}

	@Column(name = "BIRTHDAY")
	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	@Column(name = "MARRIAGE")
	public String getMarriage() {
		return marriage;
	}

	public void setMarriage(String marriage) {
		this.marriage = marriage;
	}
	
	@Column(name = "COUNTRY_ID")
	public Long getCountryId() {
		return countryId;
	}

	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}

	@Column(name = "PROVINCE_ID")
	public Long getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Long provinceId) {
		this.provinceId = provinceId;
	}

	@Column(name = "CITY_ID")
	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	@Column(name = "AREA_ID")
	public Long getAreaId() {
		return areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	@Column(name = "TOWN_ID")
	public Long getTownId() {
		return townId;
	}

	public void setTownId(Long townId) {
		this.townId = townId;
	}


	@Column(name = "COUNTRY")
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	@Column(name = "PROVINCE")
	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	@Column(name = "CITY")
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	@Column(name = "AREA")
	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}
	
	
	
	@Column(name = "TOWN")
	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		this.town = town;
	}

	@Column(name = "ADDRESS")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Column(name = "CREDENTIALS_TYPE")
	public Integer getCredentialsType() {
		return credentialsType;
	}

	public void setCredentialsType(Integer credentialsType) {
		this.credentialsType = credentialsType;
	}

	@Column(name = "CREDENTIALS_NO")
	public String getCredentialsNo() {
		return credentialsNo;
	}

	public void setCredentialsNo(String credentialsNo) {
		this.credentialsNo = credentialsNo;
	}

	@Column(name = "EMAIL")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Column(name = "MOBILE")
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Column(name = "QQ")
	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	@Column(name = "WEIBO")
	public String getWeibo() {
		return weibo;
	}

	public void setWeibo(String weibo) {
		this.weibo = weibo;
	}

	@Column(name = "WEIXIN")
	public String getWeixin() {
		return weixin;
	}

	public void setWeixin(String weixin) {
		this.weixin = weixin;
	}

	@Column(name = "EDU")
	public String getEdu() {
		return edu;
	}

	public void setEdu(String edu) {
		this.edu = edu;
	}

	@Column(name = "INDUSTY")
	public String getIndusty() {
		return industy;
	}

	public void setIndusty(String industy) {
		this.industy = industy;
	}

	@Column(name = "POSITION")
	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	@Column(name = "SALARY")
	public String getSalary() {
		return salary;
	}

	public void setSalary(String salary) {
		this.salary = salary;
	}

	@Column(name = "WORKING_LIFE")
	public String getWorkingLife() {
		return workingLife;
	}

	public void setWorkingLife(String workingLife) {
		this.workingLife = workingLife;
	}

	@Column(name = "COMPANY")
	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	@Column(name = "INTEREST")
	public String getInterest() {
		return interest;
	}

	public void setInterest(String interest) {
		this.interest = interest;
	}
	
	
	@Column(name = "POSTCODE")
	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	@Column(name = "SHORT2")
	public String getShort2() {
		return short2;
	}

	public void setShort2(String short2) {
		this.short2 = short2;
	}

	@Column(name = "SHORT3")
	public String getShort3() {
		return short3;
	}

	public void setShort3(String short3) {
		this.short3 = short3;
	}

	@Column(name = "SHORT4")
	public String getShort4() {
		return short4;
	}

	public void setShort4(String short4) {
		this.short4 = short4;
	}

	@Column(name = "SHORT5")
	public String getShort5() {
		return short5;
	}

	public void setShort5(String short5) {
		this.short5 = short5;
	}

	@Column(name = "LONG1")
	public String getLong1() {
		return long1;
	}

	public void setLong1(String long1) {
		this.long1 = long1;
	}

	@Column(name = "LONG2")
	public String getLong2() {
		return long2;
	}

	public void setLong2(String long2) {
		this.long2 = long2;
	}

	@Column(name = "LONG3")
	public String getLong3() {
		return long3;
	}

	public void setLong3(String long3) {
		this.long3 = long3;
	}

	@Column(name = "LONG4")
	public String getLong4() {
		return long4;
	}

	public void setLong4(String long4) {
		this.long4 = long4;
	}

	@Column(name = "LONG5")
	public String getLong5() {
		return long5;
	}

	public void setLong5(String long5) {
		this.long5 = long5;
	}
	
	@Column(name = "RECEIVEMAIL")
	public Integer getReceiveMail() {
		return receiveMail;
	}

	public void setReceiveMail(Integer receiveMail) {
		this.receiveMail = receiveMail;
	}
}
