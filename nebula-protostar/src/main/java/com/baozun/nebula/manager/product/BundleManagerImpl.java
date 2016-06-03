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

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.api.utils.ConvertUtils;
import com.baozun.nebula.command.product.BundleCommand;
import com.baozun.nebula.command.product.BundleElementCommand;
import com.baozun.nebula.command.product.BundleItemCommand;
import com.baozun.nebula.command.product.BundleSkuCommand;
import com.baozun.nebula.dao.product.BundleDao;
import com.baozun.nebula.dao.product.BundleElementDao;
import com.baozun.nebula.dao.product.BundleSkuDao;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.ErrorCodes;
import com.baozun.nebula.model.bundle.Bundle;
import com.baozun.nebula.model.bundle.BundleElement;
import com.baozun.nebula.model.bundle.BundleSku;

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
	@Transactional
	public Bundle createOrUpdate(BundleCommand bundle) {
		if(bundle == null) {
			throw new BusinessException("");
		}
		
		Bundle b = null;
		
		Long id = bundle.getId();
		if(id != null) { // update
			// 清空从表数据
			bundleElementDao.deleteByBundleId(id);
			bundleSkuDao.deleteByBundleId(id);
			
			b = bundleDao.getByPrimaryKey(id);
			b.setAvailableQty(bundle.getAvailableQty());
			b.setCreateTime(bundle.getCreateTime());
			b.setItemId(bundle.getItemId());
			b.setModifyTime(new Date());
			b.setPriceType(bundle.getPriceType());
			b.setSyncWithInv(bundle.getSyncWithInv());
		} else { // create
			Long itemId = bundle.getItemId();
			
			// 校验指定的item是否已经存在bundle扩展信息
			if(bundleDao.findBundleByBundleItemId(itemId, null) != null) {
				throw new BusinessException(ErrorCodes.PRODUCT_CODE_REPEAT);
			}
			
			b = (Bundle) ConvertUtils.convertTwoObject(new Bundle(), bundle);
			b.setCreateTime(new Date());
		}
		
		bundleDao.save(b);
		bundle.setId(b.getId());
		
		saveBundleElementAndSku(bundle);
		
		return bundle;
	}
	
	private void saveBundleElementAndSku(BundleCommand bundle) {
		
		Long id = bundle.getId();
		
		List<BundleElementCommand> bundleElementCommands = bundle.getBundleElementCommands();
		for(BundleElementCommand bec : bundleElementCommands) {
			bec.setBundleId(id);
			BundleElement be = (BundleElement) ConvertUtils.convertTwoObject(new BundleElement(), bec);
			bundleElementDao.save(be);
			bec.setId(be.getId());
			
			List<BundleItemCommand> bundleItemCommands = bec.getItems();
			for(BundleItemCommand bc : bundleItemCommands) {
				List<BundleSkuCommand> bundleSkuCommands = bc.getBundleSkus();
				for(BundleSkuCommand bsc : bundleSkuCommands) {
					bsc.setBundleElementId(bec.getId());
					bsc.setBundleId(id);
					bsc.setItemId(bc.getItemId());
					BundleSku bs = (BundleSku) ConvertUtils.convertTwoObject(new BundleSku(), bsc);
					bundleSkuDao.save(bs);
					bsc.setId(bs.getId());
				}
			}
		}
	}

}
