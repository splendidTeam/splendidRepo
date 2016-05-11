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
package com.baozun.nebula.sdk.manager;

import java.util.List;
import java.util.Map;

import loxia.dao.Pagination;
import loxia.dao.Sort;
import loxia.dao.Page;

import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.cms.CmsPageTemplate;

/**
 * CmsPageTemplateManager
 * @author  Justin
 *
 */
public interface SdkCmsPageTemplateManager extends BaseManager{

	/**
	 * 保存CmsPageTemplate
	 * 
	 */
	CmsPageTemplate saveCmsPageTemplate(CmsPageTemplate model);
	
	/**
	 * 通过id获取CmsPageTemplate
	 * 
	 */
	CmsPageTemplate findCmsPageTemplateById(Long id);

	/**
	 * 获取所有CmsPageTemplate列表
	 * @return
	 */
	List<CmsPageTemplate> findAllCmsPageTemplateList();
	
	/**
	 * 通过ids获取CmsPageTemplate列表
	 * @param ids
	 * @return
	 */
	List<CmsPageTemplate> findCmsPageTemplateListByIds(List<Long> ids);
	
	/**
	 * 通过参数map获取CmsPageTemplate列表
	 * @param paraMap
	 * @return
	 */
	List<CmsPageTemplate> findCmsPageTemplateListByQueryMap(Map<String, Object> paraMap);
	
	/**
	 * 分页获取CmsPageTemplate列表
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	Pagination<CmsPageTemplate> findCmsPageTemplateListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap);
	
	
	
	/**
	 * 通过ids批量启用或禁用CmsPageTemplate
	 * 设置lifecycle =0 或 1
	 * @param ids
	 * @return
	 */
	void enableOrDisableCmsPageTemplateByIds(List<Long> ids,Integer state);
	
	/**
	 * 通过ids批量删除CmsPageTemplate
	 * 设置lifecycle =2
	 * @param ids
	 * @return
	 */
	void removeCmsPageTemplateByIds(List<Long> ids);
	
	/**
	 * 获取有效的CmsPageTemplate列表
	 * lifecycle =1
	 * @param ids
	 * @return
	 */
	List<CmsPageTemplate> findAllEffectCmsPageTemplateList();
	
	/**
	 * 通过参数map获取有效的CmsPageTemplate列表
	 * 强制加上lifecycle =1
	 * @param paraMap
	 * @return
	 */
	List<CmsPageTemplate> findEffectCmsPageTemplateListByQueryMap(Map<String, Object> paraMap);
	
	/**
	 * 分页获取有效的CmsPageTemplate列表
	 * 强制加上lifecycle =1
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	
	Pagination<CmsPageTemplate> findEffectCmsPageTemplateListByQueryMapWithPage(Page page,Sort[] sorts,Map<String, Object> paraMap);
	
	
	/**
	 * 将上传的模板文件
	 * 所有静态资源加上staticbase标识,前面加上#{staticbase}
	 * 所有相对路径的url的链接加上pagebase标识,前面加上#{pagebase}
	 * @param html
	 * @return
	 */
	String addTemplateBase(String html);
	
	/**
	 * 根据配置文件将#{staticbase},#{pagebase}转换成真实的路径
	 *(方法移至SdkCmsCommonManager)
	 * @param html
	 * @return
	 */
	@Deprecated
	String processTemplateBase(String html);

	
	
}
