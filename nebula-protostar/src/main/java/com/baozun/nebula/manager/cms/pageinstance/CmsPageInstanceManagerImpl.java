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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.constant.CacheKeyConstant;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.ErrorCodes;
import com.baozun.nebula.manager.CacheManager;
import com.baozun.nebula.model.cms.CmsEditArea;
import com.baozun.nebula.model.cms.CmsPageInstance;
import com.baozun.nebula.model.cms.CmsPageTemplate;
import com.baozun.nebula.model.cms.CmsPublished;
import com.baozun.nebula.sdk.manager.SdkCmsEditAreaManager;
import com.baozun.nebula.sdk.manager.SdkCmsPageInstanceManager;
import com.baozun.nebula.sdk.manager.SdkCmsPageTemplateManager;
import com.baozun.nebula.sdk.manager.SdkCmsPublishedManager;
import com.baozun.nebula.sdk.manager.impl.SdkCmsModuleInstanceManagerImpl;
import com.baozun.nebula.zk.UrlMapWatchInvoke;
import com.baozun.nebula.zk.ZooKeeperOperator;

/**
 * 模板页面管理manager实现类
 * 
 * @author chenguang.zhou
 * @date 2014年7月2日 上午10:02:56
 */
@Service("cmsPageInstanceManager")
@Transactional
public class CmsPageInstanceManagerImpl implements CmsPageInstanceManager {

	@Autowired
	private SdkCmsPageInstanceManager	sdkCmsPageInstanceManager;

	@Autowired
	private SdkCmsPageTemplateManager	sdkCmsPageTemplateManager;

	@Autowired
	private SdkCmsEditAreaManager		sdkCmsEditAreaManager;

	@Autowired
	private SdkCmsPublishedManager		sdkCmsPublishedManager;

	@Autowired
	private ZooKeeperOperator			zooKeeperOperator;

	@Autowired
	private CacheManager				cacheManager;

	public final static String			CMS_HTML_EDIT_CLASS			= ".cms-html-edit";

	public final static String			CMS_DIV_EDIT_BUTTON_CLASS	= ".wui-tips";

	public final static String			NOEDIT_START				= "<!--noedit-start-->";
	
	public final static String			NOEDIT_END					= "<!--noedit-end-->";
	
	public final static Integer		PULISHED					= 1;
	public final static String			CMS_IMGARTICLE_EDIT_CLASS			= ".cms-imgarticle-edit";
	
	public final static String			CMS_PRODUCT_EDIT_CLASS			= ".cms-product-edit";

	@Override
	public CmsPageInstance findCmsPageInstanceById(Long id) {
		CmsPageInstance cmsPageInstance = sdkCmsPageInstanceManager.findCmsPageInstanceById(id);
		if (null == cmsPageInstance) {
			throw new BusinessException(ErrorCodes.PAGE_NOT_EXITES);
		}
		return cmsPageInstance;
	}

	@Override
	public Pagination<CmsPageInstance> findCmsPageInstanceListByParaMapWithPage(Page page, Sort[] sorts,
			Map<String, Object> paraMap) {
		
		if(paraMap.containsKey("ispublished")){
			Integer isPublished = (Integer)paraMap.get("ispublished");
			if(PULISHED.equals(isPublished)){
				paraMap.put("ispublished", true);
			}else{
				paraMap.put("ispublished", false);
			}
		}
		
		Pagination<CmsPageInstance> cmsPageInstancePage = sdkCmsPageInstanceManager
				.findCmsPageInstanceListByQueryMapWithPage(page, sorts, paraMap);
		return cmsPageInstancePage;
	}

	@Override
	public CmsPageInstance createOrUpdateCmsPageInstance(CmsPageInstance cmsPageInstance, String html) {
		/** 保存页面实例 */
		CmsPageInstance pageInstance = null;
		Long id = cmsPageInstance.getId();
		
		checkPageInstanceCodeOrUrl(cmsPageInstance, id);
		
		if (null != id) {
			// 修改
			CmsPageInstance page = sdkCmsPageInstanceManager.findCmsPageInstanceById(id);
			page.setCode(cmsPageInstance.getCode());
			page.setName(cmsPageInstance.getName());
			page.setUrl(cmsPageInstance.getUrl());
			page.setTemplateId(cmsPageInstance.getTemplateId());
			page.setSeoDescription(cmsPageInstance.getSeoDescription());
			page.setSeoKeywords(cmsPageInstance.getSeoKeywords());
			page.setSeoTitle(cmsPageInstance.getSeoTitle());
			page.setLifecycle(CmsPageInstance.LIFECYCLE_ENABLE);
			page.setModifyTime(new Date());
			pageInstance = sdkCmsPageInstanceManager.saveCmsPageInstance(page);
		} else {
			// 保存
			cmsPageInstance.setCreateTime(new Date());
			cmsPageInstance.setLifecycle(CmsPageInstance.LIFECYCLE_ENABLE);
			pageInstance = sdkCmsPageInstanceManager.saveCmsPageInstance(cmsPageInstance);
		}

		Map<String, String> editAreaMap = processPageHtml(html);
		Long pageId = pageInstance.getId();
		// 通过pageId删除所有的editArea 
		//不需要再删除
		//sdkCmsEditAreaManager.removeCmsEditAreaByPageId(pageId);
		// 保存页面编辑区域
		CmsEditArea cmsEditArea = null;
		for (Map.Entry<String, String> entry : editAreaMap.entrySet()) {
			//根据code查询
			Map<String, Object> paraMap = new  HashMap<String, Object>();
			paraMap.put("code", entry.getKey());
			paraMap.put("templateId", pageInstance.getTemplateId());
			paraMap.put("pageId", pageId);
			List<CmsEditArea> 	 cmsEditAreas = sdkCmsEditAreaManager.findCmsEditAreaListByQueryMap(paraMap);
			//修改
			if(cmsEditAreas!=null && cmsEditAreas.size()>0){
				cmsEditArea = sdkCmsEditAreaManager.findCmsEditAreaById(cmsEditAreas.get(0).getId());
				cmsEditArea.setData(entry.getValue());
				sdkCmsEditAreaManager.saveCmsEditArea(cmsEditArea);
			}else{
				//新增
				cmsEditArea = new CmsEditArea();
				cmsEditArea.setCode(entry.getKey());
				cmsEditArea.setData(entry.getValue());
				cmsEditArea.setLifecycle(CmsEditArea.LIFECYCLE_ENABLE);
				cmsEditArea.setHide(CmsEditArea.LIFECYCLE_ENABLE);
				cmsEditArea.setPageId(pageId);
				cmsEditArea.setTemplateId(pageInstance.getTemplateId());
				cmsEditArea.setCreateTime(new Date());
				
				sdkCmsEditAreaManager.saveCmsEditArea(cmsEditArea);
			}
		}
		return pageInstance;
	}
	
	
	private void checkPageInstanceCodeOrUrl(CmsPageInstance cmsPageInstance, Long id){
		// 检察 页面编码, url 是否已存在
		Map<String, Object> paraMap = new HashMap<String, Object>(); 
		if(null != id){
			paraMap.put("pageId", id);
		}
		paraMap.put("code", cmsPageInstance.getCode());
		CmsPageInstance checkPageInstance = sdkCmsPageInstanceManager.checkPageInstanceCode(paraMap);
		if(checkPageInstance != null){
			throw new BusinessException(ErrorCodes.PAGE_CODE_EXITES);
		}
		
		paraMap.remove("code");
		paraMap.put("url", cmsPageInstance.getUrl());
		paraMap.put("type", cmsPageInstance.getSupportType());
		checkPageInstance = sdkCmsPageInstanceManager.checkPageInstanceUrl(paraMap);
		if(checkPageInstance != null){
			throw new BusinessException(ErrorCodes.PAGE_URL_EXITES);
		}
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
		Elements editButtonElements = document.select(CMS_DIV_EDIT_BUTTON_CLASS);
		editButtonElements.remove();
		Elements editHideButtonElements = document.select(".wui-tips-shade");
		editHideButtonElements.remove();
		//编辑html模式
		dealHtml(pageAreaMap, document, CMS_HTML_EDIT_CLASS);
		//图文模式
		dealHtml(pageAreaMap,  document,CMS_IMGARTICLE_EDIT_CLASS);
		//商品模式
		dealHtml(pageAreaMap, document,CMS_PRODUCT_EDIT_CLASS);
		
		return pageAreaMap;
	}

	private  void dealHtml(Map<String, String> pageAreaMap ,Document document,String cls){
		Integer i = 1;
		Elements elements = document.select(cls);
		for (Element element : elements) {
			String areaCode = element.attr("code");
			if (StringUtils.isBlank(areaCode)) {
				throw new BusinessException(ErrorCodes.EDIT_AREA_CODE_NOT_EMPTY, new Object[] { i });
			}
			String areaHtml = element.html();
			pageAreaMap.put(areaCode, sdkCmsPageTemplateManager.addTemplateBase(areaHtml));
			i++;
		}
	}
	@Override
	public void removeCmsPageInstanceByIds(List<Long> ids) {
		List<CmsPageInstance> cmsPageInstanceList = sdkCmsPageInstanceManager.findCmsPageInstanceListByIds(ids);
		// 删除页面实例
		sdkCmsPageInstanceManager.removeCmsPageInstanceByIds(ids);
		
		List<String> pageCodeList = new ArrayList<String>();
		for(CmsPageInstance cmsPageInstance : cmsPageInstanceList){
			String pageCode = cmsPageInstance.getCode();
			pageCodeList.add(pageCode);
			cacheManager.removeMapValue(CacheKeyConstant.CMS_PAGE_KEY, pageCode);
		}
		// 删除pulished表中的数据 
		sdkCmsPublishedManager.removeCmsPubulishedByPageCodes(pageCodeList);
		
		zooKeeperOperator.noticeZkServer(UrlMapWatchInvoke.LISTEN_PATH);
	}

	@Override
	public String findUpdatedCmsPageInstance(Long templateId, Long pageId, Boolean isEdit) {
		CmsPageTemplate template = sdkCmsPageTemplateManager.findCmsPageTemplateById(templateId);

		String data = template.getData();
		
		if(StringUtils.isBlank(data)){
			return "";
		}

		if (null != pageId) {
			Map<String, Object> paraMap = new HashMap<String, Object>();
			paraMap.put("pageId", pageId);
			List<CmsEditArea> editAreaList = sdkCmsEditAreaManager.findCmsEditAreaListByQueryMap(paraMap);
			
			Document document = Jsoup.parse(data);
			for (CmsEditArea area : editAreaList) {
				//html模式
				dealEditClass(document, area, CMS_HTML_EDIT_CLASS, isEdit);
				//图文模式
				dealEditClass(document, area, CMS_IMGARTICLE_EDIT_CLASS, isEdit);
				//商品模式
				dealEditClass(document, area, CMS_PRODUCT_EDIT_CLASS, isEdit);
			}
			CmsPageInstance cmsPageInstance = sdkCmsPageInstanceManager.findCmsPageInstanceById(pageId);
			//处理seo信息
			sdkCmsPageInstanceManager.setSeoInfo(document, cmsPageInstance);
			data = document.toString();
		}
		
		data = sdkCmsPageTemplateManager.processTemplateBase(data);
		if (isEdit) {
			data = processNoEditData(data);
		}else{
			data = processOnlyEditData(data);
		}
		return data;
	}
	/**
	 * 
	* @author 何波
	* @Description: 去掉查看时不需要的区域 
	* @param data
	* @return   
	* String   
	* @throws
	 */
	private static String processOnlyEditData(String data) {
		StringBuffer sb = new StringBuffer();
		int indexStart = data.indexOf(SdkCmsModuleInstanceManagerImpl.ONLYEDIT_START);
		int indexEnd = data.indexOf(SdkCmsModuleInstanceManagerImpl.ONLYEDIT_END);

		if (indexStart != -1 && indexEnd != -1) {
			sb.append(data.substring(0, indexStart));
			sb.append(data.substring(indexEnd + SdkCmsModuleInstanceManagerImpl.ONLYEDIT_END.length(), data.length()));
			data = sb.toString();
			data = processOnlyEditData(data);
		}
		return data;
	}
	/**
	 * 
	* @author 何波
	* @Description: 处理各种编辑class
	* @param document
	* @param area
	* @param cls
	* @param isEdit   
	* void   
	* @throws
	 */
	private  void dealEditClass(Document document,CmsEditArea area,String cls,boolean isEdit){
		String code = area.getCode();
		Elements proEles = document.select(cls);
		if (null != proEles && proEles.size() > 0) {
			for (Element element : proEles) {
				if (code.equals(element.attr("code"))) {
					if(isEdit){
						element.attr("hide", String.valueOf(area.getHide()));
						element.html(area.getData());
						if(cls.equals(".cms-product-edit")){
							sdkCmsPageInstanceManager.setProductInfo(element,isEdit);
						}
					}else{
						if(area.getHide()!=null && area.getHide()==0){
							element.remove();
						}else{
							element.html(area.getData());
							if(cls.equals(".cms-product-edit")){
								sdkCmsPageInstanceManager.setProductInfo(element,isEdit);
							}
						}
					}
				}
			}

		}
	}
	/**
	 * 去掉不要加载的数据, 如:不要加载的js 去掉<!--noedit-start-->到<!--noedit-end-->中间的数据
	 * 
	 * @param Data
	 * @return
	 */
	private static String processNoEditData(String data) {
		StringBuffer sb = new StringBuffer();
		int indexStart = data.indexOf(NOEDIT_START);
		int indexEnd = data.indexOf(NOEDIT_END);

		if (indexStart != -1 && indexEnd != -1) {
			sb.append(data.substring(0, indexStart));
			sb.append(data.substring(indexEnd + NOEDIT_END.length(), data.length()));
			data = sb.toString();
			data = processNoEditData(data);
		}
		return data;
	}

	@Override
	public void publishPageInstance(Long pageId,Date startTime,Date endTime) {
		CmsPageInstance cmsPageInstance = sdkCmsPageInstanceManager.findCmsPageInstanceById(pageId);
		String pageCode = cmsPageInstance.getCode();
		// 先删除, 再添加
		sdkCmsPublishedManager.removeCmsPubulishedByPageCode(pageCode);

		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("pageId", cmsPageInstance.getId());
		List<CmsEditArea> editAreaList = sdkCmsEditAreaManager.findCmsEditAreaListByQueryMap(paraMap);
		CmsPublished cmsPublished = null;
		for (CmsEditArea cmsEditArea : editAreaList) {
			cmsPublished = new CmsPublished();
			cmsPublished.setAreaCode(cmsEditArea.getCode());
			cmsPublished.setPageCode(pageCode);
			cmsPublished.setData(cmsEditArea.getData());
			cmsPublished.setHide(cmsEditArea.getHide());
			cmsPublished.setPublishTime(new Date());
			sdkCmsPublishedManager.saveCmsPublished(cmsPublished);
		}
		// 修改成已发布状态
		cmsPageInstance.setIsPublished(true);
		cmsPageInstance.setStartTime(startTime);
		cmsPageInstance.setEndTime(endTime);
		sdkCmsPageInstanceManager.saveCmsPageInstance(cmsPageInstance);
		
		zooKeeperOperator.noticeZkServer(UrlMapWatchInvoke.LISTEN_PATH,"#"+cmsPageInstance.getCode());
		
	}

	@Override
	public CmsPageInstance checkPageInstanceCode(String code, Long pageId) {
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("code", code);
		if (null != pageId) {
			paraMap.put("pageId", pageId);
		}
		CmsPageInstance cmsPageInstance = sdkCmsPageInstanceManager.checkPageInstanceCode(paraMap);

		return cmsPageInstance;
	}

	@Override
	public CmsPageInstance checkPageInstanceUrl(String url, Long pageId,int type) {
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("url", url);
		paraMap.put("type", type);
		if (null != pageId) {
			paraMap.put("pageId", pageId);
		}
		return sdkCmsPageInstanceManager.checkPageInstanceUrl(paraMap);
	}

	@Override
	public void cancelPublishedPageInstance(Long pageId) {
		CmsPageInstance cmsPageInstance = sdkCmsPageInstanceManager.findCmsPageInstanceById(pageId);
		sdkCmsPublishedManager.removeCmsPubulishedByPageCode(cmsPageInstance.getCode());
		cmsPageInstance.setIsPublished(false);
		sdkCmsPageInstanceManager.saveCmsPageInstance(cmsPageInstance);
		zooKeeperOperator.noticeZkServer(UrlMapWatchInvoke.LISTEN_PATH,"#"+cmsPageInstance.getCode());
	}
	
	
}
