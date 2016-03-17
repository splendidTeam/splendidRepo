package com.baozun.nebula.wormhole.mq.entity.order;

import java.io.Serializable;

/**
 * 换货出库配送信息
 * 
 * @author yimin.qiao
 * @createtime 2016年1月13日 下午4:54:51
 */
public class ReturnApplicationDeliveryInfoV5 implements Serializable {
	private static final long serialVersionUID = -993663578216753924L;

	/** 国家 */
	private String country;
	
	/** 省 */
	private String province;
	
	/** 市 */
	private String city;
	
	/** 区 */
	private String district;
	
	 /** 镇 */
    private String town;
	
	/** 详细地址 */
	private String address;
	
	/** 邮编 */
	private String zipCode;
	
	/** 收货人 */
	private String receiver;
	
	/** 收货人电话 */
	private String receiverPhone;
	
	/** 收货人手机 */
	private String receiverMobile;
	
	/** 物流方式：SCM定义的列表中选取，和商城挂钩 */
	private String logisticsTypeCode;

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

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getReceiverPhone() {
		return receiverPhone;
	}

	public void setReceiverPhone(String receiverPhone) {
		this.receiverPhone = receiverPhone;
	}

	public String getReceiverMobile() {
		return receiverMobile;
	}

	public void setReceiverMobile(String receiverMobile) {
		this.receiverMobile = receiverMobile;
	}

	public String getLogisticsTypeCode() {
		return logisticsTypeCode;
	}

	public void setLogisticsTypeCode(String logisticsTypeCode) {
		this.logisticsTypeCode = logisticsTypeCode;
	}
}
