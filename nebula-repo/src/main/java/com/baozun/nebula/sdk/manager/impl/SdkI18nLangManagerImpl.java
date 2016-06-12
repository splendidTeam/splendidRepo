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
import java.util.List;
import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.curator.ZKWatchPath;
import com.baozun.nebula.curator.ZkOperator;
import com.baozun.nebula.curator.invoke.I18nLangWatchInvoke;
import com.baozun.nebula.curator.invoke.ModuleMapWatchInvoke;
import com.baozun.nebula.dao.i18n.I18nLangDao;
import com.baozun.nebula.model.i18n.I18nLang;
import com.baozun.nebula.sdk.manager.SdkI18nLangManager;
import com.baozun.nebula.utils.Validator;

/**
 * I18nLangManager
 * @author  何波
 *
 */
@Transactional
@Service("sdkI18nLangManager") 
public class SdkI18nLangManagerImpl implements SdkI18nLangManager {

	@Autowired
	private I18nLangDao i18nLangDao;
	
	@Autowired(required=false)
	private ZKWatchPath zkWatchPath;

	private List<I18nLang> i18nLangCache = null;
	
	private String defaultLang = null;
	
	@Autowired
	private ZkOperator zkOperator;
	
	/**
	 * 保存I18nLang
	 * 
	 */
	public I18nLang saveI18nLang(I18nLang model){
		Long id = model.getId();
		Integer dl = model.getDefaultlang();
		if(id != null){
			I18nLang db = findI18nLangById(id);
			db.setTokenizer(model.getTokenizer());
			db.setValue(model.getValue());
			db.setDefaultlang(dl);
			if(Validator.isNullOrEmpty(model.getSort())){
				int sort = i18nLangDao.selectSort()+1;
				db.setSort(sort);
			}else{
				db.setSort(model.getSort());
			}
			model = i18nLangDao.save(db);
		}else{
			if(Validator.isNullOrEmpty(model.getSort())){
				int sort = i18nLangDao.selectSort()+1;
				model.setSort(sort);
			}
			
			model.setLifecycle(1);
			model = i18nLangDao.save(model);
		}
		if(dl == 1){
			id = model.getId();
			i18nLangDao.updateDefaultLang(id);
		}
		i18nLangCache = null;
		defaultLang = null;
		zkOperator.noticeZkServer(zkWatchPath.getZKWatchPath(I18nLangWatchInvoke.class));
		return model;
	}
	
	/**
	 * 通过id获取I18nLang
	 * 
	 */
	public I18nLang findI18nLangById(Long id){
	
		return i18nLangDao.getByPrimaryKey(id);
	}

	/**
	 * 获取所有I18nLang列表
	 * @return
	 */
	public List<I18nLang> findAllI18nLangList(){
	
		return i18nLangDao.findAllI18nLangList();
	};
	
	/**
	 * 通过ids获取I18nLang列表
	 * @param ids
	 * @return
	 */
	public List<I18nLang> findI18nLangListByIds(List<Long> ids){
	
		return i18nLangDao.findI18nLangListByIds(ids);
	};
	
	/**
	 * 通过参数map获取I18nLang列表
	 * @param paraMap
	 * @return
	 */
	public List<I18nLang> findI18nLangListByQueryMap(Map<String, Object> paraMap){
	
		return i18nLangDao.findI18nLangListByQueryMap(paraMap);
	};
	
	/**
	 * 分页获取I18nLang列表
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	public Pagination<I18nLang> findI18nLangListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap){
	
		return i18nLangDao.findI18nLangListByQueryMapWithPage(page,sorts,paraMap);
	}

	@Override
	@Transactional(readOnly = true)
	public List<I18nLang> geti18nLangCache() {
		if (i18nLangCache == null) {
			i18nLangCache = findAllI18nLangList();
		}
		return i18nLangCache;
	}

	@Override
	public int updateLifecycle(List<Long> ids, int lifecycle) {
		i18nLangCache = null;
		zkOperator.noticeZkServer(zkWatchPath.getZKWatchPath(I18nLangWatchInvoke.class));
		return i18nLangDao.updateLifecycle(ids, lifecycle);
	}

	@Override
	public void loadI18nLangs() {
		//重新加载i18n配置
		i18nLangCache = findAllI18nLangList();
		//重置defaultLang
		defaultLang = null;
	}
	
	@Override
	public String getDefaultlang() {
		if(Validator.isNullOrEmpty(defaultLang)) {
			List<I18nLang> i18nLangList = geti18nLangCache();
			if (Validator.isNotNullOrEmpty(i18nLangList)) {
				for (I18nLang i18nLang : i18nLangList) {
					Integer defaultlang = i18nLang.getDefaultlang();
					if (defaultlang != null && defaultlang == 1) {
						defaultLang = i18nLang.getKey();
						break;
					}
				}
			}
		}
		
		return defaultLang;
	}
	
	
	
	@Override
	public String getCurrentLangValue(String lang) {
		List<I18nLang> i18nLangList = geti18nLangCache();
		if (Validator.isNotNullOrEmpty(i18nLangList)) {
			for (I18nLang i18nLang : i18nLangList) {
				String key = i18nLang.getKey();
				if (lang != null && lang.equals(key)) {
					return i18nLang.getValue();
				}
			}
		}
		return null;
	}
	
	@Override
	public boolean isExistLang(String lang) {
		List<I18nLang> i18nLangList = geti18nLangCache();
		if (Validator.isNotNullOrEmpty(i18nLangList)) {
			for (I18nLang i18nLang : i18nLangList) {
				String key = i18nLang.getKey();
				if (lang != null && lang.equals(key)) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	@Transactional(readOnly = true)
	public List<String> getAllEnabledI18nKeyList() {
		List<I18nLang> i18nLangList = geti18nLangCache();
		List<String> i18nKeyList = new ArrayList<String>();
		for(I18nLang i18nLang : i18nLangList){
			i18nKeyList.add(i18nLang.getKey());
		}
		
		return i18nKeyList;
	}
}
