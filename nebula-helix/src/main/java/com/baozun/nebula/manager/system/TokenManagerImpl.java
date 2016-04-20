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
package com.baozun.nebula.manager.system;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baozun.nebula.manager.CacheManager;
import com.feilong.core.Validator;

import redis.clients.jedis.JedisSentinelPool;

/**
 * @author D.C
 * @time 2016年3月24日 下午4:04:47
 */
@Service("tokenManager")
public class TokenManagerImpl implements TokenManager, Serializable {

	private static final long serialVersionUID = 7920926916890294158L;
	@Autowired
	private CacheManager cacheManager;

	@Autowired(required = false)
	private JedisSentinelPool jedisPool;

	@Override
	public void saveToken(String businessCode, String human, int liveTime, String code) {
		cacheManager.setObject(generateKey(businessCode, human),
				new Token(code, System.currentTimeMillis(), (long) liveTime), liveTime * 2);
	}

	@Override
	public VerifyResult verifyToken(String businessCode, String human, String code) {
		Token token = cacheManager.getObject(generateKey(businessCode, human));
		if (!Validator.isNullOrEmpty(token)) {
			if (code.equals(token.getCode())) {
				cacheManager.remove(generateKey(businessCode, human));
				if ((token.getCreated() + token.getLiveTime() * 1000) >= System.currentTimeMillis()) {
					return VerifyResult.SUCESS;
				} else {
					return VerifyResult.EXPIRED;
				}
			}
		}
		return VerifyResult.FAILURE;
	}

	private String generateKey(String businessCode, String human) {
		return "token_" + businessCode + "_" + human;
	}

	public CacheManager getCacheManger() {
		return cacheManager;
	}

	public void setCacheManger(CacheManager cacheManger) {
		this.cacheManager = cacheManger;
	}

	@Override
	public VerifyResult verifyAccess(String businessCode, String human, RollingTimeWindow rollingTimeWindow) {
		String key = generateKey(businessCode, human) + rollingTimeWindow;
		return cacheManager.applyRollingTimeWindow(key, rollingTimeWindow.getLimit(), rollingTimeWindow.getWindow())
				? VerifyResult.SUCESS : VerifyResult.LIMITED ;
	}
}
