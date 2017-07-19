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
package com.baozun.nebula.web.taglib;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.tagext.Tag;

import com.baozun.nebula.manager.captcha.CaptchaUtil;
import com.baozun.nebula.manager.captcha.entity.CaptchaContainerAndValidateConfig;
import com.feilong.taglib.AbstractConditionalTag;

/**
 * Captcha容器,主要是用来控制是否要显示标签题内容.
 * 
 * <p>
 * 如果 {@link CaptchaUtil#needValidate(CaptchaContainerAndValidateConfig, HttpServletRequest)},那么将会显示taglib content部分内容
 * {@link Tag#EVAL_BODY_INCLUDE};<br>
 * 如果不 {@link CaptchaUtil#needValidate(CaptchaContainerAndValidateConfig, HttpServletRequest)}将会
 * {@link Tag#SKIP_BODY}
 * </p>
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 1.5.3 2016年4月2日 下午4:59:47
 * @since 1.5.3
 * @deprecated 可以使用 feilong captcha
 */
@Deprecated
public class CaptchaContainerTag extends AbstractConditionalTag{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -1988204889437249797L;

    //目前就一个 id 属性 (see in javax.servlet.jsp.tagext.TagSupport.id)

    /*
     * (non-Javadoc)
     * 
     * @see com.feilong.taglib.base.AbstractConditionalTag#condition()
     */
    @Override
    public boolean condition(){
        HttpServletRequest request = getHttpServletRequest();

        CaptchaContainerAndValidateConfig captchaContainerAndValidateConfig = CaptchaUtil.getCaptchaContainerAndValidateConfigByContainerId(id, request);

        return CaptchaUtil.needValidate(captchaContainerAndValidateConfig, request);
    }
}
