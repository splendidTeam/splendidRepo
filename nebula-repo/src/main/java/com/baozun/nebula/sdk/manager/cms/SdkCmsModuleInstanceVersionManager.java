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

import java.util.Date;
import java.util.List;
import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.cms.CmsModuleInstanceVersion;
import com.baozun.nebula.model.cms.CmsPublished;

/**
 * SdkCmsModuleInstanceVersionManager
 * @author  xienan
 *
 */
public interface SdkCmsModuleInstanceVersionManager extends BaseManager{

	public Pagination<CmsModuleInstanceVersion> findCmsModuleInstanceVersionListByQueryMapWithPage(
			Page page, Sort[] sorts, Map<String, Object> paraMap);
	
	public List<CmsModuleInstanceVersion> findCmsModuleInstanceVersionListByIds(List<Long> ids);

//	public String findUpdatedCmsModuleInstanceVersion(Long templateId, Long moduleId,Long versionId, Boolean isEdit);

	public CmsModuleInstanceVersion findCmsModuleInstanceVersionById(Long versionId);

	/**
	 * 保存/更新模块版本
	 * @param cmsModuleInstanceVersion 模块版本对象
	 * @param html 页面信息
	 * @return
	 */
	public CmsModuleInstanceVersion createOrUpdateModuleInstanceVersion(CmsModuleInstanceVersion cmsModuleInstanceVersion, String html);

	//public void setPublishNotOverModuleVersionInMap();

	public CmsModuleInstanceVersion getPublishingModuleVersion(Long id);


	public void publishModuleInstanceVersion(Long versionId, Date startTime,
			Date endTime);

	/**
	 * 发布模块版本信息
	 */
	public List<CmsPublished> savePublishModuleInstanceVersion(Long versionId, Date startTime,
			Date endTime);

	/**
	 * 获取发布模板的版本
	 * @param id
	 * @return
	 */
	public CmsModuleInstanceVersion findPublishModuleVersion(Long id);

	public void cancelPublishedModuleInstanceVersion(Long versionId);

	//public void loadModuleVersionMap();

	/**
	 * 重新设置发布模块版本队列的缓存信息
	 */
	public void setPublicModuleVersionCacheInfo();

	/**
	 * 删除模块版本，并重新发布基础模块版本
	 * @param asList
	 */
	public void removeCmsModuleInstanceVersionPublishByIds(List<Long> asList);

	/**
	 * 只是单独删除模块版本
	 * @param versionIds
	 */
	public void removeModuleVersionByIds(List<Long> versionIds);
	
	/**
	 * 删除模块下载所有版本
	 * @param versionIds
	 */
	public void removeModuleVersionByModuleIds(List<Long> moduleIds);

	/**
	 * 取消模块下的所有实例版本的发布
	 * @param id
	 */
	public void cancelInstanceVersionInModuleId(Long id);

	public void copyModuleInstanceVersion(Long versionId, String name);

	public CmsModuleInstanceVersion saveCmsModuleInstanceVersion(CmsModuleInstanceVersion version);

}
