/**
 * Copyright (c) 2012 Baozun All Rights Reserved.
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
 *
 */
package com.baozun.nebula.web.interceptor;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.feilong.core.lang.reflect.FieldUtil;
import com.feilong.tools.jsonlib.JsonUtil;

/**
 * The Class AbstractHandlerInterceptorAdapter.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 1.4.0 2015年8月6日 下午10:59:16
 * @since 1.4.0
 */
public abstract class AbstractHandlerInterceptorAdapter extends HandlerInterceptorAdapter implements Ordered{

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractHandlerInterceptorAdapter.class);

    /**
     * Post construct.
     */
    @PostConstruct
    protected void postConstruct(){
        if (LOGGER.isInfoEnabled()){
            Map<String, Object> map = FieldUtil.getAllFieldNameAndValueMap(this);
            LOGGER.info("\n[{}] fieldValueMap: \n[{}]", getClass().getCanonicalName(), JsonUtil.formatSimpleMap(map));
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.core.Ordered#getOrder()
     */
    @Override
    public int getOrder(){
        return 0;
    }
}