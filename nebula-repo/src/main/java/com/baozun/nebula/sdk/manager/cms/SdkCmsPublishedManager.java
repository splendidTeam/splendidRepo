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
package com.baozun.nebula.sdk.manager.cms;

import java.util.List;
import java.util.Map;

import loxia.dao.Pagination;
import loxia.dao.Sort;
import loxia.dao.Page;

import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.cms.CmsPublished;

/**
 * CmsPublishedManager
 * @author  Justin
 *
 */
public interface SdkCmsPublishedManager extends BaseManager{

	/**
	 * 保存CmsPublished
	 * 
	 */
	CmsPublished saveCmsPublished(CmsPublished model);
	
	/**
	 * 通过id获取CmsPublished
	 * 
	 */
	CmsPublished findCmsPublishedById(Long id);

	/**
	 * 获取所有CmsPublished列表
	 * @return
	 */
	List<CmsPublished> findAllCmsPublishedList();
	
	/**
	 * 通过ids获取CmsPublished列表
	 * @param ids
	 * @return
	 */
	List<CmsPublished> findCmsPublishedListByIds(List<Long> ids);
	
	/**
	 * 通过参数map获取CmsPublished列表
	 * @param paraMap
	 * @return
	 */
	List<CmsPublished> findCmsPublishedListByQueryMap(Map<String, Object> paraMap);
	
	/**
	 * 分页获取CmsPublished列表
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	Pagination<CmsPublished> findCmsPublishedListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap);
	
	/**
	 * 通过页面编码删除发布的页面实例
	 * @param pageCode
	 */
	void removeCmsPubulishedByPageCode(String pageCode);
	
	
	/**
	 * 通过模块编码删除发布的页面实例
	 * @param pageCode
	 */
	void removeCmsPubulishedByModuleCode(String moduleCode);


	/**
	 * 通过页面编码删除发布的页面实例
	 * @param pageCodes
	 */
	void removeCmsPubulishedByPageCodes(List<String> pageCodes);
	
}
