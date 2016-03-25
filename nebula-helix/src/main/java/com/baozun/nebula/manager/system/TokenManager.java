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


/**
 * 验证码保存和验证的类
 * @author D.C
 * @version 2016年3月24日 下午3:33:50
 */
public interface TokenManager {
	/**
	 * 保存验证码
	 * @param businessCode 业务编码
	 * @param human 人的识别码
	 * @param liveTime 验证码的存活时间，单位是秒
	 * @param token 验证码
	 */
	void saveToken(String businessCode, String human, int liveTime, String token);
	/**
	 * 
	 * @param businessCode 业务编码
	 * @param human  人的识别码
	 * @param token 验证码
	 * @return
	 */
	VerifyResult verifyToken(String businessCode, String human, String token);
	
	enum VerifyResult {
		SUCESS, FAILURE, EXPIRED;
	}
}
