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
package com.baozun.nebula.manager.product;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baozun.nebula.command.product.BundleCommand;
import com.baozun.nebula.dao.product.BundleDao;
import com.baozun.nebula.dao.product.BundleElementDao;
import com.baozun.nebula.dao.product.BundleSkuDao;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.ErrorCodes;
import com.baozun.nebula.model.bundle.Bundle;

/**
 * @author yue.ch
 * @time 2016年5月25日 下午6:02:58
 */
@Service
public class BundleManagerImpl implements BundleManager {
	
	private static final Logger LOG = LoggerFactory.getLogger(BundleManagerImpl.class);
	
	@Autowired
	private BundleDao bundleDao;
	
	@Autowired
	private BundleElementDao bundleElementDao;
	
	@Autowired
	private BundleSkuDao bundleSkuDao;

	/* (non-Javadoc)
	 * @see com.baozun.nebula.manager.product.BundleManager#createOrUpdate(com.baozun.nebula.model.bundle.Bundle)
	 */
	@Override
	public Bundle createOrUpdate(BundleCommand bundle) {
		if(bundle != null) {
			Long id = bundle.getId();
			
			if(id != null) { // update
				
			} else { // create
				Long itemId = bundle.getItemId();
				
				// 校验指定的item是否已经存在bundle扩展信息
				if(bundleDao.findBundleByBundleItemId(itemId, null) != null) {
					throw new BusinessException(ErrorCodes.PRODUCT_CODE_REPEAT);
				}
			}
		}
		
		return bundle;
	}

}
