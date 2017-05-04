package com.baozun.nebula.model.member;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.Index;
import org.hibernate.annotations.OptimisticLockType;

import com.baozun.nebula.model.BaseModel;

@Entity
@Table(name = "T_MEM_CONTACT")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class Contact extends BaseModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8656342392984388813L;

	/** 默认地址*/
	public static final boolean ISDEFAULT = true;
	/** 普通地址*/
	public static final boolean NOTDEFAULT = false;
	
	/** PK. */
	private Long				id;
	
	/** 地址类型 如：家，公司等 */
	private String contactType;

	/** 姓名 */
	private String				name;
	
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
	 * 国家
	 */
	private String 				country;
	
	/** 省 */
	private String				province;
	
	/** 市 */
	private String			    city;
	
	/** 区 */
	private String			    area;
	
	/** 镇*/
	private String				town;
	 

	/** 邮编 */
	private String				postcode;
	
	/** 家庭地址 */
	private String				address;
	
	/**
	 * 联系电话
	 */
	private String				telphone;
	
	/** 手机 */
	private String 				mobile;
	
	/**
	 * 是否为默认地址
	 * false 非默认地址
	 * true 默认地址
	 */
	private Boolean 			isDefault;
	
	/**
	 * 会员id
	 */
	private Long 				memberId;
	
	/**
	 * 邮箱
	 */
	private String 				email;
	
	
	/**
	 * 修改时间
	 */
	private Date				modifyTime;
	
	private Date				version;
	
	
	
	
	@Id
	@Column(name = "ID")
	@SequenceGenerator(name = "SEQ_T_SAL_CONTACT",sequenceName = "S_T_SAL_CONTACT",allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_SAL_CONTACT")
	public Long getId(){
		return id;
	}

	public void setId(Long id){
		this.id = id;
	}

	@Column(name = "NAME", length = 100)
    @Index(name = "IDX_CONTACT_NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	

	@Column(name = "POST_CODE")
	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	@Column(name = "ADDRESS")
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	 

	@Column(name = "TELPHONE")
	public String getTelphone() {
		return telphone;
	}

	public void setTelphone(String telphone) {
		this.telphone = telphone;
	}

	@Column(name = "MOBILE")
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Column(name = "IS_DEFAULT")
    @Index(name = "IDX_CONTACT_IS_DEFAULT")
	public Boolean getIsDefault() {
		return isDefault;
	}
	
	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}

	@Column(name = "MEMBER_ID")
    @Index(name = "IDX_CONTACT_MEMBER_ID")
	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}

	@Column(name = "EMAIL")
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Version
	@Column(name = "VERSION")
	public Date getVersion() {
		return version;
	}

	public void setVersion(Date version) {
		this.version = version;
	}

	@Column(name = "MODIFYTIME")
	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	@Column(name = "COUNTRY_ID")
    @Index(name = "IDX_CONTACT_COUNTRY_ID")
	public Long getCountryId() {
		return countryId;
	}

	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}

	@Column(name = "PROVINCE_ID")
    @Index(name = "IDX_CONTACT_PROVINCE_ID")
	public Long getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Long provinceId) {
		this.provinceId = provinceId;
	}

	@Column(name = "CITY_ID")
    @Index(name = "IDX_CONTACT_CITY_ID")
	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	@Column(name = "AREA_ID")
    @Index(name = "IDX_CONTACT_AREA_ID")
	public Long getAreaId() {
		return areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	@Column(name = "TOWN_ID")
    @Index(name = "IDX_CONTACT_TOWN_ID")
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

	@Column(name = "CONTACT_TYPE")
    @Index(name = "IDX_CONTACT_CONTACT_TYPE")
	public String getContactType() {
		return contactType;
	}

	public void setContactType(String contactType) {
		this.contactType = contactType;
	}
	
}
