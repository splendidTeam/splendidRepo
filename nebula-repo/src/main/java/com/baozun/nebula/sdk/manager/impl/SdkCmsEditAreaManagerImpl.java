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

import java.util.List;
import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.dao.cms.CmsEditAreaDao;
import com.baozun.nebula.model.cms.CmsEditArea;
import com.baozun.nebula.sdk.manager.SdkCmsEditAreaManager;

/**
 * CmsEditAreaManager
 * @author  Justin
 *
 */
@Transactional
@Service("sdkCmsEditAreaManager") 
public class SdkCmsEditAreaManagerImpl implements SdkCmsEditAreaManager {

	@Autowired
	private CmsEditAreaDao cmsEditAreaDao;


	/**
	 * 保存CmsEditArea
	 * 
	 */
	public CmsEditArea saveCmsEditArea(CmsEditArea model){
	
		return cmsEditAreaDao.save(model);
	}
	
	/**
	 * 通过id获取CmsEditArea
	 * 
	 */
	@Transactional(readOnly=true)
	public CmsEditArea findCmsEditAreaById(Long id){
	
		return cmsEditAreaDao.getByPrimaryKey(id);
	}

	/**
	 * 获取所有CmsEditArea列表
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<CmsEditArea> findAllCmsEditAreaList(){
	
		return cmsEditAreaDao.findAllCmsEditAreaList();
	};
	
	/**
	 * 通过ids获取CmsEditArea列表
	 * @param ids
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<CmsEditArea> findCmsEditAreaListByIds(List<Long> ids){
	
		return cmsEditAreaDao.findCmsEditAreaListByIds(ids);
	};
	
	/**
	 * 通过参数map获取CmsEditArea列表
	 * @param paraMap
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<CmsEditArea> findCmsEditAreaListByQueryMap(Map<String, Object> paraMap){
	
		return cmsEditAreaDao.findCmsEditAreaListByQueryMap(paraMap);
	};
	
	/**
	 * 分页获取CmsEditArea列表
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	@Transactional(readOnly=true)
	public Pagination<CmsEditArea> findCmsEditAreaListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap){
	
		return cmsEditAreaDao.findCmsEditAreaListByQueryMapWithPage(page,sorts,paraMap);
	};
	
	
	
	/**
	 * 通过ids批量启用或禁用CmsEditArea
	 * 设置lifecycle =0 或 1
	 * @param ids
	 * @return
	 */
	public void enableOrDisableCmsEditAreaByIds(List<Long> ids,Integer state){
		cmsEditAreaDao.enableOrDisableCmsEditAreaByIds(ids,state);
	}
	
	/**
	 * 通过ids批量删除CmsEditArea
	 * 设置lifecycle =2
	 * @param ids
	 * @return
	 */
	public void removeCmsEditAreaByIds(List<Long> ids){
		cmsEditAreaDao.removeCmsEditAreaByIds(ids);
	}
	
	
	/**
	 * 获取有效的CmsEditArea列表
	 * lifecycle =1
	 * @param ids
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<CmsEditArea> findAllEffectCmsEditAreaList(){
	
		return cmsEditAreaDao.findAllEffectCmsEditAreaList();
	};
	
	/**
	 * 通过参数map获取有效的CmsEditArea列表
	 * 强制加上lifecycle =1
	 * @param paraMap
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<CmsEditArea> findEffectCmsEditAreaListByQueryMap(Map<String, Object> paraMap){
	
		return cmsEditAreaDao.findEffectCmsEditAreaListByQueryMap(paraMap);
	};
	
	/**
	 * 分页获取有效的CmsEditArea列表
	 * 强制加上lifecycle =1
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	@Transactional(readOnly=true)
	public Pagination<CmsEditArea> findEffectCmsEditAreaListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap){
	
		return cmsEditAreaDao.findEffectCmsEditAreaListByQueryMapWithPage(page,sorts,paraMap);
	}

	@Override
	public void removeCmsEditAreaByPageId(Long pageId) {
		cmsEditAreaDao.removeCmsEditAreaByPageId(pageId);

	}

	@Override
	public void removeCmsEditAreaByTemplateId(Long templateId,String code) {
		cmsEditAreaDao.removeCmsEditAreaByTemplateId(templateId,code);
		
	}

	@Override
	public void removeCmsModuleEditAreaByTemplateId(Long templateId,String code) {
		cmsEditAreaDao.removeCmsModuleEditAreaByTemplateId(templateId,code);
	}

	@Override
	public void editAreaHide(Map<String, Object> paraMap) {
		cmsEditAreaDao.editAreaHide(paraMap);
	}

	@Override
	@Transactional(readOnly=true)
	public CmsEditArea queryEditAreaHide(Map<String, Object> paraMap) {
		return cmsEditAreaDao.queryEditAreaHide(paraMap);
	}

	
}
