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
 * 平台访问策略中控类
 * 可以用于验证码的保存、验证、访问控制
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
	 * 验证
	 * @param businessCode 业务编码
	 * @param human  人的识别码
	 * @param token 验证码
	 * @return
	 */
	VerifyResult verifyToken(String businessCode, String human, String token);
	
	enum VerifyResult {
		SUCESS, FAILURE, EXPIRED, LIMITED;
	}
	
	/**
	 * Rolling Time Window, 用于控制用户在某个时间窗口内的访问次数，
	 * 比如：发送短信业务，5分钟内可以发送2次，24小时内可以发送5次
	 * 完成这种访问控制需要定义2个RollingTimeWindow(5,2),(24,5)对象，分两次调用verifyAccess来达成目标
	 * 如果验证通过后需要将本次调用添加进去
	 * 基于redis集合特性实现，key = businessCode + "_" +　human +　"_" + rollingTimeWindow.toString()
	 * @param businessCode 业务编码
	 * @param human  人的识别码
	 * @param rollingTimeWindow 时间窗
	 * @return　验证结果
	 */
	VerifyResult verifyAccess(String businessCode, String human, RollingTimeWindow rollingTimeWindow);
	
	
	
}
