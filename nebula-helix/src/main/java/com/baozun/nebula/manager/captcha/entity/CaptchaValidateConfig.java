/**
 * Copyright (c) 2015 Baozun All Rights Reserved.
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
 */
package com.baozun.nebula.manager.captcha.entity;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import com.baozun.nebula.manager.captcha.CaptchaValidate;

/**
 * The Class CaptchaValidateableConfig.
 *
 * @author feilong
 * @version 1.5.3 2016年3月30日 上午12:11:17
 * @since 1.5.3
 */
public class CaptchaValidateConfig implements Serializable{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID   = 616674389095113914L;

    /**
     * Captcha id,通常用来标识 captcha的功能.
     */
    private String            captchaId;

    /** {@link HttpServletRequest} 请求里面,用户输入的验证码参数名字. */
    private String            userInputParamName = "captchaValue";

    /**
     * Instance id name.(通常ajax请求需要设置该属性)
     */
    private String            instanceIdName     = "instanceId";

    /** 校验器. */
    private CaptchaValidate   captchaValidate;

    /**
     * 获得 captcha id,通常用来标识 captcha的功能.
     *
     * @return the captchaId
     */
    public String getCaptchaId(){
        return captchaId;
    }

    /**
     * 设置 captcha id,通常用来标识 captcha的功能.
     *
     * @param captchaId
     *            the captchaId to set
     */
    public void setCaptchaId(String captchaId){
        this.captchaId = captchaId;
    }

    /**
     * 获得 {@link HttpServletRequest} 请求里面,用户输入的验证码参数名字.
     *
     * @return the userInputParamName
     */
    public String getUserInputParamName(){
        return userInputParamName;
    }

    /**
     * 设置 {@link HttpServletRequest} 请求里面,用户输入的验证码参数名字.
     *
     * @param userInputParamName
     *            the userInputParamName to set
     */
    public void setUserInputParamName(String userInputParamName){
        this.userInputParamName = userInputParamName;
    }

    /**
     * 获得 instance id name.
     *
     * @return the instanceIdName
     */
    public String getInstanceIdName(){
        return instanceIdName;
    }

    /**
     * 设置 instance id name.
     *
     * @param instanceIdName
     *            the instanceIdName to set
     */
    public void setInstanceIdName(String instanceIdName){
        this.instanceIdName = instanceIdName;
    }

    /**
     * 获得 校验器.
     *
     * @return the captchaValidate
     */
    public CaptchaValidate getCaptchaValidate(){
        return captchaValidate;
    }

    /**
     * 设置 校验器.
     *
     * @param captchaValidate
     *            the captchaValidate to set
     */
    public void setCaptchaValidate(CaptchaValidate captchaValidate){
        this.captchaValidate = captchaValidate;
    }
}
