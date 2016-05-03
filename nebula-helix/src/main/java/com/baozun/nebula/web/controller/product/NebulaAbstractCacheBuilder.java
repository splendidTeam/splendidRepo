
/**
 * Copyright (c) 2016 Jumbomart All Rights Reserved.
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
package com.baozun.nebula.web.controller.product;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baozun.nebula.manager.CacheManager;
import com.baozun.nebula.utils.spring.SpringUtil;


public abstract class NebulaAbstractCacheBuilder<T, EX extends Exception> {

	/**
	 * log定义
	 */
	private static final Logger	LOG = LoggerFactory.getLogger(NebulaAbstractCacheBuilder.class);
	
	private String key;
	
	private int expire;
	
	protected CacheManager cacheManager;
	
	
	public NebulaAbstractCacheBuilder(String key, int expire) {
		super();
		this.key = key;
		this.expire = expire;
		this.cacheManager = (CacheManager)SpringUtil.getBean(CacheManager.class);
	}

	public T getCachedObject() throws EX {
		T t = null;
		
		try {
			t = cacheManager.getObject(getKey());
		} catch(Exception e) {
			LOG.error("get cache exception. exception:{} [{}] \"{}\"",
					    e.getMessage(), new Date(), this.getClass().getSimpleName());
			LOG.error("cache exception.", e);
		}
		
		if(t == null) {
			t = buildCachedObject();
			
			// put to cache
			try {
				cacheManager.setObject(getKey(), t, getExpire());
			} catch(Exception e) {
				LOG.error("set cache exception. exception:{} [{}] \"{}\"",
						    e.getMessage(), new Date(), this.getClass().getSimpleName());
				LOG.error("cache exception.", e);
			}
		}
		
		return t;
	}

	protected abstract T buildCachedObject() throws EX;

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getExpire() {
		return expire;
	}

	public void setExpire(int expire) {
		this.expire = expire;
	}

}
