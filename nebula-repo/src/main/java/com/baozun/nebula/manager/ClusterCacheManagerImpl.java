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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 为了兼容 ,此处就不适用service 标识了, 如要使用 请自行在 spring xml 中进行配置
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.18
 */
public class ClusterCacheManagerImpl extends AbstractCacheManager{

    private static final Logger LOGGER = LoggerFactory.getLogger(ClusterCacheManagerImpl.class);

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.manager.CacheManager#setValue(java.lang.String, java.lang.String, java.lang.Integer)
     */
    @Override
    public void setValue(String key,String value,Integer expireSeconds){
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.manager.CacheManager#setValue(java.lang.String, java.lang.String)
     */
    @Override
    public void setValue(String key,String value){
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.manager.CacheManager#remove(java.lang.String)
     */
    @Override
    public Long remove(String key){
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.manager.CacheManager#getValue(java.lang.String)
     */
    @Override
    public String getValue(String key){
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.manager.CacheManager#removeMapValue(java.lang.String, java.lang.String)
     */
    @Override
    public void removeMapValue(String key,String field){
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.manager.CacheManager#setMapObject(java.lang.String, java.lang.String, java.lang.Object, int)
     */
    @Override
    public <T> void setMapObject(String key,String field,T t,int seconds){
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.manager.CacheManager#getMapObject(java.lang.String, java.lang.String)
     */
    @Override
    public <T> T getMapObject(String key,String field){
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.manager.CacheManager#pushToListFooter(java.lang.String, java.lang.String)
     */
    @Override
    public void pushToListFooter(String key,String value){
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.manager.CacheManager#popListHead(java.lang.String)
     */
    @Override
    public String popListHead(String key){
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.manager.CacheManager#listLen(java.lang.String)
     */
    @Override
    public Long listLen(String key){
        // TODO Auto-generated method stub
        return 0L;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.manager.CacheManager#addSet(java.lang.String, java.lang.String[])
     */
    @Override
    public void addSet(String key,String[] values){
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.manager.CacheManager#removeFromSet(java.lang.String, java.lang.String[])
     */
    @Override
    public Long removeFromSet(String key,String[] values){
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.manager.CacheManager#Decr(java.lang.String, long)
     */
    @Override
    public Long Decr(String key,long value){
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.manager.CacheManager#incr(java.lang.String, int)
     */
    @Override
    public Long incr(String key,int expireSeconds){
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.manager.CacheManager#applyRollingTimeWindow(java.lang.String, long, long)
     */
    @Override
    public Boolean applyRollingTimeWindow(String key,long limit,long window){
        // TODO Auto-generated method stub
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.manager.CacheManager#removeByPrefix(java.lang.String)
     */
    @Override
    public int removeByPrefix(String key){
        // TODO Auto-generated method stub
        return 0;
    }

}
