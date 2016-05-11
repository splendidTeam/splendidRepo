package com.baozun.nebula.sdk.manager.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.dao.product.ItemCollectionDao;
import com.baozun.nebula.model.product.ItemCollection;
import com.baozun.nebula.sdk.manager.SdkItemCollectionManager;

@Transactional
@Service("sdkItemCollectionManager")
public class SdkItemCollectionManagerImpl implements SdkItemCollectionManager {

	@Autowired
	private ItemCollectionDao	itemCollectionDao;
	
	@Override
	@Transactional(readOnly=true)
	public ItemCollection findItemCollectionById(Long id) {
		return itemCollectionDao.findItemCollectionById(id);
	}
	
	@Override
	@Transactional(readOnly = true)
	public ItemCollection findItemCollectionByNavigationId(Long navigationId){
		return itemCollectionDao.findItemCollectionByNavigationId(navigationId);
	}

}
