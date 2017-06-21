/**
 * Copyright (c) 2010 Jumbomart All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Jumbomart.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Jumbo.
 *
 * JUMBOMART MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. JUMBOMART SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.manager;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import com.baozun.nebula.command.cache.CacheItemCommand;
import com.baozun.nebula.dao.system.CacheItemDao;
import com.baozun.nebula.model.system.MataInfo;
import com.baozun.nebula.sdk.manager.SdkMataInfoManager;
import com.baozun.nebula.utilities.common.ProfileConfigUtil;
import com.baozun.nebula.utilities.common.SerializableUtil;

import static com.feilong.core.Validator.isNullOrEmpty;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.18
 */
public abstract class AbstractCacheManager implements CacheManager{

    /**
     * 配置不同环境不同项目的key前辍
     */
    private static final String REDIS_KEY_START = "redis.keystart";

    @Autowired
    private CacheItemDao cacheItemDao;

    @Autowired
    private SdkMataInfoManager sdkMataInfoManager;

    /**
     * 无论是key为null或是配置的key前辍为null,都返回key的值
     * 
     * @param key
     * @return
     */
    protected String processKey(String key){
        String confKeyStart = getPrefix();
        if (StringUtils.isBlank(key) || StringUtils.isBlank(confKeyStart)){
            return key;
        }
        return confKeyStart + "_" + key;
    }

    /**
     * 配置文件中的前缀
     * 
     * @return
     */
    //FIXME 可重构 常量或者配置式的
    protected String getPrefix(){
        Properties properties = ProfileConfigUtil.findPro("config/redis.properties");
        return properties.getProperty(REDIS_KEY_START, null);
    }

    /**
     * @param key
     * @return
     */
    protected String buildCleanKeyPattern(String key){
        String confKeyStart = getPrefix();
        if (isNullOrEmpty(confKeyStart)){
            return key + "*";
        }
        //因为setObject 这个方法 保存的cache的可以是 keyStart_keyStart_key,
        //setValue 的key 是keystart_key
        //所以这里根据key来删除就拼成 keystart*key* 这种
        return confKeyStart + "*" + key + "*";
    }

    /**
     * 返回true表示会使用缓存
     * 
     * @return
     */
    protected boolean useCache(){
        String value = sdkMataInfoManager.findValue(MataInfo.KEY_HAS_CACHE);
        return value != null && "true".equals(value);
    }

    @Override
    public List<CacheItemCommand> findAllCacheItem(Map<String, Object> paraMap){
        // 获取全部的缓存项
        return cacheItemDao.findAllCacheItem(new BeanPropertyRowMapper<CacheItemCommand>(CacheItemCommand.class), paraMap);
    }

    @Override
    public <T> void setObject(String key,T t){
        String value = SerializableUtil.convert2String((Serializable) t);
        setValue(key, value);
    }

    @Override
    public <T> void setObject(String key,T t,Integer expireSeconds){
        String value = SerializableUtil.convert2String((Serializable) t);
        setValue(key, value, expireSeconds);
    }

    @Override
    public <T> T getObject(String key){
        String value = getValue(key);
        if (StringUtils.isBlank(value)){
            return null;
        }
        return (T) SerializableUtil.convert2Object(value);
    }

}
