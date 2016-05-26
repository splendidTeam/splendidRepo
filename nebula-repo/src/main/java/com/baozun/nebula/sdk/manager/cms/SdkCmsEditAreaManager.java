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

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.cms.CmsEditArea;
import com.baozun.nebula.model.cms.CmsEditVersionArea;

/**
 * CmsEditAreaManager
 * @author  Justin
 *
 */
public interface SdkCmsEditAreaManager extends BaseManager{

	/**
	 * 保存CmsEditArea
	 * 
	 */
	CmsEditArea saveCmsEditArea(CmsEditArea model);
	
	/**
	 * 通过id获取CmsEditArea
	 * 
	 */
	CmsEditArea findCmsEditAreaById(Long id);

	/**
	 * 获取所有CmsEditArea列表
	 * @return
	 */
	List<CmsEditArea> findAllCmsEditAreaList();
	
	/**
	 * 通过ids获取CmsEditArea列表
	 * @param ids
	 * @return
	 */
	List<CmsEditArea> findCmsEditAreaListByIds(List<Long> ids);
	
	/**
	 * 通过参数map获取CmsEditArea列表
	 * @param paraMap
	 * @return
	 */
	List<CmsEditArea> findCmsEditAreaListByQueryMap(Map<String, Object> paraMap);
	
	/**
	 * 分页获取CmsEditArea列表
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	Pagination<CmsEditArea> findCmsEditAreaListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap);
	
	
	
	/**
	 * 通过ids批量启用或禁用CmsEditArea
	 * 设置lifecycle =0 或 1
	 * @param ids
	 * @return
	 */
	void enableOrDisableCmsEditAreaByIds(List<Long> ids,Integer state);
	
	/**
	 * 通过ids批量删除CmsEditArea
	 * 设置lifecycle =2
	 * @param ids
	 * @return
	 */
	void removeCmsEditAreaByIds(List<Long> ids);
	
	/**
	 * 获取有效的CmsEditArea列表
	 * lifecycle =1
	 * @param ids
	 * @return
	 */
	List<CmsEditArea> findAllEffectCmsEditAreaList();
	
	/**
	 * 通过参数map获取有效的CmsEditArea列表
	 * 强制加上lifecycle =1
	 * @param paraMap
	 * @return
	 */
	List<CmsEditArea> findEffectCmsEditAreaListByQueryMap(Map<String, Object> paraMap);
	
	/**
	 * 分页获取有效的CmsEditArea列表
	 * 强制加上lifecycle =1
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	
	Pagination<CmsEditArea> findEffectCmsEditAreaListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap);
	
	/**
	 * 通过pageId批量删除CmsEditArea
	 * 设置lifecycle =2
	 * @param pageId
	 */
	void removeCmsEditAreaByPageId(Long pageId);
	
	/**
	 * 通过templateId批量删除CmsEditArea
	 * 设置lifecycle =2
	 * @param templateId
	 */
	void removeCmsEditAreaByTemplateId( Long templateId,String code);
	/**
	 * 
	* @author 何波
	* @Description: 删除模块对应编辑数据
	* @param templateId   
	* void   
	* @throws
	 */
	void removeCmsModuleEditAreaByTemplateId(Long templateId,String code);

	void editAreaHide(Map<String, Object> paraMap);
	
	CmsEditArea queryEditAreaHide(Map<String, Object> paraMap);

}
