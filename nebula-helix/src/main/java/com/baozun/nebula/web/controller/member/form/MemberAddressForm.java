/**
 * Copyright (c) 2010 Jumbomart All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Jumbomart.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Jumbo.
 *
 * JUMBOMART MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. JUMBOMART SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.web.controller.member.form;


import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baozun.nebula.command.ContactCommand;
import com.baozun.nebula.web.controller.BaseForm;

public class MemberAddressForm extends BaseForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4534711242178149888L;
	
	private static final Logger	LOGGER = LoggerFactory.getLogger(MemberAddressForm.class);

	/**
	 * ID
	 */
	private Long id;

	/**
	 * 联系人姓名
	 */
	private String name;
	/**
	 * 联系手机
	 */
	private String mobile;

	/**
	 * 联系电话
	 */
	private String telphone;

	/**
	 * 联系EMAIL
	 */
	private String email;
	
	
	/** 国家 */
	private Long 				countryId; 

	/** 省 */
	private Long				provinceId;
	
	/** 市 */
	private Long			    cityId;
	
	/** 区 */
	private Long			    areaId;
	
	/** 镇*/
	private Long				townId;

	/**
	 * 地址
	 */
	private String address;

	/**
	 * 类型 1.个人 2.单位
	 */
	private int contactType;

	/**
	 * 邮编
	 */
	private String postcode;

	/**
	 * 是否默认
	 */
	private boolean isDefault;

	

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the telphone
	 */
	public String getTelphone() {
		return telphone;
	}

	/**
	 * @param telphone
	 *            the telphone to set
	 */
	public void setTelphone(String telphone) {
		this.telphone = telphone;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the contactType
	 */
	public int getContactType() {
		return contactType;
	}

	/**
	 * @param contactType
	 *            the contactType to set
	 */
	public void setContactType(int contactType) {
		this.contactType = contactType;
	}

	/**
	 * @return the postcode
	 */
	public String getPostcode() {
		return postcode;
	}

	/**
	 * @param postcode
	 *            the postcode to set
	 */
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}
	/**
	 * 将memberAddressForm转成ContactCommand
	 * @param memberAddressForm
	 * @return
	 */
	public ContactCommand toContactCommand() {
		ContactCommand contactCommand = new ContactCommand();
		toContactCommand(contactCommand);
		return contactCommand;
	}

	/**
	 * 更新地址信息
	 * @param command
	 * @return
	 */
	public ContactCommand toContactCommand(ContactCommand command) {
		try {
			BeanUtils.copyProperties(command, this);
		} catch (Exception e){
			LOGGER.error("", e);
		}
		return command;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the mobile
	 */
	public String getMobile() {
		return mobile;
	}

	/**
	 * @param mobile the mobile to set
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
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

	public Long getCountryId() {
		return countryId;
	}

	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}
	
}
