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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.dao.system.WarningConfigDao;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.model.system.WarningConfig;
import com.baozun.nebula.sdk.manager.SdkWarningConfigManager;

/**
 * WarningConfigManager
 * 
 * @author 何波
 * 
 */
@Transactional
@Service("sdkWarningConfigManager")
public class SdkWarningConfigManagerImpl implements SdkWarningConfigManager {

	@Autowired
	private WarningConfigDao warningConfigDao;

	/**
	 * 保存WarningConfig
	 * 
	 */
	public WarningConfig saveWarningConfig(WarningConfig model) {
		Long id = model.getId();
		if (id != null) {
			WarningConfig dbModel = warningConfigDao.getByPrimaryKey(id);
			dbModel.setCode(model.getCode());
			dbModel.setCount(model.getCount());
			dbModel.setLevel(model.getLevel());
			dbModel.setName(model.getName());
			dbModel.setTemplateCode(model.getTemplateCode());
			dbModel.setReceivers(model.getReceivers());
			dbModel.setTimeParam(model.getTimeParam());
			dbModel.setType(model.getType());
			dbModel.setWarningDesc(model.getWarningDesc());
			return warningConfigDao.save(dbModel);
		} else {
			model.setLifecycle(1);
			String code = model.getCode();
			Map<String, Object> paraMap = new HashMap<String, Object>();
			paraMap.put("code", code);
			if (findWarningConfigListByQueryMap(paraMap) != null &&findWarningConfigListByQueryMap(paraMap).size()>0) {
	          throw new BusinessException("编码已经存在");
			}
			return warningConfigDao.save(model);
		}
		
	}

	/**
	 * 通过id获取WarningConfig
	 * 
	 */
	@Override
	@Transactional(readOnly=true)
	public WarningConfig findWarningConfigById(Long id) {
		WarningConfig wc = warningConfigDao.getByPrimaryKey(id);
		if(wc.getLifecycle()==3){
			return null;
		}
		return wc;
	}

	/**
	 * 获取所有WarningConfig列表
	 * 
	 * @return
	 */
	@Override
	@Transactional(readOnly=true)
	public List<WarningConfig> findAllWarningConfigList() {

		return warningConfigDao.findAllWarningConfigList();
	};

	/**
	 * 通过ids获取WarningConfig列表
	 * 
	 * @param ids
	 * @return
	 */
	@Override
	@Transactional(readOnly=true)
	public List<WarningConfig> findWarningConfigListByIds(List<Long> ids) {

		return warningConfigDao.findWarningConfigListByIds(ids);
	};

	/**
	 * 通过参数map获取WarningConfig列表
	 * 
	 * @param paraMap
	 * @return
	 */
	@Override
	@Transactional(readOnly=true)
	public List<WarningConfig> findWarningConfigListByQueryMap(
			Map<String, Object> paraMap) {

		return warningConfigDao.findWarningConfigListByQueryMap(paraMap);
	};

	/**
	 * 分页获取WarningConfig列表
	 * 
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts
	 * @return
	 */
	@Override
	@Transactional(readOnly=true)
	public Pagination<WarningConfig> findWarningConfigListByQueryMapWithPage(
			Page page, Sort[] sorts, Map<String, Object> paraMap) {

		return warningConfigDao.findWarningConfigListByQueryMapWithPage(page,
				sorts, paraMap);
	};

	/**
	 * 通过ids批量启用或禁用WarningConfig 设置lifecycle =0 或 1
	 * 
	 * @param ids
	 * @return
	 */
	public void enableOrDisableWarningConfigByIds(List<Long> ids, Integer state) {
		warningConfigDao.enableOrDisableWarningConfigByIds(ids, state);
	}

	/**
	 * 通过ids批量删除WarningConfig 设置lifecycle =2
	 * 
	 * @param ids
	 * @return
	 */
	public void removeWarningConfigByIds(List<Long> ids) {
		warningConfigDao.removeWarningConfigByIds(ids);
	}

	/**
	 * 获取有效的WarningConfig列表 lifecycle =1
	 * 
	 * @param ids
	 * @return
	 */
	@Override
	@Transactional(readOnly=true)
	public List<WarningConfig> findAllEffectWarningConfigList() {

		return warningConfigDao.findAllEffectWarningConfigList();
	};

	/**
	 * 通过参数map获取有效的WarningConfig列表 强制加上lifecycle =1
	 * 
	 * @param paraMap
	 * @return
	 */
	@Override
	@Transactional(readOnly=true)
	public List<WarningConfig> findEffectWarningConfigListByQueryMap(
			Map<String, Object> paraMap) {

		return warningConfigDao.findEffectWarningConfigListByQueryMap(paraMap);
	};

	/**
	 * 分页获取有效的WarningConfig列表 强制加上lifecycle =1
	 * 
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts
	 * @return
	 */
	@Override
	@Transactional(readOnly=true)
	public Pagination<WarningConfig> findEffectWarningConfigListByQueryMapWithPage(
			Page page, Sort[] sorts, Map<String, Object> paraMap) {

		return warningConfigDao.findEffectWarningConfigListByQueryMapWithPage(
				page, sorts, paraMap);
	}

	@Override
	@Transactional(readOnly=true)
	public WarningConfig findWarningConfigByCode(String code) {
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("code", code);
		List<WarningConfig> wcs = warningConfigDao.findEffectWarningConfigListByQueryMap(paraMap);
		if(wcs==null || wcs.size()==0){
			return null;
		}
		return wcs.get(0);
	}

}
