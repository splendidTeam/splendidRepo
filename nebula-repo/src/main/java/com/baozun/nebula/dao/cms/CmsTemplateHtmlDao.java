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

import com.baozun.nebula.model.cms.CmsPublished;
import com.baozun.nebula.model.cms.CmsTemplateHtml;

/**
 * CmsTemplateHtmlDao
 * @author  谢楠
 *
 */
public interface CmsTemplateHtmlDao extends GenericEntityDao<CmsTemplateHtml,Long>{

	/**
	 * 通过模块编号和版本id获取发布的html模板对象
	 * @param moduleCode
	 * @param versionId
	 * @return
	 */
	@NativeQuery(model = CmsTemplateHtml.class)
	public CmsTemplateHtml findCmsTemplateHtmlByModuleCodeAndVersionId(@QueryParam("moduleCode")String moduleCode, @QueryParam("versionId")Long versionId);
	
	/**
	 * 
	 * @param paraMap
	 * @return
	 */
	@NativeQuery(model = CmsTemplateHtml.class)
	public List<CmsTemplateHtml> findCmsTemplateHtmlListByQueryMap(@QueryParam Map<String, Object> paraMap);

	@NativeUpdate
	public void removeCmsTemplateHtml(@QueryParam("id")Long id);
	
	@NativeUpdate
	public void removeCmsTemplateHtmlByModuleCode(@QueryParam("moduleCode")String moduleCode);
	
	@NativeUpdate
	public void removeCmsTemplateHtmlByPageCode(@QueryParam("pageCode")String pageCode);

	@NativeQuery(model = CmsTemplateHtml.class)
	public CmsTemplateHtml findCmsTemplateHtmlByPageCodeAndVersionId(@QueryParam("pageCode")String pageCode, @QueryParam("versionId")Long versionId);
	
	
}
