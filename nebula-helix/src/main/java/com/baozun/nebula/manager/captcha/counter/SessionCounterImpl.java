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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.feilong.core.bean.ConvertUtil;

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
public class SessionCounterImpl implements SessionCounter{

    /** The Constant LOGGER. */
    private static final Logger LOGGER                         = LoggerFactory.getLogger(SessionCounterImpl.class);

    /** <code>{@value}</code>. */
    private static final String COUNTER_SESSION_ATTRIBUTE_NAME = SessionCounterImpl.class.getName();

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.manager.captcha.counter.SessionCounter#getCount(java.lang.String, javax.servlet.http.HttpServletRequest)
     */
    @Override
    public int getCount(String id,HttpServletRequest request){
        HttpSession session = request.getSession();
        String counterKey = getCounterKey(id, request);
        Object counter = session.getAttribute(counterKey);

        if (null == counter){
            LOGGER.debug("session has no attribute:[{}],return 0", counterKey);
            return 0;
        }
        LOGGER.debug("counterKey:[{}] in session value is:[{}]", counterKey, counter);
        return ConvertUtil.toInteger(counter);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.manager.captcha.counter.SessionCounter#incr(java.lang.String, javax.servlet.http.HttpServletRequest)
     */
    @Override
    public void incr(String id,HttpServletRequest request){
        HttpSession session = request.getSession();
        int errorCount = getCount(id, request);

        String counterKey = getCounterKey(id, request);

        LOGGER.debug("counterKey:[{}] in session,old value is:[{}],incr value is:[{}]", counterKey, errorCount, errorCount + 1);
        session.setAttribute(counterKey, errorCount + 1);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.manager.captcha.counter.SessionCounter#clear(java.lang.String, javax.servlet.http.HttpServletRequest)
     */
    @Override
    public void clear(String id,HttpServletRequest request){
        HttpSession session = request.getSession();

        String counterKey = getCounterKey(id, request);

        session.removeAttribute(counterKey);
        LOGGER.debug("remove session Attribute counterKey:[{}]", counterKey);
    }

    /**
     * 获得 counter key.
     *
     * @param id
     *            标识是哪个功能
     * @param request
     *            the request
     * @return the counter key
     */
    private static String getCounterKey(String id,HttpServletRequest request){
        return COUNTER_SESSION_ATTRIBUTE_NAME + "." + id;
    }
}
