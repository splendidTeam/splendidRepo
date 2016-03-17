package com.baozun.nebula.wormhole.mq.entity.logistics;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 上门取件信息
 * @author Justin Hu
 *
 */

public class PickupInfoV5 implements Serializable {

	
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 7249976785227975535L;

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
     * 区
     */
    private String district;
    
    
    /**
     * 镇
     */
    private String town;
    
    /**
     * 地址
     */
    private String address;
    
    
    /**
     * 邮编
     */
    private String zipCode;
    
	
	/**
     * 收货人
     */
    private String contact;
    
    /**
     * 收货人电话
     */
    private String contactPhone;
    
    /**
     * 收货人手机
     */
    private String contactMobile;
    
   
    /**
     * 商城方的订单编号
     */
    private String bsOrdercode;


	public String getCountry() {
		return country;
	}


	public void setCountry(String country) {
		this.country = country;
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


	public String getDistrict() {
		return district;
	}


	public void setDistrict(String district) {
		this.district = district;
	}


	public String getTown() {
		return town;
	}


	public void setTown(String town) {
		this.town = town;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public String getZipCode() {
		return zipCode;
	}


	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}


	public String getContact() {
		return contact;
	}


	public void setContact(String contact) {
		this.contact = contact;
	}


	public String getContactPhone() {
		return contactPhone;
	}


	public void setContactPhone(String contactPhone) {
		this.contactPhone = contactPhone;
	}


	public String getContactMobile() {
		return contactMobile;
	}


	public void setContactMobile(String contactMobile) {
		this.contactMobile = contactMobile;
	}


	public String getBsOrdercode() {
		return bsOrdercode;
	}


	public void setBsOrdercode(String bsOrdercode) {
		this.bsOrdercode = bsOrdercode;
	}






}
