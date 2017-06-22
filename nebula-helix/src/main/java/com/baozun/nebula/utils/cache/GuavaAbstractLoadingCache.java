/**
 * Copyright (c) 2016 Baozun All Rights Reserved.
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
package com.baozun.nebula.utils.cache;

import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * 抽象Guava缓存类、缓存模板。
 * 子类需要实现fetchData(key)，从数据库或其他数据源（如Redis）中获取数据。
 * 子类调用getValue(key)方法，从缓存中获取数据，并处理不同的异常，比如value为null时的InvalidCacheLoadException异常。
 * 
 * @author D.C
 * @Date 2017-6-21
 * 
 * @param <K>
 *            key 类型
 * @param <V>
 *            value 类型
 */
public abstract class GuavaAbstractLoadingCache<K, V> {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    //用于初始化cache的参数及其缺省值    
    private int maximumSize = 100; //最大缓存条数，子类在构造方法中调用setMaximumSize(int size)来更改    

    private int expireAfterWriteDuration = 60; //数据存在时长，子类在构造方法中调用setExpireAfterWriteDuration(int duration)来更改    

    private TimeUnit timeUnit = TimeUnit.MINUTES; //时间单位（分钟）    

    private Date resetTime; //Cache初始化或被重置的时间    

    private long highestSize = 0; //历史最高记录数    

    private Date highestTime; //创造历史记录的时间    

    private LoadingCache<K, V> cache;

    /**
     * 通过调用getCache().get(key)来获取数据
     * 
     * @return cache
     */
    public LoadingCache<K, V> getCache() {
        if (cache == null){ //使用双重校验锁保证只有一个cache实例    
            synchronized (this){
                if (cache == null){
                    cache = CacheBuilder.newBuilder().maximumSize(maximumSize) //缓存数据的最大条目，也可以使用.maximumWeight(weight)代替    
                                    .expireAfterWrite(expireAfterWriteDuration, timeUnit) //数据被创建多久后被移除    
                                    //.recordStats() //启用统计    
                                    .build(new CacheLoader<K, V>() {
                                        @Override
                                        public V load(K key) throws Exception {
                                            return fetchData(key);
                                        }
                                    });
                    this.resetTime = new Date();
                    this.highestTime = new Date();
                    logger.debug("本地缓存{}初始化成功", this.getClass().getSimpleName());
                }
            }
        }

        return cache;
    }

    /**
     * 根据key从数据库或其他数据源中获取一个value，并被自动保存到缓存中。
     * 
     * @param key
     * @return value,连同key一起被加载到缓存中的。
     */
    protected abstract V fetchData(K key);

    /**
     * 从缓存中获取数据（第一次自动调用fetchData从外部获取数据），并处理异常
     * 
     * @param key
     * @return Value
     * @throws ExecutionException
     */
    public V getValue(K key) throws ExecutionException {
        V result = getCache().get(key);
        if (getCache().size() > highestSize){
            highestSize = getCache().size();
            highestTime = new Date();
        }

        return result;
    }

    public long getHighestSize() {
        return highestSize;
    }

    public Date getHighestTime() {
        return highestTime;
    }

    public Date getResetTime() {
        return resetTime;
    }

    public void setResetTime(Date resetTime) {
        this.resetTime = resetTime;
    }

    public int getMaximumSize() {
        return maximumSize;
    }

    public int getExpireAfterWriteDuration() {
        return expireAfterWriteDuration;
    }

    /**
     * 设置最大缓存条数
     * 
     * @param maximumSize
     */
    public void setMaximumSize(int maximumSize) {
        this.maximumSize = maximumSize;
    }

    /**
     * 设置数据存在时长（分钟）
     * 
     * @param expireAfterWriteDuration
     */
    public void setExpireAfterWriteDuration(int expireAfterWriteDuration) {
        this.expireAfterWriteDuration = expireAfterWriteDuration;
    }
}
