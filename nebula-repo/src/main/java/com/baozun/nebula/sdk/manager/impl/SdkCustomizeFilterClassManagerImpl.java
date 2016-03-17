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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import loxia.dao.Pagination;
import loxia.dao.Sort;
import loxia.dao.Page;

import com.baozun.nebula.model.rule.CustomizeFilterClass;
import com.baozun.nebula.model.system.ChooseOption;
import com.baozun.nebula.sdk.manager.SdkCustomizeFilterClassManager;
import com.baozun.nebula.command.rule.CustomizeFilterClassCommand;
import com.baozun.nebula.dao.rule.CustomizeFilterClassDao;
import com.baozun.nebula.dao.system.ChooseOptionDao;

/**
 * CustomizeFilterClassManager
 * @author  lxy
 *
 */
@Transactional
@Service("sdkCustomizeFilterClassManager") 
public class SdkCustomizeFilterClassManagerImpl implements SdkCustomizeFilterClassManager {

	@Autowired
	private CustomizeFilterClassDao customizeFilterClassDao;
	
	@Autowired
	private ChooseOptionDao chooseOptionDao;
	
	private final static String OPTION_GROUP_CODE_SCOPE_TYPE="PRODUCT_CUSTOMIZE_FILTER";


	/**
	 * 保存CustomizeFilterClass
	 * 
	 */
	@Override
	public CustomizeFilterClass saveCustomizeFilterClass(CustomizeFilterClass model){
	
		return customizeFilterClassDao.save(model);
	}
	
	/**
	 * 通过id获取CustomizeFilterClass
	 * 
	 */
	@Override
	@Transactional(readOnly=true)
	public CustomizeFilterClass findCustomizeFilterClassById(Long id){
	
		return customizeFilterClassDao.getByPrimaryKey(id);
	}
	@Override
	@Transactional(readOnly=true)
	public CustomizeFilterClass findCustomizeFilterClassListByServiceName(String svcName){
	
		return customizeFilterClassDao.findCustomizeFilterClassListByServiceName(svcName);
	}
	
	/**
	 * 获取所有CustomizeFilterClass列表
	 * @return
	 */
	@Override
	@Transactional(readOnly=true)
	public List<CustomizeFilterClass> findAllCustomizeFilterClassList(){
	
		return customizeFilterClassDao.findAllCustomizeFilterClassList();
	};
	
	/**
	 * 通过ids获取CustomizeFilterClass列表
	 * @param ids
	 * @return
	 */
	@Override
	@Transactional(readOnly=true)
	public List<CustomizeFilterClass> findCustomizeFilterClassListByIds(List<Long> ids){
	
		return customizeFilterClassDao.findCustomizeFilterClassListByIds(ids);
	};
	
	/**
	 * 通过参数map获取CustomizeFilterClass列表
	 * @param paraMap
	 * @return
	 */
	@Override
	@Transactional(readOnly=true)
	public List<CustomizeFilterClass> findCustomizeFilterClassListByQueryMap(Map<String, Object> paraMap){
	
		return customizeFilterClassDao.findCustomizeFilterClassListByQueryMap(paraMap);
	};
	
	/**
	 * 分页获取CustomizeFilterClass列表
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	@Override
	@Transactional(readOnly=true)
	public Pagination<CustomizeFilterClassCommand> findCustomizeFilterClassListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap){
		Pagination<CustomizeFilterClassCommand> resultCmdList = 
				customizeFilterClassDao.findCustomizeFilterClassListByQueryMapWithPage(page,sorts,paraMap);
		
		List<String> groupCodes=new ArrayList<String>();
		groupCodes.add(OPTION_GROUP_CODE_SCOPE_TYPE);
		List<ChooseOption> optionList=chooseOptionDao.findChooseOptionValue(groupCodes);
		
		Map<String,String> optionMap=new HashMap<String,String>();
		for(ChooseOption co:optionList){
			optionMap.put(co.getGroupCode()+"-"+co.getOptionValue(), co.getOptionLabel());
		}
		
		for(CustomizeFilterClassCommand cfCmd :resultCmdList.getItems()){
			cfCmd.setScopeTypeName(optionMap.get(OPTION_GROUP_CODE_SCOPE_TYPE+"-"+cfCmd.getScopeType()));
		}
		
		return resultCmdList;
	};
	
	
	
	/**
	 * 通过ids批量启用或禁用CustomizeFilterClass
	 * 设置lifecycle =0 或 1
	 * @param ids
	 * @return
	 */
	@Override
	public void enableOrDisableCustomizeFilterClassByIds(List<Long> ids,Integer state){
		customizeFilterClassDao.enableOrDisableCustomizeFilterClassByIds(ids,state);
	}
	
	/**
	 * 通过ids批量删除CustomizeFilterClass
	 * 设置lifecycle =2
	 * @param ids
	 * @return
	 */
	@Override
	public void removeCustomizeFilterClassByIds(List<Long> ids){
		customizeFilterClassDao.removeCustomizeFilterClassByIds(ids);
	}
	
	
	/**
	 * 获取有效的CustomizeFilterClass列表
	 * lifecycle =1
	 * @param ids
	 * @return
	 */
	@Override
	@Transactional(readOnly=true)
	public List<CustomizeFilterClass> findAllEffectCustomizeFilterClassList(){
	
		return customizeFilterClassDao.findAllEffectCustomizeFilterClassList();
	};
	
	/**
	 * 通过参数map获取有效的CustomizeFilterClass列表
	 * 强制加上lifecycle =1
	 * @param paraMap
	 * @return
	 */
	@Override
	@Transactional(readOnly=true)
	public List<CustomizeFilterClass> findEffectCustomizeFilterClassListByQueryMap(Map<String, Object> paraMap){
	
		return customizeFilterClassDao.findEffectCustomizeFilterClassListByQueryMap(paraMap);
	};
	
	/**
	 * 分页获取有效的CustomizeFilterClass列表
	 * 强制加上lifecycle =1
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	@Override
	@Transactional(readOnly=true)
	public Pagination<CustomizeFilterClass> findEffectCustomizeFilterClassListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap){
	
		return customizeFilterClassDao.findEffectCustomizeFilterClassListByQueryMapWithPage(page,sorts,paraMap);
	}

	@Override
	@Transactional(readOnly=true)
	public Pagination<CustomizeFilterClassCommand> findCustomizeFilterClassListByQueryMapAndShopIdWithPage(
			Page page, Sort[] sorts, Map<String, Object> paraMap, Long shopId) {
		
		Pagination<CustomizeFilterClassCommand> resultCmdList = 
				customizeFilterClassDao.findCustomizeFilterClassListByQueryMapAndShopIdWithPage(page,sorts,paraMap,shopId);
		
		List<String> groupCodes=new ArrayList<String>();
		groupCodes.add(OPTION_GROUP_CODE_SCOPE_TYPE);
		List<ChooseOption> optionList=chooseOptionDao.findChooseOptionValue(groupCodes);
		
		Map<String,String> optionMap=new HashMap<String,String>();
		for(ChooseOption co:optionList){
			optionMap.put(co.getGroupCode()+"-"+co.getOptionValue(), co.getOptionLabel());
		}
		
		for(CustomizeFilterClassCommand cfCmd :resultCmdList.getItems()){
			cfCmd.setScopeTypeName(optionMap.get(OPTION_GROUP_CODE_SCOPE_TYPE+"-"+cfCmd.getScopeType()));
		}
		
		return resultCmdList;
	}

	@Override
	public void createOrUpdate(CustomizeFilterClass customizeFilterClass) {
		
		//更新
		if(customizeFilterClass.getId()!=null){
			CustomizeFilterClass csPersistent =customizeFilterClassDao.getByPrimaryKey(customizeFilterClass.getId());
			
			csPersistent.setCacheSecond(customizeFilterClass.getCacheSecond());
			csPersistent.setScopeName(customizeFilterClass.getScopeName());
			csPersistent.setScopeType(customizeFilterClass.getScopeType());
			csPersistent.setServiceName(customizeFilterClass.getServiceName());
			csPersistent.setShopId(customizeFilterClass.getShopId());
			csPersistent.setVersion(new Date());
			csPersistent.setCacheVersion(new Date());
			//更新后设置为禁用，需手动启用(enable+测试通过)
			csPersistent.setLifecycle(CustomizeFilterClass.LIFECYCLE_DISABLE);
			customizeFilterClassDao.save(csPersistent);
		}else{
			customizeFilterClass.setCacheVersion(new Date());
			customizeFilterClass.setVersion(new Date());
			//初始状态也为禁用
			customizeFilterClass.setLifecycle(CustomizeFilterClass.LIFECYCLE_DISABLE);
			customizeFilterClassDao.save(customizeFilterClass);
		}
		
	}

	@Override
	public void updateCacheVersion(Long id) {
		CustomizeFilterClass csPersistent =customizeFilterClassDao.getByPrimaryKey(id);
		csPersistent.setCacheVersion(new Date());
		customizeFilterClassDao.save(csPersistent);
	}

	@Override
	@Transactional(readOnly=true)
	public List<CustomizeFilterClass> findEffectCustomizeFilterClassListByTypeAndShopId(Integer type, Long shopId) {
		return customizeFilterClassDao.findEffectCustomizeFilterClassListByTypeAndShopId(type, shopId);
	};
}
