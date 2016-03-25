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

import org.apache.commons.lang3.StringUtils;

import com.baozun.nebula.command.ContactCommand;
import com.baozun.nebula.web.controller.BaseForm;

public class MemberAddressForm extends BaseForm {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4534711242178149888L;

	/**
	 * ID
	 */
	private Long id;

	/**
	 * 联系人姓名
	 */
	private String consignee;
	/**
	 * 联系手机
	 */
	private String phone;

	/**
	 * 联系电话
	 */
	private String telphone;

	/**
	 * 联系EMAIL
	 */
	private String email;

	/**
	 * 省
	 */
	private Long province;

	/**
	 * 市
	 */
	private Long city;

	/**
	 * 区
	 */
	private Long area;

	/**
	 * 镇
	 */
	private Long town;

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
	 * @return the consignee
	 */
	public String getConsignee() {
		return consignee;
	}

	/**
	 * @param consignee
	 *            the consignee to set
	 */
	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone
	 *            the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
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
	 * @return the province
	 */
	public Long getProvince() {
		return province;
	}

	/**
	 * @param province
	 *            the province to set
	 */
	public void setProvince(Long province) {
		this.province = province;
	}

	/**
	 * @return the city
	 */
	public Long getCity() {
		return city;
	}

	/**
	 * @param city
	 *            the city to set
	 */
	public void setCity(Long city) {
		this.city = city;
	}

	/**
	 * @return the area
	 */
	public Long getArea() {
		return area;
	}

	/**
	 * @param area
	 *            the area to set
	 */
	public void setArea(Long area) {
		this.area = area;
	}

	/**
	 * @return the town
	 */
	public Long getTown() {
		return town;
	}

	/**
	 * @param town
	 *            the town to set
	 */
	public void setTown(Long town) {
		this.town = town;
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

	/**
	 * @return the isDefault
	 */
	public boolean isDefault() {
		return isDefault;
	}

	/**
	 * @param isDefault
	 *            the isDefault to set
	 */
	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	/**
	 * 将memberAddressForm转成ContactCommand
	 * @param memberAddressForm
	 * @return
	 */
	public ContactCommand toContactCommand() {
		ContactCommand contactCommand = new ContactCommand();
		contactCommand.setIsDefault(false);
		contactCommand.setAreaId(this.getArea());
		contactCommand.setCityId(this.getCity());		
		contactCommand.setTownId(this.getTown());	
		contactCommand.setProvinceId(this.getProvince());	
		contactCommand.setPostcode(this.getPostcode());
		contactCommand.setAddress(this.getAddress());
		contactCommand.setName(this.getConsignee());
		contactCommand.setMobile(this.getPhone());
		contactCommand.setTelphone(this.getTelphone());
		return contactCommand;
	}

	/**
	 * 更新地址信息
	 * @param command
	 * @return
	 */
	public ContactCommand toContactCommand(ContactCommand command) {
		command.setAreaId(this.getArea());		
		command.setCityId(this.getCity());		
		command.setTownId(this.getTown());	
		command.setProvinceId(this.getProvince());	
		command.setPostcode(this.getPostcode());
		command.setAddress(this.getAddress());
		command.setName(this.getConsignee());
		command.setIsDefault(command.getIsDefault());
		if(StringUtils.isNotBlank(this.getPhone())){
			command.setMobile(this.getPhone());
		}
		if(StringUtils.isNotBlank(this.getTelphone())){
			command.setTelphone(this.getTelphone());
		}		
		return command;
	}
	
}
