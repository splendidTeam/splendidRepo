
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
import org.springframework.beans.factory.annotation.Autowired;

import com.baozun.nebula.manager.CacheManager;


public abstract class NebulaAbstractCacheBuilder<T> {

	/**
	 * log定义
	 */
	private static final Logger	LOG = LoggerFactory.getLogger(NebulaAbstractCacheBuilder.class);
	
	private String key;
	
	private int expire;
	
	@Autowired
	protected CacheManager cacheManager;
	
	public T getCachedObject() {
		T t = null;
		
		try {
			t = cacheManager.getObject(getKey());
		} catch(Exception e) {
			LOG.error("[PDP_BUILD_PDP_VIEW_COMMAND_WITH_CACHE] pdp get pdp view command cache exception. itemCode:{}, exception:{} [{}] \"{}\"",
					"", e.getMessage(), new Date(), this.getClass().getSimpleName());
		}
		
		if(t == null) {
			t = buildCachedObject();
			
			// put to cache
			try {
				cacheManager.setObject(getKey(), t, getExpire());
			} catch(Exception e) {
				LOG.error("[PDP_BUILD_PDP_VIEW_COMMAND_WITH_CACHE] pdp get pdp view command cache exception. itemCode:{}, exception:{} [{}] \"{}\"",
						"", e.getMessage(), new Date(), this.getClass().getSimpleName());
			}
		}
		
		return t;
	}

	protected abstract T buildCachedObject();

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
