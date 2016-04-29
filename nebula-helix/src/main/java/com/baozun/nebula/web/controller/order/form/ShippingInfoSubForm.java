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
package com.baozun.nebula.web.controller.order.form;

import java.io.Serializable;

/**
 * 订单收货地址信息.
 *
 * @author feilong
 * @version 5.3.1 2016年4月28日 下午1:23:51
 * @see com.baozun.nebula.model.salesorder.Consignee
 * @since 5.3.1
 */
public class ShippingInfoSubForm implements Serializable{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -7282842164155121566L;

    /** 收件人姓名. */
    private String            name;

    //*********************************游客购买人信息******************************************************
    //目前看来是speedo 特殊的需求
    //思想之挣扎: 本来不想放一个form的,但是为了确保操作事务并且又不想增加代码
    //************************************************************
    /** The buyer name. */
    private String            buyerName;

    /** The buyer tel. */
    private String            buyerTel;

    //********************这里之所以只使用 id,因为需要支持国际化,在订单显示以及传输到后端的时候，需要转换*********************************************************

    /** 国家id. */
    private Long              countryId;

    /** 省 id. */
    private Long              provinceId;

    /** 市id. */
    private Long              cityId;

    /** 区 id. */
    private Long              areaId;

    /** 镇id. */
    private Long              townId;

    //***************************************************************************

    /** 详细地址. */
    private String            address;

    /** 邮编. */
    private String            postcode;

    /** 手机. */
    private String            mobile;

    /** 固话. */
    private String            tel;

    /** email. */
    private String            email;

    //***************************************************************************
    /**
     * 指定时间段,参见 {@link com.baozun.nebula.model.salesorder.Consignee#getAppointTimeQuantum()}
     */
    private String            appointTimeQuantum;

    /** 指定日期,参见 {@link com.baozun.nebula.model.salesorder.Consignee#getAppointTime()} */
    private String            appointTime;

    /** 指定类型 ,参见 {@link com.baozun.nebula.model.salesorder.Consignee#getAppointType()} */
    private String            appointType;

    //***************************************************************************
    //TODO 地址别名 String alias, 目前数据库还不支持

    //***************************************************************************
    /**
     * 获得 收件人姓名.
     *
     * @return the name
     */
    public String getName(){
        return name;
    }

    /**
     * 设置 收件人姓名.
     *
     * @param name
     *            the name to set
     */
    public void setName(String name){
        this.name = name;
    }

    /**
     * 获得 国家id.
     *
     * @return the countryId
     */
    public Long getCountryId(){
        return countryId;
    }

    /**
     * 设置 国家id.
     *
     * @param countryId
     *            the countryId to set
     */
    public void setCountryId(Long countryId){
        this.countryId = countryId;
    }

    /**
     * 获得 省 id.
     *
     * @return the provinceId
     */
    public Long getProvinceId(){
        return provinceId;
    }

    /**
     * 设置 省 id.
     *
     * @param provinceId
     *            the provinceId to set
     */
    public void setProvinceId(Long provinceId){
        this.provinceId = provinceId;
    }

    /**
     * 获得 市id.
     *
     * @return the cityId
     */
    public Long getCityId(){
        return cityId;
    }

    /**
     * 设置 市id.
     *
     * @param cityId
     *            the cityId to set
     */
    public void setCityId(Long cityId){
        this.cityId = cityId;
    }

    /**
     * 获得 区 id.
     *
     * @return the areaId
     */
    public Long getAreaId(){
        return areaId;
    }

    /**
     * 设置 区 id.
     *
     * @param areaId
     *            the areaId to set
     */
    public void setAreaId(Long areaId){
        this.areaId = areaId;
    }

    /**
     * 获得 镇id.
     *
     * @return the townId
     */
    public Long getTownId(){
        return townId;
    }

    /**
     * 设置 镇id.
     *
     * @param townId
     *            the townId to set
     */
    public void setTownId(Long townId){
        this.townId = townId;
    }

    /**
     * 获得 详细地址.
     *
     * @return the address
     */
    public String getAddress(){
        return address;
    }

    /**
     * 设置 详细地址.
     *
     * @param address
     *            the address to set
     */
    public void setAddress(String address){
        this.address = address;
    }

    /**
     * 获得 邮编.
     *
     * @return the postcode
     */
    public String getPostcode(){
        return postcode;
    }

    /**
     * 设置 邮编.
     *
     * @param postcode
     *            the postcode to set
     */
    public void setPostcode(String postcode){
        this.postcode = postcode;
    }

    /**
     * 获得 手机.
     *
     * @return the mobile
     */
    public String getMobile(){
        return mobile;
    }

    /**
     * 设置 手机.
     *
     * @param mobile
     *            the mobile to set
     */
    public void setMobile(String mobile){
        this.mobile = mobile;
    }

    /**
     * 获得 固话.
     *
     * @return the tel
     */
    public String getTel(){
        return tel;
    }

    /**
     * 设置 固话.
     *
     * @param tel
     *            the tel to set
     */
    public void setTel(String tel){
        this.tel = tel;
    }

    /**
     * 获得 email.
     *
     * @return the email
     */
    public String getEmail(){
        return email;
    }

    /**
     * 设置 email.
     *
     * @param email
     *            the email to set
     */
    public void setEmail(String email){
        this.email = email;
    }

    /**
     * 获得 指定时间段,参见 {@link com.
     *
     * @return the appointTimeQuantum
     */
    public String getAppointTimeQuantum(){
        return appointTimeQuantum;
    }

    /**
     * 设置 指定时间段,参见 {@link com.
     *
     * @param appointTimeQuantum
     *            the appointTimeQuantum to set
     */
    public void setAppointTimeQuantum(String appointTimeQuantum){
        this.appointTimeQuantum = appointTimeQuantum;
    }

    /**
     * 获得 指定日期,参见 {@link com.
     *
     * @return the appointTime
     */
    public String getAppointTime(){
        return appointTime;
    }

    /**
     * 设置 指定日期,参见 {@link com.
     *
     * @param appointTime
     *            the appointTime to set
     */
    public void setAppointTime(String appointTime){
        this.appointTime = appointTime;
    }

    /**
     * 获得 指定类型 ,参见 {@link com.
     *
     * @return the appointType
     */
    public String getAppointType(){
        return appointType;
    }

    /**
     * 设置 指定类型 ,参见 {@link com.
     *
     * @param appointType
     *            the appointType to set
     */
    public void setAppointType(String appointType){
        this.appointType = appointType;
    }

    /**
     * 获得 buyer name.
     *
     * @return the buyerName
     */
    public String getBuyerName(){
        return buyerName;
    }

    /**
     * 设置 buyer name.
     *
     * @param buyerName
     *            the buyerName to set
     */
    public void setBuyerName(String buyerName){
        this.buyerName = buyerName;
    }

    /**
     * 获得 buyer tel.
     *
     * @return the buyerTel
     */
    public String getBuyerTel(){
        return buyerTel;
    }

    /**
     * 设置 buyer tel.
     *
     * @param buyerTel
     *            the buyerTel to set
     */
    public void setBuyerTel(String buyerTel){
        this.buyerTel = buyerTel;
    }

}
