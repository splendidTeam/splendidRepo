/**
 * Copyright (c) 2013 Baozun All Rights Reserved.
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
package com.baozun.nebula.sdk.manager;

import com.baozun.nebula.command.ShopCommand;
import com.baozun.nebula.manager.BaseManager;

/**
 * 店铺管理
 * 
 * @author chenguang.zhou
 * @date 2014年5月20日 上午9:57:58
 */
public interface SdkShopManager extends BaseManager{

	/**
	 * 通过店铺名称查询店铺信息
	 * @param orgName
	 * @return
	 */
	public ShopCommand findShopCommandByOrgName(String orgName);
}
