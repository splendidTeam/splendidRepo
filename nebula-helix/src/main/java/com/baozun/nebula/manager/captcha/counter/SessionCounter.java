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
package com.baozun.nebula.manager.captcha.counter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 基于 {@link HttpSession}的计数器实现.
 * 
 * <p>
 * session counter和 humanKeyValue不相关
 * </p>
 * 
 * <h3>主要提供以下方法实现:</h3>
 * 
 * <blockquote>
 * <ol>
 * <li>{@link #incr(String, HttpServletRequest)}计数器 +1</li>
 * <li>{@link #getCount(String, HttpServletRequest)} 获得当前计数器的值</li>
 * <li>{@link #clear(String, HttpServletRequest)} 指定功能计算器清零</li>
 * </ol>
 * </blockquote>
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 1.5.3 2016年3月28日 上午1:26:18
 * @since 1.5.3
 * @deprecated 请使用 feilong-captch ,进行了框架的升级
 */
@Deprecated
public interface SessionCounter extends Counter{

    /**
     * 获得 count.
     *
     * @param id
     *            标识是哪个功能
     * @param request
     *            the request
     * @return 如果不存在对应的key/attribute 返回0;else 取到数据转成integer 返回
     */
    int getCount(String id,HttpServletRequest request);

    /**
     * Incr.
     *
     * @param id
     *            标识是哪个功能
     * @param request
     *            the request
     */
    void incr(String id,HttpServletRequest request);

    /**
     * 清除.
     *
     * @param id
     *            标识是哪个功能
     * @param request
     *            the request
     */
    void clear(String id,HttpServletRequest request);
}
