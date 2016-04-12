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

/**
 * The Class CaptchaValidateEntity.
 *
 * @author feilong
 * @version 1.5.3 2016年3月28日 下午11:31:11
 * @since 1.5.3
 */
public class CaptchaValidateEntity implements Serializable{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -346499273494415790L;

    /** 标识 captcha 是什么功能. */
    private String            captchaId;

    /** 用户输入的验证码的值. */
    private String            userInputValue;

    /**
     * instanceId 通常ajax验证 相关验证码会使用到, 比如 {@link com.feilong.captcha.manager.captcha.validate.BotdetectCaptchaValidate}.
     */
    private String            instanceIdValue;

    /**
     * 获得 标识 captcha 是什么功能.
     *
     * @return the captchaId
     */
    public String getCaptchaId(){
        return captchaId;
    }

    /**
     * 设置 标识 captcha 是什么功能.
     *
     * @param captchaId
     *            the captchaId to set
     */
    public void setCaptchaId(String captchaId){
        this.captchaId = captchaId;
    }

    /**
     * 获得 用户输入的验证码的值.
     *
     * @return the userInputValue
     */
    public String getUserInputValue(){
        return userInputValue;
    }

    /**
     * 设置 用户输入的验证码的值.
     *
     * @param userInputValue
     *            the userInputValue to set
     */
    public void setUserInputValue(String userInputValue){
        this.userInputValue = userInputValue;
    }

    /**
     * 获得 instanceId 通常ajax验证 相关验证码会使用到,instanceId 通常ajax验证 相关验证码会使用到, 比如
     * {@link com.baozun.nebula.manager.captcha.validate.BotdetectCaptchaValidate}.
     *
     * @return the instanceIdValue
     */
    public String getInstanceIdValue(){
        return instanceIdValue;
    }

    /**
     * 设置 instanceId 通常ajax验证 相关验证码会使用到,instanceId 通常ajax验证 相关验证码会使用到, 比如
     * {@link com.baozun.nebula.manager.captcha.validate.BotdetectCaptchaValidate}.
     *
     * @param instanceIdValue
     *            the instanceIdValue to set
     */
    public void setInstanceIdValue(String instanceIdValue){
        this.instanceIdValue = instanceIdValue;
    }
}