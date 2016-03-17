/**
 * Copyright (c) 2013 Baozun All Rights Reserved.
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
 */
package com.baozun.nebula.manager.cms.pageinstance;

import java.util.Date;
import java.util.List;
import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.cms.CmsPageInstance;

/**
 * 模板页面管理manager
 * 
 * @author chenguang.zhou
 * @date 2014年7月2日 上午9:45:43
 */
public interface CmsPageInstanceManager extends BaseManager {

	/**
	 * 通过id查询页面实例
	 * 
	 * @param id
	 * @return
	 */
	public CmsPageInstance findCmsPageInstanceById(Long id);

	/**
	 * 分页查询摸板页面实例
	 * 
	 * @param page
	 * @param sorts
	 * @param paraMap
	 * @return
	 */
	public Pagination<CmsPageInstance> findCmsPageInstanceListByParaMapWithPage(Page page, Sort[] sorts,
			Map<String, Object> paraMap);

	/**
	 * 保存页面实例
	 * 
	 * @param cmsPageInstance
	 * @param html
	 * @return
	 */
	public CmsPageInstance createOrUpdateCmsPageInstance(CmsPageInstance cmsPageInstance, String html);

	/**
	 * 通过ids删除页面实例
	 * 
	 * @param ids
	 */
	public void removeCmsPageInstanceByIds(List<Long> ids);

	/**
	 * 查询模板与可编辑区域的数据
	 * 
	 * @param templateId
	 * @param pageId
	 * @param isEdit
	 * @return
	 */
	public String findUpdatedCmsPageInstance(Long templateId, Long pageId, Boolean isEdit);

	/**
	 * 发布页面实例
	 * 
	 * @param pageId
	 */
	public void publishPageInstance(Long pageId,Date startTime,Date endTime);

	/**
	 * 检察页面实例编码是否存在
	 * 
	 * @param code
	 * @param pageId
	 * @return
	 */
	public CmsPageInstance checkPageInstanceCode(String code, Long pageId);

	/**
	 * 检察页面实例url是否存在
	 * 
	 * @param url
	 * @param pageId
	 * @return
	 */
	public CmsPageInstance checkPageInstanceUrl(String url, Long pageId,int type);

	/**
	 * 取消发布页面实例
	 * 
	 * @param pageId
	 */
	public void cancelPublishedPageInstance(Long pageId);

}
