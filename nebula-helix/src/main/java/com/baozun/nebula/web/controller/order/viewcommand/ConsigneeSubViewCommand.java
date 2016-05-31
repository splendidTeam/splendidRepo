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
package com.baozun.nebula.web.controller.order.viewcommand;

import com.baozun.nebula.web.controller.BaseViewCommand;

/**
 * 订单里面的收获地址信息.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 5.3.1 2016年5月12日 下午3:16:25
 * @see com.baozun.nebula.model.salesorder.Consignee
 * @see com.baozun.nebula.sdk.command.ConsigneeCommand
 * @since 5.3.1
 */
public class ConsigneeSubViewCommand extends BaseViewCommand {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -6400008677521495969L;

    /** The id. */
    private Long id;

    /** 姓名. */
    private String name;

    /** 购买人姓名. */
    private String buyerName;

    /** 购买人电话. */
    private String buyerTel;

    /** 国. */
    private String country;

    /** 省. */
    private String province;

    /** 市. */
    private String city;

    /** 区. */
    private String area;

    /** 地址. */
    private String address;

    /** 手机. */
    private String mobile;

    /** 固话. */
    private String tel;

    /** email. */
    private String email;

    /** 邮编. */
    private String postcode;

    // ****************************************************************

    /** 指定时间段. */
    private String appointTimeQuantum;

    /** 指定日期. */
    private String appointTime;

    /** 指定类型. */
    private String appointType;

    /**
     * 获得 id.
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置 id.
     *
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获得 姓名.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * 设置 姓名.
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获得 国.
     *
     * @return the country
     */
    public String getCountry() {
        return country;
    }

    /**
     * 设置 国.
     *
     * @param country the country to set
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * 获得 省.
     *
     * @return the province
     */
    public String getProvince() {
        return province;
    }

    /**
     * 设置 省.
     *
     * @param province the province to set
     */
    public void setProvince(String province) {
        this.province = province;
    }

    /**
     * 获得 市.
     *
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * 设置 市.
     *
     * @param city the city to set
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * 获得 区.
     *
     * @return the area
     */
    public String getArea() {
        return area;
    }

    /**
     * 设置 区.
     *
     * @param area the area to set
     */
    public void setArea(String area) {
        this.area = area;
    }

    /**
     * 获得 地址.
     *
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * 设置 地址.
     *
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 获得 手机.
     *
     * @return the mobile
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * 设置 手机.
     *
     * @param mobile the mobile to set
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * 获得 固话.
     *
     * @return the tel
     */
    public String getTel() {
        return tel;
    }

    /**
     * 设置 固话.
     *
     * @param tel the tel to set
     */
    public void setTel(String tel) {
        this.tel = tel;
    }

    /**
     * 获得 email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * 设置 email.
     *
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * 获得 邮编.
     *
     * @return the postcode
     */
    public String getPostcode() {
        return postcode;
    }

    /**
     * 设置 邮编.
     *
     * @param postcode the postcode to set
     */
    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    /**
     * 获得 指定时间段.
     *
     * @return the appointTimeQuantum
     */
    public String getAppointTimeQuantum() {
        return appointTimeQuantum;
    }

    /**
     * 设置 指定时间段.
     *
     * @param appointTimeQuantum the appointTimeQuantum to set
     */
    public void setAppointTimeQuantum(String appointTimeQuantum) {
        this.appointTimeQuantum = appointTimeQuantum;
    }

    /**
     * 获得 指定日期.
     *
     * @return the appointTime
     */
    public String getAppointTime() {
        return appointTime;
    }

    /**
     * 设置 指定日期.
     *
     * @param appointTime the appointTime to set
     */
    public void setAppointTime(String appointTime) {
        this.appointTime = appointTime;
    }

    /**
     * 获得 指定类型.
     *
     * @return the appointType
     */
    public String getAppointType() {
        return appointType;
    }

    /**
     * 设置 指定类型.
     *
     * @param appointType the appointType to set
     */
    public void setAppointType(String appointType) {
        this.appointType = appointType;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getBuyerTel() {
        return buyerTel;
    }

    public void setBuyerTel(String buyerTel) {
        this.buyerTel = buyerTel;
    }

}
