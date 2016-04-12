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
 * The Class CaptchaContainerAndValidateConfig.
 *
 * @author feilong
 * @version 1.5.3 2016年4月7日 下午8:11:14
 * @see CaptchaContainer
 * @see CaptchaValidateConfig
 * @since 1.5.3
 */
public class CaptchaContainerAndValidateConfig implements Serializable{

    /** The Constant serialVersionUID. */
    private static final long     serialVersionUID = 7537365892919462083L;

    /** The captcha container. */
    private CaptchaContainer      captchaContainer;

    /** The captcha validate config. */
    private CaptchaValidateConfig captchaValidateConfig;

    /**
     * 获得 captcha container.
     *
     * @return the captchaContainer
     */
    public CaptchaContainer getCaptchaContainer(){
        return captchaContainer;
    }

    /**
     * 设置 captcha container.
     *
     * @param captchaContainer
     *            the captchaContainer to set
     */
    public void setCaptchaContainer(CaptchaContainer captchaContainer){
        this.captchaContainer = captchaContainer;
    }

    /**
     * 获得 captcha validate config.
     *
     * @return the captchaValidateConfig
     */
    public CaptchaValidateConfig getCaptchaValidateConfig(){
        return captchaValidateConfig;
    }

    /**
     * 设置 captcha validate config.
     *
     * @param captchaValidateConfig
     *            the captchaValidateConfig to set
     */
    public void setCaptchaValidateConfig(CaptchaValidateConfig captchaValidateConfig){
        this.captchaValidateConfig = captchaValidateConfig;
    }
}
