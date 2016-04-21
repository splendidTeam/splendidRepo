package com.baozun.nebula.sdk.manager.impl;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.dao.product.ItemCategoryDao;
import com.baozun.nebula.model.product.ItemCategory;
import com.baozun.nebula.sdk.manager.SdkItemCategoryManager;
/**
 * 商品分类
 * @author xy
 *
 */
@Transactional
@Service("sdkItemCategoryManager")
public class SdkItemCategoryManagerImpl implements SdkItemCategoryManager {

	@Autowired
	private ItemCategoryDao itemCategoryDao;
	
	@Override
	public List<ItemCategory> findItemCategoryListByItemId(Long itemId) {
		return itemCategoryDao.findItemCategoryListByItemId(itemId);
	}
	
	
}
