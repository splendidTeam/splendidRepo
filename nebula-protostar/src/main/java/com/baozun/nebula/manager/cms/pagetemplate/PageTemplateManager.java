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
package com.baozun.nebula.manager.cms.pagetemplate;

import com.baozun.nebula.model.cms.CmsPageTemplate;

/**
 * @author jumbo
 *
 */
public interface PageTemplateManager {
	
	/**
	 * 保存CmsPageTemplate
	 * 
	 */
	public CmsPageTemplate saveOrUodateCmsPageTemplate(CmsPageTemplate model,String customBaseUrl);
	
	
	/**
	 * 通过ID查询模板信息
	 * @param templateId
	 * @return
	 */
	public CmsPageTemplate findCmsPageTemplateById(Long templateId);
	/**
	 * 
	* @author 何波
	* @Description: 修改模板数据
	* @param model
	* @param customBaseUrl
	* @return   
	* CmsPageTemplate   
	* @throws
	 */
	public CmsPageTemplate editCmsPageTemplate(CmsPageTemplate model) ;
	

	/**
	 * 查询模板
	 * 
	 * @param templateId
	 * @param pageId
	 * @param isEdit
	 * @return
	 */
	public String findUpdatedCmsPageInstance(Long templateId);
	/**
	 * 
	* @author 何波
	* @Description: 还原模板对应code区域数据
	* @param templateId
	* @param code
	* @return   
	* String   
	* @throws
	 */
	String recoverTemplateCodeArea(Long templateId,String code, Long versionId);

}
