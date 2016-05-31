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
 * 通过配置的默认规则来判断是否需要验证码.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 1.5.3 2016年4月7日 下午10:06:37
 * @since 1.5.3
 */
public interface RuleResolver{

    /**
     * Need show captcha.
     *
     * @param request
     *            the request
     * @return
     *         <ol>
     *         <li>false,不需要显示</li>
     *         <li>true,标识需要显示</li>
     *         <li><code>null</code>,标识穿透内部验证(不在范围内),比如ip即不属于白名单,又不属于黑名单,什么都没有做</li>
     *         </ol>
     */
    Boolean needShowCaptcha(HttpServletRequest request);
}
