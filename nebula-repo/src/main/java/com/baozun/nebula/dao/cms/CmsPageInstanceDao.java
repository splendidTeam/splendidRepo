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
package com.baozun.nebula.dao.cms;

import java.util.List;
import java.util.Map;

import loxia.annotation.NativeQuery;
import loxia.annotation.NativeUpdate;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;
import loxia.dao.Pagination;
import loxia.dao.Sort;
import loxia.dao.Page;

import com.baozun.nebula.model.cms.CmsPageInstance;

/**
 * CmsPageInstanceDao
 * @author  Justin
 *
 */
public interface CmsPageInstanceDao extends GenericEntityDao<CmsPageInstance,Long>{

	/**
	 * 获取所有CmsPageInstance列表
	 * @return
	 */
	@NativeQuery(model = CmsPageInstance.class)
	List<CmsPageInstance> findAllCmsPageInstanceList();
	
	/**
	 * 通过ids获取CmsPageInstance列表
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = CmsPageInstance.class)
	List<CmsPageInstance> findCmsPageInstanceListByIds(@QueryParam("ids")List<Long> ids);
	
	/**
	 * 通过templateIds获取CmsPageInstance列表
	 * @param templateIds
	 * @return
	 */
	@NativeQuery(model = CmsPageInstance.class)
	List<CmsPageInstance> findCmsPageInstanceListByTemplateIds(@QueryParam("templateIds")List<Long> templateIds);

	
	/**
	 * 通过参数map获取CmsPageInstance列表
	 * @param paraMap
	 * @return
	 */
	@NativeQuery(model = CmsPageInstance.class)
	List<CmsPageInstance> findCmsPageInstanceListByQueryMap(@QueryParam Map<String, Object> paraMap);
	
	/**
	 * 分页获取CmsPageInstance列表
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	@NativeQuery(model = CmsPageInstance.class)
	Pagination<CmsPageInstance> findCmsPageInstanceListByQueryMapWithPage(Page page,Sort[] sorts,@QueryParam Map<String, Object> paraMap);
	
	
	
	/**
	 * 通过ids批量启用或禁用CmsPageInstance
	 * 设置lifecycle =0 或 1
	 * @param ids
	 * @return
	 */
	@NativeUpdate
	void enableOrDisableCmsPageInstanceByIds(@QueryParam("ids")List<Long> ids,@QueryParam("state")Integer state);
	
	/**
	 * 通过ids批量删除CmsPageInstance
	 * 设置lifecycle =2
	 * @param ids
	 * @return
	 */
	@NativeUpdate
	void removeCmsPageInstanceByIds(@QueryParam("ids")List<Long> ids);
	
	/**
	 * 获取有效的CmsPageInstance列表
	 * lifecycle =1
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = CmsPageInstance.class)
	List<CmsPageInstance> findAllEffectCmsPageInstanceList();
	
	/**
	 * 获取有效的CmsPageInstance列表
	 * lifecycle =1
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = CmsPageInstance.class)
	List<CmsPageInstance> findAllPublishedCmsPageInstanceList();

	
	/**
	 * 通过参数map获取有效的CmsPageInstance列表
	 * 强制加上lifecycle =1
	 * @param paraMap
	 * @return
	 */
	@NativeQuery(model = CmsPageInstance.class)
	List<CmsPageInstance> findEffectCmsPageInstanceListByQueryMap(@QueryParam Map<String, Object> paraMap);
	
	/**
	 * 分页获取有效的CmsPageInstance列表
	 * 强制加上lifecycle =1
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	@NativeQuery(model = CmsPageInstance.class)
	Pagination<CmsPageInstance> findEffectCmsPageInstanceListByQueryMapWithPage(Page page,Sort[] sorts,@QueryParam Map<String, Object> paraMap);
	
	/**
	 * 检察页面实例编码是否存在
	 * @param paraMap
	 * @return
	 */
	@NativeQuery(model = CmsPageInstance.class)
	CmsPageInstance checkPageInstanceCode(@QueryParam Map<String, Object> paraMap);
	
	/**
	 * 检察页面实例url是否存在
	 * @param paraMap
	 * @return
	 */
	@NativeQuery(model = CmsPageInstance.class)
	CmsPageInstance checkPageInstanceUrl(@QueryParam Map<String, Object> paraMap);
	
}
