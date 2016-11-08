package com.baozun.nebula.command;

import java.util.Date;

import com.baozun.nebula.command.delivery.ContactDeliveryCommand;


public class ContactCommand implements Command{
	private static final long serialVersionUID = -2247833530922765508L;

	/** PK. */
	private Long				id;
	
	/** 地址类型 如：家，公司等 */
	private String contactType;

	/** 姓名 */
	private String				name;
	
	private String 				country;
	
	/** 省 */
	private String				province;
	
	/** 市 */
	private String			    city;
	
	/** 区 */
	private String			    area;
	
	/** 镇*/
	private String				town; 
  

	private Long 				countryId; 

	/** 省 */
	private Long				provinceId;
	
	/** 市 */
	private Long			    cityId;
	
	/** 区 */
	private Long			    areaId;
	
	/** 镇*/
	private Long				townId;
	 
	  
	  
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
	private Boolean 			isDefault = false;
	
	private String 				email;
	
	private Integer 			ifReceiveMail;
	
	private Long 				memberId;
	
	private Date 				modifyTime;
	
	private ContactDeliveryCommand	contactDeliveryCommand;
	
	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}
	
	public Long getId(){
		return id;
	}

	public void setId(Long id){
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTelphone() {
		return telphone;
	}

	public void setTelphone(String telphone) {
		this.telphone = telphone;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}



	public Boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public Integer getIfReceiveMail() {
		return ifReceiveMail;
	}

	public void setIfReceiveMail(Integer ifReceiveMail) {
		this.ifReceiveMail = ifReceiveMail;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}
	 
 
	public Long getCountryId() {
		return countryId;
	}

	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}

	public Long getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Long provinceId) {
		this.provinceId = provinceId;
	}

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public Long getAreaId() {
		return areaId;
	}

	public void setAreaId(Long areaId) {
		this.areaId = areaId;
	}

	public Long getTownId() {
		return townId;
	}

	public void setTownId(Long townId) {
		this.townId = townId;
	}
	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		this.town = town;
	}

	public String getContactType() {
		return contactType;
	}

	public void setContactType(String contactType) {
		this.contactType = contactType;
	}

	@Override
	public String toString() {
		return "ContactCommand [id=" + id + ", contactType=" + contactType
				+ ", name=" + name + ", country="
				+ country + ", province=" + province + ", city=" + city
				+ ", area=" + area + ", postcode=" + postcode + ", address="
				+ address + ", telphone=" + telphone + ", mobile=" + mobile
				+ ", isDefault=" + isDefault + ", email=" + email
				+ ", ifReceiveMail=" + ifReceiveMail + ", memberId=" + memberId
				+ "]";
	}

	/**
	 * @return the contactDeliveryCommand
	 */
	public ContactDeliveryCommand getContactDeliveryCommand() {
		return contactDeliveryCommand;
	}

	/**
	 * @param contactDeliveryCommand the contactDeliveryCommand to set
	 */
	public void setContactDeliveryCommand(ContactDeliveryCommand contactDeliveryCommand) {
		this.contactDeliveryCommand = contactDeliveryCommand;
	}
}
