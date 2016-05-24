package com.baozun.nebula.dao.offlineStore;

import java.util.List;
import java.util.Map;

import com.baozun.nebula.model.offlineStore.OfflineStore;

import loxia.annotation.NativeQuery;
import loxia.annotation.NativeUpdate;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;
import loxia.dao.Page;
import loxia.dao.Pagination;

public interface OfflineStoreDao extends GenericEntityDao<OfflineStore, Long>{
	
	 
	
	/**
	 * 分页查询店铺 
	 */
	@NativeQuery (model=OfflineStore.class, pagable = true)
	Pagination<OfflineStore> findOfflineStoreListByPage(Page page, @QueryParam Map<Object, Object> params);
	

	/**
	 * 查询店铺所有的省
	 */
	@NativeQuery (model=OfflineStore.class)
	List<OfflineStore> findOfflineStoreProvince();
	/**
	 * 查询店铺所有的市
	 */
	@NativeQuery (alias = "city", clazzes = String.class)
	List<String> findAllOfflineStoreCity();
	/**
	 * 查询省对应的市
	 */
	@NativeQuery (model=OfflineStore.class)
	List<OfflineStore> findOfflineStoreCity(@QueryParam Map<Object, Object> params);
	/**
	 * 根据id查询一个店铺
	 */
	@NativeQuery (model=OfflineStore.class)
	OfflineStore findOfflineStoreById(@QueryParam Map<Object, Object> params);
	/**
	 * 查询所有店铺
	 */
	@NativeQuery (model=OfflineStore.class)
	List<OfflineStore> findOfflineStoreList();
	/**
	 * 新增店铺
	 */
	@NativeUpdate
	int insertStore(@QueryParam Map<String, Object> params);
	/**
	 * 修改店铺
	 */
	@NativeUpdate
	int updateStore(@QueryParam Map<String, Object> params);
	/**
	 * 删除店铺
	 */
	@NativeUpdate
	int deleteStore(@QueryParam("id") Long id);
}
