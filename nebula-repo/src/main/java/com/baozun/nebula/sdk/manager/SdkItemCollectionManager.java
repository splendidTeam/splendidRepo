package com.baozun.nebula.sdk.manager;

import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.product.ItemCollection;

public interface SdkItemCollectionManager extends BaseManager{

	ItemCollection findItemCollectionById(Long id);
	
	/**
	 * 根据导航id查询商品指定排序
	 * @return ItemCollection
	 * @param navigationId
	 * @author 冯明雷
	 * @time 2016年4月27日下午3:33:30
	 */
	ItemCollection findItemCollectionByNavigationId(Long navigationId);
}
