package com.baozun.nebula.manager.offlineStore;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.dao.offlineStore.OfflineStoreDao;
import com.baozun.nebula.model.offlineStore.OfflineStore;

import loxia.dao.Page;
import loxia.dao.Pagination;

@Service("offlineStoreManager")
@Transactional
public class OfflineStoreManagerImpl implements OfflineStoreManager {

	private static final Logger log = LoggerFactory.getLogger(OfflineStoreManagerImpl.class);
	@Autowired
	private OfflineStoreDao offlineStoreDao;
	
	
	@Override
	public Pagination<OfflineStore> findOfflineStoreListByPage(Page page, Map<Object, Object> params) {
		return offlineStoreDao.findOfflineStoreListByPage(page, params);
	}

	@Override
	public OfflineStore findOfflineStoreById(Map<Object, Object> params) {
		return offlineStoreDao.findOfflineStoreById(params);
	}

	@Override
	public List<OfflineStore> findOfflineStoreList() {
		return offlineStoreDao.findOfflineStoreList();
	}
	
	@Override
	public int insertStore(Map<String, Object> params) {
		return offlineStoreDao.insertStore(params);
	}

	@Override
	public int updateStore(Map<String, Object> params) {
		return offlineStoreDao.updateStore(params);
	}

	@Override
	public int deleteStore(Long id) {
		return offlineStoreDao.deleteStore(id);
	}

}
