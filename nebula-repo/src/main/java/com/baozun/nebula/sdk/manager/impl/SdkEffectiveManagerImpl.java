package com.baozun.nebula.sdk.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.dao.product.ItemDao;
import com.baozun.nebula.dao.product.SdkSkuInventoryDao;
import com.baozun.nebula.dao.product.SkuDao;
import com.baozun.nebula.model.product.SkuInventory;
import com.baozun.nebula.sdk.constants.Constants;
import com.baozun.nebula.sdk.manager.SdkEffectiveManager;

@Transactional
@Service("sdkEffectiveManager")
public class SdkEffectiveManagerImpl implements SdkEffectiveManager {

	@Autowired
	private SdkSkuInventoryDao sdkInventoryDao;

	@Autowired
	private SkuDao skuDao;

	@Autowired
	private ItemDao itemDao;

	@Override
	@Transactional(readOnly=true)
	public boolean chckInventory(String extentionCode, Integer qty) {
		boolean flag = false;
		SkuInventory sdkInventory = sdkInventoryDao.findSkuInventoryByExtentionCode(extentionCode);
		if (null == sdkInventory)
			return flag;
		if (sdkInventory.getAvailableQty() >= qty) {
			flag = true;
		}
		return flag;
	}

	@Override
	public Integer checkItemIsValid(boolean valid) {
		if (!valid) {
			return Constants.CHECK_VALID_FAILURE;
		}
		return Constants.SUCCESS;
	}

}
