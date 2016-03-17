/**
 * Copyright (c) 2013 Jumbomart All Rights Reserved.
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
package com.baozun.nebula.model.salesorder;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.hibernate.annotations.OptimisticLockType;

import com.baozun.nebula.model.BaseModel;

/**
 * PickupInfo 上门取件信息
 *
 * @author: shiyang.lv
 * @date: 2014年5月12日
 **/
@Entity
@Table(name = "T_SO_PICKUPINFO")
@org.hibernate.annotations.Entity(optimisticLock = OptimisticLockType.VERSION)
public class PickupInfo extends BaseModel {

    /**
     * 
     */
    private static final long serialVersionUID = -7337175935527165569L;
    
    /**
     * ID
     */
    private Long id;
    
    /**
     * 订单ID
     */
    private Long orderId;
    
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
     * 最后修改时间
     */
    private Date modifyTime;
    
    /**
     * 版本
     */
    private Date version;
    
    /**
     * 创建时间
     */
    private Date createTime;
    
    /**
     * 商城方的订单编号
     */
    private String bsOrdercode;

    
    private Integer lifecycle;
    
    @Id
    @Column(name = "ID")
    @SequenceGenerator(name = "SEQ_T_SO_PICKUPINFO",sequenceName = "S_T_SO_PICKUPINFO",allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "SEQ_T_SO_PICKUPINFO")
    public Long getId() {
        return id;
    }

    
    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "ORDER_ID")
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
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

    @Column(name = "DISTRICT")
    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
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

    @Column(name = "ZIPCODE")
    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    @Column(name = "CONTACT")
    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
    
    @Column(name = "CONTACT_PHONE")
    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    @Column(name = "CONTACT_MOBILE")
    public String getContactMobile() {
        return contactMobile;
    }

    public void setContactMobile(String contactMobile) {
        this.contactMobile = contactMobile;
    }
    
    @Column(name = "MODIFY_TIME")
    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    @Column(name = "VERSION")
    public Date getVersion() {
        return version;
    }

    public void setVersion(Date version) {
        this.version = version;
    }

    @Column(name = "CREATE_TIME")
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Column(name = "BS_ORDER_CODE")
    public String getBsOrdercode() {
        return bsOrdercode;
    }


    public void setBsOrdercode(String bsOrdercode) {
        this.bsOrdercode = bsOrdercode;
    }

    @Column(name = "LIFECYCLE")
    public Integer getLifecycle() {
        return lifecycle;
    }


    public void setLifecycle(Integer lifecycle) {
        this.lifecycle = lifecycle;
    }

    
}

