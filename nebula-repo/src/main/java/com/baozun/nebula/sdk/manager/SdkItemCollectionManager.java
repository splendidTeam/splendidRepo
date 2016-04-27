package com.baozun.nebula.sdk.manager;

import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.product.ItemCollection;

public interface SdkItemCollectionManager extends BaseManager{

	ItemCollection findItemCollectionById(Long id);
}
