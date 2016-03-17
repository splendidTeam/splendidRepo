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

import com.baozun.nebula.model.cms.CmsPublished;

/**
 * CmsPublishedDao
 * @author  Justin
 *
 */
public interface CmsPublishedDao extends GenericEntityDao<CmsPublished,Long>{

	/**
	 * 获取所有CmsPublished列表
	 * @return
	 */
	@NativeQuery(model = CmsPublished.class)
	List<CmsPublished> findAllCmsPublishedList();
	
	/**
	 * 通过ids获取CmsPublished列表
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = CmsPublished.class)
	List<CmsPublished> findCmsPublishedListByIds(@QueryParam("ids")List<Long> ids);
	
	/**
	 * 通过参数map获取CmsPublished列表
	 * @param paraMap
	 * @return
	 */
	@NativeQuery(model = CmsPublished.class)
	List<CmsPublished> findCmsPublishedListByQueryMap(@QueryParam Map<String, Object> paraMap);
	
	/**
	 * 分页获取CmsPublished列表
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	@NativeQuery(model = CmsPublished.class)
	Pagination<CmsPublished> findCmsPublishedListByQueryMapWithPage(Page page,Sort[] sorts,@QueryParam Map<String, Object> paraMap);
	
	/**
	 * 通过页面编码删除发布的页面实例
	 * @param pageCode
	 */
	@NativeUpdate
	void removeCmsPubulishedByPageCode(@QueryParam("pageCode") String pageCode);

	/**
	 * 通过页面编码集合删除发布的页面实例
	 * @param pageCode
	 */
	@NativeUpdate
	void removeCmsPubulishedByPageCodes(@QueryParam("pageCodes") List<String> pageCodes);
	
	@NativeUpdate
	void removeCmsPubulishedByModuleCode(@QueryParam("moduleCode") String moduleCode);
}
