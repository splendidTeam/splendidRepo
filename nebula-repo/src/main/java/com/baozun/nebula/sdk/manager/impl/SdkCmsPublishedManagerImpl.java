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

import com.baozun.nebula.dao.cms.CmsPublishedDao;
import com.baozun.nebula.model.cms.CmsPublished;
import com.baozun.nebula.sdk.manager.SdkCmsPublishedManager;

/**
 * CmsPublishedManager
 * @author  Justin
 *
 */
@Transactional
@Service("sdkCmsPublishedManager") 
public class SdkCmsPublishedManagerImpl implements SdkCmsPublishedManager {

	@Autowired
	private CmsPublishedDao cmsPublishedDao;


	/**
	 * 保存CmsPublished
	 * 
	 */
	public CmsPublished saveCmsPublished(CmsPublished model){
	
		return cmsPublishedDao.save(model);
	}
	
	/**
	 * 通过id获取CmsPublished
	 * 
	 */
	@Transactional(readOnly=true)
	public CmsPublished findCmsPublishedById(Long id){
	
		return cmsPublishedDao.getByPrimaryKey(id);
	}

	/**
	 * 获取所有CmsPublished列表
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<CmsPublished> findAllCmsPublishedList(){
	
		return cmsPublishedDao.findAllCmsPublishedList();
	};
	
	/**
	 * 通过ids获取CmsPublished列表
	 * @param ids
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<CmsPublished> findCmsPublishedListByIds(List<Long> ids){
	
		return cmsPublishedDao.findCmsPublishedListByIds(ids);
	};
	
	/**
	 * 通过参数map获取CmsPublished列表
	 * @param paraMap
	 * @return
	 */
	@Transactional(readOnly=true)
	public List<CmsPublished> findCmsPublishedListByQueryMap(Map<String, Object> paraMap){
	
		return cmsPublishedDao.findCmsPublishedListByQueryMap(paraMap);
	};
	
	/**
	 * 分页获取CmsPublished列表
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	@Transactional(readOnly=true)
	public Pagination<CmsPublished> findCmsPublishedListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap){
	
		return cmsPublishedDao.findCmsPublishedListByQueryMapWithPage(page,sorts,paraMap);
	}

	@Override
	public void removeCmsPubulishedByPageCode(String pageCode) {
		cmsPublishedDao.removeCmsPubulishedByPageCode(pageCode);
	}

	@Override
	public void removeCmsPubulishedByPageCodes(List<String> pageCodes) {
		cmsPublishedDao.removeCmsPubulishedByPageCodes(pageCodes);
	}

	@Override
	public void removeCmsPubulishedByModuleCode(String moduleCode) {
		cmsPublishedDao.removeCmsPubulishedByModuleCode(moduleCode);
	}
	
}
