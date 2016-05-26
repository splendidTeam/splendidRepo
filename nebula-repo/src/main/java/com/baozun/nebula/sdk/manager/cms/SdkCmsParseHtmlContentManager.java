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

import java.util.List;
import java.util.Map;

import com.baozun.nebula.manager.BaseManager;

/**
 * cms解析html内容
 * SdkCmsParseHtmlContentManager
 * @author  谢楠
 *
 */
public interface SdkCmsParseHtmlContentManager extends BaseManager{

	/**
	 * 获取页面模板的数据
	 * @param templateId 模板id
	 * @param pageId 页面id
	 * @param versionId 版本id（如果是基础页不是版本页，该值为null）
	 * @param isEdit 是否是预览还是编辑（true：编辑，  false:预览）
	 * @return
	 */
	public String getTemplatePageData(Long templateId, Long pageId, Long versionId, Boolean isEdit);

	/**
	 * 获取发布页面的内容
	 * @param templateId 模板id
	 * @param pageId 页面id
	 * @param versionId 版本id
	 * @return
	 */
	public String getParsePageData(Long templateId, Long pageId, Long versionId);

	/**
	 * 获取发布模块的内容
	 * @param editAreaList
	 * @param templateId
	 * @return
	 */
	public <T> String getParseModuleData(List<T> editAreaList, Long templateId);

	/**
	 * 将编辑的页面html内容拆分成各个编辑单元
	 * @param html
	 * @param pageType
	 * @return
	 */
	public Map<String, String> processPageHtml(String html, int pageType);
	
	/**
	 * 去掉不要加载的数据, 如:不要加载的js 去掉<!--noedit-start-->到<!--noedit-end-->中间的数据
	 * @param data 编辑数据
	 * @return
	 */
	public String processNoEditData(String data);

	/**
	 * 去掉编辑时添加的内容, 如:不要加载的js 去掉<!--onlyedit-start-->到<!--onlyedit-start-->中间的数据
	 * @param data 编辑数据
	 * @return
	 */
	public String processOnlyEditData(String data);

	/**
	 * 获取模块模板的数据
	 * @param templateId 模板id
	 * @param moduleId 模块id
	 * @param versionId 版本id（如果是基础页不是版本页，该值为null）
	 * @param isEdit 是否是预览还是编辑（true：编辑，  false:预览）
	 * @return
	 */
	public String getTemplateModuleData(Long templateId, Long moduleId, Long versionId, Boolean isEdit);

	/**
	 * 处理页面的html, 获取code对应的html
	 * @param html 页面内容
	 * @return
	 */
	public Map<String, String> processResetPageHtml(String html);

	/**
	 * 取出公共的资源 公共资源在<--resources-start-->标签之内的
	 * @param html cms页面内容
	 * @return
	 */
	public String findResource(String html);
	
}
