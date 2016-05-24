package com.baozun.nebula.manager.offlineStore;

import java.util.List;
import java.util.Map;

import com.baozun.nebula.model.offlineStore.OfflineStore;

import loxia.annotation.QueryParam;
import loxia.dao.Page;
import loxia.dao.Pagination;

public interface OfflineStoreManager {
	
	Pagination<OfflineStore> findOfflineStoreListByPage(Page page,  Map<Object, Object> params);
	
	OfflineStore findOfflineStoreById(Map<Object, Object> params);
	
	List<OfflineStore> findOfflineStoreList();
	
	int insertStore(Map<String, Object> params);
	
	int updateStore(Map<String, Object> params);
	
	int deleteStore(Long id);
}
