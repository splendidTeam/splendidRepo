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
package com.baozun.nebula.manager.captcha;

import javax.servlet.http.HttpServletRequest;

/**
 * 通用验证码校验接口.
 * <p>
 * 可以有短信验证码校验实现,图形验证码校验实现,目前可以参考 {@link com.baozun.nebula.manager.captcha.validate.BotdetectCaptchaValidate}
 * </p>
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 1.5.3 2016年3月28日 下午5:10:11
 * @since 1.5.3
 * @deprecated 请使用 feilong-captch ,进行了框架的升级
 */
@Deprecated
public interface CaptchaValidate{

    /**
     * Validate.
     *
     * @param captchaId
     *            the captcha id
     * @param userInput
     *            the user input
     * @param request
     *            the request
     * @return true, if validate
     */
    boolean validate(String captchaId,String userInput,HttpServletRequest request);

    /**
     * Validate ajax.
     *
     * @param captchaId
     *            the captcha id
     * @param userInput
     *            the user input
     * @param instanceId
     *            the instance id
     * @param request
     *            the request
     * @return true, if validate ajax
     */
    boolean validateAjax(String captchaId,String userInput,String instanceId,HttpServletRequest request);
}
