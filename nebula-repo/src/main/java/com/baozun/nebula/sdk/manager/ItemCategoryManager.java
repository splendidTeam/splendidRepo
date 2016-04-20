package com.baozun.nebula.sdk.manager;

import java.util.List;

import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.product.ItemCategory;

public interface ItemCategoryManager extends BaseManager{

	/**
	 * 通过商品ID查找商品分类
	 * @param itemId
	 * @return
	 */
	public List<ItemCategory> findItemCategoryListByItemId(Long itemId);
	
}
