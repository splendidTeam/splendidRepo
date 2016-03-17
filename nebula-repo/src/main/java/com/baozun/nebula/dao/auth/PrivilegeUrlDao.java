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
package com.baozun.nebula.dao.auth;

import java.util.List;
import java.util.Map;

import loxia.annotation.NativeQuery;
import loxia.annotation.NativeUpdate;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;
import loxia.dao.Pagination;
import loxia.dao.Sort;
import loxia.dao.Page;
import com.baozun.nebula.model.auth.PrivilegeUrl;

/**
 * PrivilegeUrlDao
 * @author  何波
 *
 */
public interface PrivilegeUrlDao extends GenericEntityDao<PrivilegeUrl,Long>{

	/**
	 * 获取所有PrivilegeUrl列表
	 * @return
	 */
	@NativeQuery(model = PrivilegeUrl.class)
	List<PrivilegeUrl> findAllPrivilegeUrlList();
	
	/**
	 * 通过ids获取PrivilegeUrl列表
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = PrivilegeUrl.class)
	List<PrivilegeUrl> findPrivilegeUrlListByIds(@QueryParam("ids")List<Long> ids);
	
	/**
	 * 通过参数map获取PrivilegeUrl列表
	 * @param paraMap
	 * @return
	 */
	@NativeQuery(model = PrivilegeUrl.class)
	List<PrivilegeUrl> findPrivilegeUrlListByQueryMap(@QueryParam Map<String, Object> paraMap);
	
	/**
	 * 分页获取PrivilegeUrl列表
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	@NativeQuery(model = PrivilegeUrl.class)
	Pagination<PrivilegeUrl> findPrivilegeUrlListByQueryMapWithPage(Page page,Sort[] sorts,@QueryParam Map<String, Object> paraMap);
	
	/**
	 * 通过ids批量删除PrivilegeUrl
	 * @param ids
	 * @return
	 */
	@NativeUpdate
	void removePrivilegeUrlByIds(@QueryParam("ids")List<Long> ids);
	@NativeUpdate
	void removePrivilegeUrlByParentId(@QueryParam("parentId") Long parentId);
	
	@NativeUpdate
	void removePrivilegeUrlBypIds(@QueryParam("pIds")List<Long> pIds);
	
}
