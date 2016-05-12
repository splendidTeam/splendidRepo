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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.ErrorCodes;
import com.baozun.nebula.manager.cms.pageinstance.CmsPageInstanceManager;
import com.baozun.nebula.model.BaseModel;
import com.baozun.nebula.model.cms.CmsEditArea;
import com.baozun.nebula.model.cms.CmsEditVersionArea;
import com.baozun.nebula.model.cms.CmsPageTemplate;
import com.baozun.nebula.sdk.manager.SdkCmsEditAreaManager;
import com.baozun.nebula.sdk.manager.SdkCmsEditVersionAreaManager;
import com.baozun.nebula.sdk.manager.SdkCmsPageTemplateManager;
import com.baozun.nebula.sdk.manager.impl.SdkCmsParseHtmlContentManagerImpl;
import com.baozun.nebula.utils.image.ImageOpeartion;
import com.feilong.core.Validator;

/**
 * @author jumbo
 *
 */
@Service
@Transactional
public class PageTemplateManagerImpl implements PageTemplateManager{

	@Autowired
	private SdkCmsPageTemplateManager sdkCmsPageTemplateManager;
	
	@Autowired
	private SdkCmsEditAreaManager		sdkCmsEditAreaManager;
	
	@Autowired
	private SdkCmsEditVersionAreaManager		sdkCmsEditVersionAreaManager;
	
	@Autowired
	private CmsPageInstanceManager cmsPageInstanceManager;

	/* (non-Javadoc)
	 * @see com.baozun.nebula.manager.cms.pagetemplate.PageTemplateManager#saveCmsPageTemplate(com.baozun.nebula.model.cms.CmsPageTemplate)
	 */
	@Override
	public CmsPageTemplate saveOrUodateCmsPageTemplate(CmsPageTemplate model,String customBaseUrl) {
		if(Validator.isNullOrEmpty(model.getUseCommonHeader())){
			model.setUseCommonHeader(false);
		}
		model.setImg(ImageOpeartion.imageUrlConvert(model.getImg(), customBaseUrl, true));
		if(Validator.isNotNullOrEmpty(model.getId())){//修改
			CmsPageTemplate oldPage = sdkCmsPageTemplateManager.findCmsPageTemplateById(model.getId());
			oldPage.setName(model.getName());
			oldPage.setImg(model.getImg());
			if(Validator.isNotNullOrEmpty(model.getData())){
				oldPage.setData(model.getData());
			}
			oldPage.setModifyTime(new Date());
			oldPage.setSupportType(model.getSupportType());
			oldPage.setUseCommonHeader(model.getUseCommonHeader());
			return oldPage;
		}else{
			//保存
			model.setCreateTime(new Date());
			model.setLifecycle(BaseModel.LIFECYCLE_ENABLE);
			model.setVersion(new Date());
			model = sdkCmsPageTemplateManager.saveCmsPageTemplate(model);
		}
		return model;
	}
	
	@Override
	public CmsPageTemplate editCmsPageTemplate(CmsPageTemplate model) {
		Long templateId = model.getId();
		if(Validator.isNotNullOrEmpty(templateId)){
			CmsPageTemplate oldPage = sdkCmsPageTemplateManager.findCmsPageTemplateById(model.getId());
			if(Validator.isNotNullOrEmpty(model.getData())){
				//<html>标签之前的内容会被js删掉，所以此处从old里边取出重新添加在新的html之前
				String newData = model.getData();
				String oldData = oldPage.getData();
				if(oldData != null && oldData.indexOf("<html") > newData.indexOf("<html")) {
					newData = oldData.substring(0, oldData.indexOf("<html")).concat(newData);
				}
				
				oldPage.setData(newData);
			}
			oldPage.setModifyTime(new Date());
			
			return oldPage;
		}else{
			throw new BusinessException("id is null");
		}
		
	}
	
	@Transactional(readOnly=true)
	@Override
	public CmsPageTemplate findCmsPageTemplateById(Long templateId) {
		CmsPageTemplate cmsPageTemplate = sdkCmsPageTemplateManager.findCmsPageTemplateById(templateId);
		
		return cmsPageTemplate;
	}
	@SuppressWarnings("deprecation")
	@Override
	public String findUpdatedCmsPageInstance(Long templateId) {
		CmsPageTemplate template = sdkCmsPageTemplateManager.findCmsPageTemplateById(templateId);
		String data = template.getData();
		data = sdkCmsPageTemplateManager.processTemplateBase(data);
		return data;
	}

	@SuppressWarnings("deprecation")
	@Override
	public String recoverTemplateCodeArea(Long templateId, String code, Long versionId) {
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("templateId", templateId);
		paraMap.put("code", code);
		String data = "";
		if(Validator.isNotNullOrEmpty(versionId)){
			List<CmsEditVersionArea> cmsEditVersionAreas = sdkCmsEditVersionAreaManager.findEffectCmsEditVersionAreaListByQueryMap(paraMap);
			if(cmsEditVersionAreas == null || cmsEditVersionAreas.size()==0){
				throw new BusinessException("编辑部分无需重置");
			}
			//删除编辑数据
			//根据模板id删除编辑区域数据
			sdkCmsEditAreaManager.removeCmsEditAreaByTemplateId(templateId,code);
		}else{
			List<CmsEditArea> cmsEditAreas = sdkCmsEditAreaManager.findEffectCmsEditAreaListByQueryMap(paraMap);
			if(cmsEditAreas == null || cmsEditAreas.size()==0){
				throw new BusinessException("编辑部分无需重置");
			}
		}
		//根据code在模板中找出需要还原的数据
		String html = findUpdatedCmsPageInstance(templateId);
		Map<String, String> map = processPageHtml(html);
		if(map.size()==0){
			throw new BusinessException("编辑部分无需重置");
		}
		data = map.get(code);
		Document document = Jsoup.parse(data);
		Elements eles =	document.getElementsByTag("body");
		if(eles!=null&& eles.size()!=0){
			data =eles.get(0).html();
		}
		//删除编辑数据
		//根据模板id删除编辑区域数据
		if(Validator.isNotNullOrEmpty(versionId)){
			sdkCmsEditVersionAreaManager.removeCmsEditVersionAreaByTemplateIdAndPageCode(templateId,code, versionId);
		}else{
			sdkCmsEditAreaManager.removeCmsEditAreaByTemplateId(templateId,code);
		}
		data = sdkCmsPageTemplateManager.processTemplateBase(data);
		return data;
	}
	
	/**
	 * 处理页面的html, 获取code与html
	 * 
	 * @param html
	 * @return
	 */
	private Map<String, String> processPageHtml(String html) {
		/** key:areaCode, value:html */
		Map<String, String> pageAreaMap = new HashMap<String, String>();
		Document document = Jsoup.parse(html);
		// 去掉 "编辑"按钮的div
		Elements editButtonElements = document.select(SdkCmsParseHtmlContentManagerImpl.CMS_DIV_EDIT_BUTTON_CLASS);
		editButtonElements.remove();
		//编辑html模式
		Elements elements = document.select(SdkCmsParseHtmlContentManagerImpl.CMS_HTML_EDIT_CLASS);
		dealHtml(pageAreaMap, elements,"cms-html-edit");
		//图文模式
		Elements imgElements = document.select(SdkCmsParseHtmlContentManagerImpl.CMS_IMGARTICLE_EDIT_CLASS);
		dealHtml(pageAreaMap, imgElements,"cms-imgarticle-edit");
		//商品模式
		//"cms-product-edit";
		Elements proElements = document.select(SdkCmsParseHtmlContentManagerImpl.CMS_PRODUCT_EDIT_CLASS);
		dealHtml(pageAreaMap, proElements,"cms-product-edit");
		
		return pageAreaMap;
	}

	private  void dealHtml(Map<String, String> pageAreaMap ,Elements elements,String cls){
		Integer i = 1;
		for (Element element : elements) {
			String areaCode = element.attr("code");
			if (StringUtils.isBlank(areaCode)) {
				throw new BusinessException(ErrorCodes.EDIT_AREA_CODE_NOT_EMPTY, new Object[] { i });
			}
			String areaHtml = element.html();
			pageAreaMap.put(areaCode, cls+"EDIT_CLASS_SEP"+sdkCmsPageTemplateManager.addTemplateBase(areaHtml));
			i++;
		}
	}

}
