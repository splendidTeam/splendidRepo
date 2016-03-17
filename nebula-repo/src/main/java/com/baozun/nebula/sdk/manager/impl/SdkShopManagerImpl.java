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
package com.baozun.nebula.sdk.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.command.ShopCommand;
import com.baozun.nebula.dao.product.ShopDao;
import com.baozun.nebula.sdk.manager.SdkShopManager;

/**
 * 店铺管理实现类
 * 
 * @author chenguang.zhou
 * @date 2014年5月20日 上午10:00:45
 */
@Service("sdkShopManager")
@Transactional
public class SdkShopManagerImpl implements SdkShopManager {

	@Autowired
	private ShopDao shopDao;
	
	@Override
	@Transactional(readOnly=true)
	public ShopCommand findShopCommandByOrgName(String orgName) {
		return shopDao.findShopCommandByOrgName(orgName);
	}

}
