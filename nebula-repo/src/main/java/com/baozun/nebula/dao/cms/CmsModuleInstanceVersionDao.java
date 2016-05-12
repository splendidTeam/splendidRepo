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

import java.util.Date;
import java.util.List;
import java.util.Map;

import loxia.annotation.NativeQuery;
import loxia.annotation.NativeUpdate;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;
import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.command.cms.CmsModuleInstanceVersionCommand;
import com.baozun.nebula.model.cms.CmsModuleInstanceVersion;

/**
 * CmsModuleInstanceVersionDao
 * @author  xienan
 *
 */
public interface CmsModuleInstanceVersionDao extends GenericEntityDao<CmsModuleInstanceVersion,Long>{
	/**
	 * 获取所有CmsModuleInstanceVersion列表
	 * @return
	 */
	@NativeQuery(model = CmsModuleInstanceVersion.class)
	List<CmsModuleInstanceVersion> findAllCmsModuleInstanceVersionList();
	
	/**
	 * 通过ids获取CmsModuleInstanceVersion列表
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = CmsModuleInstanceVersion.class)
	List<CmsModuleInstanceVersion> findCmsModuleInstanceVersionListByIds(@QueryParam("ids")List<Long> ids);
	
	/**
	 * 通过参数map获取CmsModuleInstanceVersion列表
	 * @param paraMap
	 * @return
	 */
	@NativeQuery(model = CmsModuleInstanceVersion.class)
	List<CmsModuleInstanceVersion> findCmsModuleInstanceVersionListByQueryMap(@QueryParam Map<String, Object> paraMap);
	/**
	 * 分页获取CmsModuleInstanceVersion列表
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts 
	 * @return
	 */
	@NativeQuery(model = CmsModuleInstanceVersion.class)
	public Pagination<CmsModuleInstanceVersion> findCmsModuleInstanceVersionListByQueryMapWithPage(Page page,Sort[] sorts,@QueryParam Map<String, Object> paraMap);

	@NativeQuery(model = CmsModuleInstanceVersionCommand.class)
	public List<CmsModuleInstanceVersionCommand> findPublishNotOverModuleVersion();

	@NativeQuery(model = CmsModuleInstanceVersion.class)
	public CmsModuleInstanceVersion getPublishingModuleVersion(@QueryParam("moduleId")Long id);

	@NativeQuery(model = CmsModuleInstanceVersion.class)
	public List<CmsModuleInstanceVersion> findModuleVersionInTimeQuantum(@QueryParam("moduleId")Long moduleId, @QueryParam("versionId")Long versionId, @QueryParam("startTime")Date startTime, @QueryParam("endTime")Date endTime);

	@NativeQuery(model = CmsModuleInstanceVersion.class)
	public CmsModuleInstanceVersion findPublishModuleVersion(@QueryParam("moduleId")Long moduleid, @QueryParam("mtime")Date mtime);

	@NativeUpdate
	public void removeCmsModuleInstanceVersionByIds(@QueryParam("ids")List<Long> ids);

	@NativeUpdate
	public void removeModuleVersionByModuleIds(@QueryParam("ids")List<Long> moduleIds);

	@NativeUpdate
	public void cancelInstanceVersionInModuleId(@QueryParam("moduleId")Long moduleId);

	
}
