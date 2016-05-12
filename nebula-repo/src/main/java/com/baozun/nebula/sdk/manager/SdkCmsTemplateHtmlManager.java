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

import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.cms.CmsTemplateHtml;

/**
 * SdkCmsTemplateHtmlManager
 * @author  谢楠
 *
 */
public interface SdkCmsTemplateHtmlManager extends BaseManager{
	/**
	 * 保存saveCmsTemplateHtml
	 * @param model
	 * @return
	 */
	public CmsTemplateHtml saveCmsTemplateHtml(CmsTemplateHtml model);

	/**
	 * 根据id来获取CmsTemplateHtml
	 * @param id
	 * @return
	 */
	public CmsTemplateHtml findCmsTemplateHtmlById(Long id);

	public CmsTemplateHtml findCmsTemplateHtmlByModuleCodeAndVersionId(String code, Long versionId);

	public List<CmsTemplateHtml> findCmsTemplateHtmlListByQueryMap(Map<String, Object> paraMap);

	public void removeCmsTemplateHtml(Long id);
	
	public void removeCmsTemplateHtmlByModuleCode(String moduleCode);
	
	public void removeCmsTemplateHtmlByPageCode(String pageCode);

	public CmsTemplateHtml findCmsTemplateHtmlByPageCodeAndVersionId(String code,
			Long versionId);
}
