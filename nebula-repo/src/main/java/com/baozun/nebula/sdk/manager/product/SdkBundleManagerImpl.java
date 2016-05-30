/**
 
* Copyright (c) 2014 Baozun All Rights Reserved.
 
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
package com.baozun.nebula.sdk.manager.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baozun.nebula.command.product.BundleSkuPriceCommand;
import com.baozun.nebula.dao.product.BundleSkuDao;

/**
 * @author yue.ch
 * @time 2016年5月30日 上午11:29:54
 */
@Service
public class SdkBundleManagerImpl implements SdkBundleManager {
	
	@Autowired
	private BundleSkuDao bundleSkuDao;

	/* (non-Javadoc)
	 * @see com.baozun.nebula.manager.product.NebulaBundleManager#getBundleSkuPrice(java.lang.Long, java.lang.Long)
	 */
	@Override
	public BundleSkuPriceCommand getBundleSkuPrice(Long bundleItemId, Long skuId) {
		return bundleSkuDao.getBundleSkuPrice(bundleItemId, skuId);
	}

	/* (non-Javadoc)
	 * @see com.baozun.nebula.manager.product.NebulaBundleManager#getBundleSkusPrice(java.lang.Long, java.util.List)
	 */
	@Override
	public List<BundleSkuPriceCommand> getBundleSkusPrice(Long bundleItemId, List<Long> skuIds) {
		return bundleSkuDao.getBundleSkusPrice(bundleItemId, skuIds);
	}
}
