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

import com.baozun.nebula.manager.CacheManager;
import com.feilong.core.Validator;

/**
 * @author D.C
 * @time 2016年3月24日 下午4:04:47
 */
public class TokenManagerImpl implements TokenManager {

	@Autowired
	private CacheManager cacheManager;

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
				if ((token.getCreated() + token.getLiveTime()) >= System.currentTimeMillis()) {
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

	class Token implements Serializable {
		public Token(String code, long created, long liveTime) {
			super();
			this.code = code;
			this.created = created;
			this.liveTime = liveTime;
		}

		private static final long serialVersionUID = -6460180684340341983L;

		public long getCreated() {
			return created;
		}

		public void setCreated(long created) {
			this.created = created;
		}

		public long getLiveTime() {
			return liveTime;
		}

		public void setLiveTime(long liveTime) {
			this.liveTime = liveTime;
		}

		private String code;

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		private long created;
		private long liveTime;

	}

}
