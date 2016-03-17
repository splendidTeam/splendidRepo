/**
 * Copyright (c) 2012 Baozun All Rights Reserved.
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
package com.baozun.nebula.sdk.manager.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.command.product.ItemVisibilityCommand;
import com.baozun.nebula.dao.product.ItemVisibilityDao;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.model.product.ItemVisibility;
import com.baozun.nebula.sdk.manager.SdkEngineManager;
import com.baozun.nebula.sdk.manager.SdkItemVisibilityManager;
import com.baozun.nebula.utilities.common.Validator;

/**
 * ItemVisibilityManager
 * @author  何波
 *
 */
@Transactional
@Service("sdkItemVisibilityManager") 
public class SdkItemVisibilityManagerImpl implements SdkItemVisibilityManager {

	@Autowired
	private ItemVisibilityDao itemVisibilityDao;

	@Autowired
	private SdkEngineManager sdkEngineManager;
	/**
	 * 保存ItemVisibility
	 * 
	 */
	public ItemVisibility saveItemVisibility(ItemVisibility model){
		Long id  = model.getId();
//		ItemVisibility visibility = findItemVisibilityByMemFilterId(model.getMemFilterId());
//		if(id!=null && visibility != null && !visibility.getId().equals(id)){
//			throw new  BusinessException("商品可见性的会员筛选器已经存在");
//		}
//		if(id == null && visibility != null){
//			throw new  BusinessException("商品可见性的会员筛选器已经存在");
//		}
		ItemVisibility visibility1 = findItemVisibilityByItemFilterId(model.getItemFilterId(),model.getMemFilterId());
		if(visibility1 != null && !visibility1.getId().equals(id)){
			throw new  BusinessException("商品可见性已经存在");
		}
		if(id != null){
			ItemVisibility db = findItemVisibilityById(id);
			db.setMemFilterId(model.getMemFilterId());
			db.setItemFilterId(model.getItemFilterId());
			return itemVisibilityDao.save(db);
		}
		return itemVisibilityDao.save(model);
	}
	
	/**
	 * 通过id获取ItemVisibility
	 * 
	 */
	@Transactional(readOnly=true)
	public ItemVisibility findItemVisibilityById(Long id){
		ItemVisibility visibility = itemVisibilityDao.getByPrimaryKey(id);
		return visibility;
	}

	/**
	 * 获取所有ItemVisibility列表
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<ItemVisibility> findAllItemVisibilityList(){
	
		return itemVisibilityDao.findAllItemVisibilityList();
	};
	
	/**
	 * 通过ids获取ItemVisibility列表
	 * @param ids
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<ItemVisibility> findItemVisibilityListByIds(List<Long> ids){
	
		return itemVisibilityDao.findItemVisibilityListByIds(ids);
	};
	
	/**
	 * 通过参数map获取ItemVisibility列表
	 * @param paraMap
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<ItemVisibility> findItemVisibilityListByQueryMap(Map<String, Object> paraMap){
	
		return itemVisibilityDao.findItemVisibilityListByQueryMap(paraMap);
	};
	
	/**
	 * 分页获取ItemVisibility列表
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	@Transactional(readOnly=true)
	public Pagination<ItemVisibilityCommand> findItemVisibilityListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap){
	
		return itemVisibilityDao.findItemVisibilityListByQueryMapWithPage(page,sorts,paraMap);
	};
	
	
	
	/**
	 * 通过ids批量启用或禁用ItemVisibility
	 * 设置lifecycle =0 或 1
	 * @param ids
	 * @return
	 */
	public void enableOrDisableItemVisibilityByIds(List<Long> ids,Integer state){
		itemVisibilityDao.enableOrDisableItemVisibilityByIds(ids,state);
	}
	
	/**
	 * 通过ids批量删除ItemVisibility
	 * 设置lifecycle =2
	 * @param ids
	 * @return
	 */
	public void removeItemVisibilityByIds(List<Long> ids){
		itemVisibilityDao.removeItemVisibilityByIds(ids);
	}
	
	
	/**
	 * 获取有效的ItemVisibility列表
	 * lifecycle =1
	 * @param ids
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<ItemVisibility> findAllEffectItemVisibilityList(){
	
		return itemVisibilityDao.findAllEffectItemVisibilityList();
	};
	
	/**
	 * 通过参数map获取有效的ItemVisibility列表
	 * 强制加上lifecycle =1
	 * @param paraMap
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<ItemVisibility> findEffectItemVisibilityListByQueryMap(Map<String, Object> paraMap){
	
		return itemVisibilityDao.findEffectItemVisibilityListByQueryMap(paraMap);
	};
	
	/**
	 * 分页获取有效的ItemVisibility列表
	 * 强制加上lifecycle =1
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	@Transactional(readOnly=true)
	public Pagination<ItemVisibility> findEffectItemVisibilityListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap){
	
		return itemVisibilityDao.findEffectItemVisibilityListByQueryMapWithPage(page,sorts,paraMap);
	}

	@Override
	@Transactional(readOnly=true)
	public ItemVisibilityCommand findItemVisibilityCommandbyId(Long id) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		List<ItemVisibilityCommand>  commands = itemVisibilityDao.findItemVisibilityCommand(params);
		if(commands!=null && commands.size()>0){
			return commands.get(0);
		}
		return null;
	}

	@Override
	@Transactional(readOnly=true)
	public ItemVisibility findItemVisibilityByMemFilterId(Long memFilterId) {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("memFilterId", memFilterId);
		List<ItemVisibility> visibilities = itemVisibilityDao.findItemVisibilityListByQueryMap(params);
		if(visibilities!=null && visibilities.size()>0){
			return visibilities.get(0);
		}
		return null;
	}

	@Override
	@Transactional(readOnly=true)
	public ItemVisibility findItemVisibilityByItemFilterId(Long itemFilterId,Long memFilterId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("itemFilterId", itemFilterId);
		params.put("memFilterId", memFilterId);
		List<ItemVisibility> visibilities = itemVisibilityDao.findItemVisibilityListByQueryMap(params);
		if(visibilities!=null && visibilities.size()>0){
			return visibilities.get(0);
		}
		return null;
	}

	@Override
	@Transactional(readOnly=true)
	public List<ItemVisibility> findItemVisibilityListByItemFilterIds(
			List<String> itemFilterIds) {
		if(itemFilterIds == null || itemFilterIds.size() == 0){
			return null;
		}
		return itemVisibilityDao.findItemVisibilityListByItemFilterIds(itemFilterIds);
	}
	
	@Override
	public Set<Long> getMembersByItemId(Long itemId) {
		if(itemId == null){
			return null;
		}
		String id = String.valueOf(itemId);
		//根据商品id查询商品筛选器
		Set<String> itemFilterIds = sdkEngineManager.getItemScopeListByItemAndCategory(id, null);
	 	if(Validator.isNullOrEmpty(itemFilterIds)){
	 		return null;
	 	}
	 	List<String> itemFilterIdList = Arrays.asList(itemFilterIds.toArray(new String[]{})) ;
	 	//根据查询出来的商品筛选器查询可见性
		List<ItemVisibility> visibilities = this.findItemVisibilityListByItemFilterIds(itemFilterIdList);
		if(Validator.isNullOrEmpty(visibilities)){
			return null;
		}
	 	//会员集合
	 	Set<Long> memFilterIds = new HashSet<Long>();
	 	for (ItemVisibility ib : visibilities) {
			memFilterIds.add(ib.getMemFilterId());
		}
		return memFilterIds;
	}
	@Override
	public Boolean checkItemIsVisibility(Long itemId, Long memId) {
		if(itemId == null){
			return true;
		}
		String id = String.valueOf(itemId);
		Set<String> itemFilterIds = sdkEngineManager.getItemScopeListByItemAndCategory(id, null);
		//如果为空则商品没有建立商品筛选器直接访问
	 	if(Validator.isNullOrEmpty(itemFilterIds)){
	 		return true;
	 	}
	    List<String> itemFilterIdList = Arrays.asList(itemFilterIds.toArray(new String[]{})) ;
		List<ItemVisibility> visibilities = this.findItemVisibilityListByItemFilterIds(itemFilterIdList);
		//如果为空则商品没有建立可见性关系可访问
		if(Validator.isNullOrEmpty(visibilities)){
			return true;
		}
		//游客时 memId为空存在可见性关系则不能访问
		if(memId == null && visibilities.size() > 0){
			return false;
		}
		//通过会员获取筛选会员id
	 	Set<String> memFilterIds = sdkEngineManager.getCrowdScopeListByMemberAndGroup(memId, null);
	 	//如果会员没在人群筛选器中则没有访问权限
	 	if(Validator.isNullOrEmpty(memFilterIds)){
	 		return false;
	 	}
	 	for (ItemVisibility ib : visibilities) {
			String memFilterId = String.valueOf(ib.getMemFilterId());
			//如果查询出来的可见性存在于会员所在的会员筛选器中则有权限访问
			if(memFilterIds.contains(memFilterId) ){
				return true;
			}
		}
		return false;
	}
}
