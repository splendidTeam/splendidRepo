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

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.baozun.nebula.manager.CacheManager;
import com.feilong.core.TimeInterval;
import com.feilong.core.Validator;
import com.feilong.core.bean.ConvertUtil;

/**
 * 基于 {@link com.baozun.nebula.manager.CacheManager}的计数器实现.
 * 
 * <h3>主要提供以下方法实现:</h3>
 * 
 * <blockquote>
 * <ol>
 * <li>{@link #incr(String,String, HttpServletRequest)}计数器 +1</li>
 * <li>{@link #getCount(String, String,HttpServletRequest)} 获得当前计数器的值</li>
 * <li>{@link #clear(String,String, HttpServletRequest)} 指定功能计算器清零</li>
 * </ol>
 * </blockquote>
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 1.5.3 2016年3月28日 上午1:26:18
 * @since 1.5.3
 * @deprecated 请使用 feilong-captch ,进行了框架的升级
 */
@Deprecated
public class CacheServerCounterImpl implements ServerCounter{

    /** The Constant LOGGER. */
    private static final Logger LOGGER                    = LoggerFactory.getLogger(CacheServerCounterImpl.class);

    /** server counter key的前缀 <code>{@value}</code>. */
    private static final String COUNTER_SERVER_KET_PREFIX = CacheServerCounterImpl.class.getName();

    /** The cache manager. */
    @Autowired
    private CacheManager        cacheManager;

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.manager.captcha.counter.ServerCounter#getCount(java.lang.String, java.lang.String,
     * javax.servlet.http.HttpServletRequest)
     */
    @Override
    public int getCount(String id,String humanKeyValue,HttpServletRequest request){
        Validate.notEmpty(id, "id can't be null/empty!");

        if (Validator.isNullOrEmpty(humanKeyValue)){
            LOGGER.debug("when id is:[{}],humanKeyValue isNullOrEmpty,return 0", id);
            return 0;
        }

        String counterKey = getCounterKey(id, humanKeyValue, request);

        try{
            Object counter = cacheManager.getObject(counterKey);

            if (null == counter){
                LOGGER.debug("Cache has no attribute:[{}],return 0", counterKey);
                return 0;
            }
            LOGGER.debug("counterKey:[{}] in Cache value is:[{}]", counterKey, counter);
            return ConvertUtil.toInteger(counter);
        }catch (Exception e){
            LOGGER.error("", e);
            return 0;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.manager.captcha.counter.ServerCounter#incr(java.lang.String, java.lang.String,
     * javax.servlet.http.HttpServletRequest)
     */
    @Override
    public void incr(String id,String humanKeyValue,HttpServletRequest request){
        String counterKey = getCounterKey(id, humanKeyValue, request);

        try{
            Long result = cacheManager.incr(counterKey, getExpireSeconds());
            LOGGER.debug("server counter incr counterKey:[{}],result value is:[{}]", counterKey, result);
        }catch (Exception e){
            LOGGER.error("", e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.manager.captcha.counter.ServerCounter#clear(java.lang.String, java.lang.String,
     * javax.servlet.http.HttpServletRequest)
     */
    @Override
    public void clear(String id,String humanKeyValue,HttpServletRequest request){
        try{
            String counterKey = getCounterKey(id, humanKeyValue, request);
            cacheManager.remove(counterKey);

            LOGGER.debug("remove cache Attribute counterKey:[{}]", counterKey);
        }catch (Exception e){
            LOGGER.error("", e);
        }
    }

    /**
     * 获得 counter key.
     *
     * @param id
     *            标识是哪个功能
     * @param humanKeyValue
     *            the human key value
     * @param request
     *            the request
     * @return the counter key
     */
    private static String getCounterKey(String id,String humanKeyValue,HttpServletRequest request){
        return COUNTER_SERVER_KET_PREFIX + "." + id + "." + humanKeyValue;
    }

    /**
     * 过期时间,目前设置为一天.
     *
     * @return the expire seconds
     */
    private int getExpireSeconds(){
        return TimeInterval.SECONDS_PER_DAY;
    }
}
