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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.command.cms.CmsPageInstanceVersionCommand;
import com.baozun.nebula.constant.CacheKeyConstant;
import com.baozun.nebula.curator.ZkOperator;
import com.baozun.nebula.curator.invoke.UrlMapWatchInvoke;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.ErrorCodes;
import com.baozun.nebula.manager.CacheManager;
import com.baozun.nebula.manager.TimeInterval;
import com.baozun.nebula.manager.cms.pageversion.CmsPageInstanceVersionManager;
import com.baozun.nebula.model.cms.CmsEditArea;
import com.baozun.nebula.model.cms.CmsPageInstance;
import com.baozun.nebula.model.cms.CmsPageInstanceVersion;
import com.baozun.nebula.model.cms.CmsPublished;
import com.baozun.nebula.model.cms.CmsTemplateHtml;
import com.baozun.nebula.sdk.constants.Constants;
import com.baozun.nebula.sdk.manager.cms.SdkCmsEditAreaManager;
import com.baozun.nebula.sdk.manager.cms.SdkCmsPageInstanceManager;
import com.baozun.nebula.sdk.manager.cms.SdkCmsPageTemplateManager;
import com.baozun.nebula.sdk.manager.cms.SdkCmsParseHtmlContentManager;
import com.baozun.nebula.sdk.manager.cms.SdkCmsPublishedManager;
import com.baozun.nebula.sdk.manager.cms.SdkCmsTemplateHtmlManager;
import com.feilong.core.Validator;

/**
 * 模板页面管理manager实现类
 * 
 * @author chenguang.zhou
 * @date 2014年7月2日 上午10:02:56
 */
@Service("cmsPageInstanceManager")
@Transactional
public class CmsPageInstanceManagerImpl implements CmsPageInstanceManager {
	private static Logger			log	= LoggerFactory.getLogger(CmsPageInstanceManagerImpl.class);
	
	@Autowired
	private SdkCmsPageInstanceManager	sdkCmsPageInstanceManager;

	@Autowired
	private SdkCmsPageTemplateManager	sdkCmsPageTemplateManager;

	@Autowired
	private SdkCmsEditAreaManager		sdkCmsEditAreaManager;

	@Autowired
	private SdkCmsPublishedManager		sdkCmsPublishedManager;

	@Autowired
	private ZkOperator 					zkOperator;

	@Autowired
	private CacheManager				cacheManager;
	
	@Autowired
	private CmsPageInstanceVersionManager cmsPageInstanceVersionManager;
	
	@Autowired
	private SdkCmsTemplateHtmlManager sdkCmsTemplateHtmlManager;
	
	@Autowired
	private SdkCmsParseHtmlContentManager sdkCmsParseHtmlContentManager;
	
	public final static Integer			PULISHED					= 1;
	
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
			cmsPageInstance.setIsPublished(false);
			pageInstance = sdkCmsPageInstanceManager.saveCmsPageInstance(cmsPageInstance);
		}

		Map<String, String> editAreaMap = sdkCmsParseHtmlContentManager.processPageHtml(html, Constants.CMS_PAGE);
		Long pageId = pageInstance.getId();
		
		// 保存页面编辑区域
		CmsEditArea cmsEditArea = null;
		for (Map.Entry<String, String> entry : editAreaMap.entrySet()) {
			//根据code查询
			Map<String, Object> paraMap = new  HashMap<String, Object>();
			paraMap.put("code", entry.getKey());
			paraMap.put("templateId", pageInstance.getTemplateId());
			paraMap.put("pageId", pageId);
			List<CmsEditArea> cmsEditAreas = sdkCmsEditAreaManager.findCmsEditAreaListByQueryMap(paraMap);
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
		log.info("save Page Success page's id ="+pageInstance.getId());
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
	
	@Override
	public void removeCmsPageInstanceByIds(List<Long> ids) {
		List<CmsPageInstance> cmsPageInstanceList = sdkCmsPageInstanceManager.findCmsPageInstanceListByIds(ids);
		// 删除页面实例
		sdkCmsPageInstanceManager.removeCmsPageInstanceByIds(ids);

		List<String> pageCodeList = new ArrayList<String>();
		Map<String, List<CmsPageInstanceVersionCommand>> cmsPageInstanceVersionList = cacheManager.getMapObject(CacheKeyConstant.CMS_PAGE_KEY, CacheKeyConstant.CMS_PAGE_VERSION_KEY);
		String instanceIds = "";
		for(CmsPageInstance cmsPageInstance : cmsPageInstanceList){
			String pageCode = cmsPageInstance.getCode();
			pageCodeList.add(pageCode);
			cacheManager.removeMapValue(CacheKeyConstant.CMS_PAGE_KEY, pageCode);
			//删除版本实例
			cmsPageInstanceVersionList.remove(pageCode);
		
			sdkCmsTemplateHtmlManager.removeCmsTemplateHtmlByPageCode(pageCode);
			instanceIds += cmsPageInstance.getId()+",";
		}
		cacheManager.setMapObject(CacheKeyConstant.CMS_PAGE_KEY, CacheKeyConstant.CMS_PAGE_VERSION_KEY, cmsPageInstanceVersionList,
				TimeInterval.SECONDS_PER_WEEK);
		// 删除pulished表中的数据 
		sdkCmsPublishedManager.removeCmsPubulishedByPageCodes(pageCodeList);
		// 删除所有发布的页面实例实例版本定时器
		Map<String, Object> queryParam = new HashMap<String, Object>();
		queryParam.put("instanceIds", ids);
		String vids = "";
		List<CmsPageInstanceVersion> cmsPageInstanceVersions = cmsPageInstanceVersionManager.findCmsPageInstanceVersionListByParaMap(queryParam);
		try{
			for(CmsPageInstanceVersion cmsPageInstanceVersion : cmsPageInstanceVersions){
				cmsPageInstanceVersion.setPublished(false);
				cmsPageInstanceVersion.setLifecycle(2);
				cmsPageInstanceVersionManager.saveCmsPageInstanceVersion(cmsPageInstanceVersion);
				vids += cmsPageInstanceVersion.getId()+",";
			}
			log.info("remove PublishedPage's id="+instanceIds+", PublishedPageVersions Success versionids="+vids);
		}catch(Exception e){
			e.printStackTrace();
			log.error("remove PublishedPage's id="+instanceIds+" , PublishedPageVersions Error versionids="+vids);
		}
		
		zkOperator.noticeZkServer(zkOperator.getPath(UrlMapWatchInvoke.PATH_KEY));
	}

	@Override
	public void publishPageInstance(Long pageId,Date startTime,Date endTime) {
		CmsPageInstance cmsPageInstance = sdkCmsPageInstanceManager.findCmsPageInstanceById(pageId);
		try{
			String pageCode = cmsPageInstance.getCode();
			// 先删除, 再添加
			sdkCmsPublishedManager.removeCmsPubulishedByPageCode(pageCode);
			log.debug("PublishPage, RemovePublishPage pagecode : " + pageCode);
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


			CmsTemplateHtml cmsTemplateHtml = sdkCmsTemplateHtmlManager.findCmsTemplateHtmlByPageCodeAndVersionId(pageCode, -1L);
			if(Validator.isNullOrEmpty(cmsTemplateHtml)){
				cmsTemplateHtml = new CmsTemplateHtml();
				cmsTemplateHtml.setCreateTime(new Date());	
				cmsTemplateHtml.setPageCode(pageCode);
				cmsTemplateHtml.setVersionId(-1L);
			}
			cmsTemplateHtml.setLifecycle(1);
			cmsTemplateHtml.setStartTime(startTime);
			cmsTemplateHtml.setEndTime(endTime);
			String data = sdkCmsParseHtmlContentManager.getParsePageData(cmsPageInstance.getTemplateId(), pageId, null);
			cmsTemplateHtml.setData(data);
			sdkCmsTemplateHtmlManager.saveCmsTemplateHtml(cmsTemplateHtml);
			log.debug("PublishPage, Save CmsTemplateHtml's id : " + cmsTemplateHtml.getId());
			zkOperator.noticeZkServer(zkOperator.getPath(UrlMapWatchInvoke.PATH_KEY),"#"+cmsPageInstance.getCode());
			log.info("PublishPage Success, Page's id : "+cmsPageInstance.getId()+", code : " + cmsPageInstance.getCode());
		}catch(Exception e){
			log.info("PublishPage Error, Page's id : "+cmsPageInstance.getId()+", code : " + cmsPageInstance.getCode()+", error cause is "+e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void cancelExpireVersion(Long pageId){
		//校验该模块下的版本是否有发布到期没有取消掉的
		Map<String, Object> expire = new HashMap<String, Object>();
		expire.put("instanceId", pageId);
		expire.put("endTimeEnd", new Date());
		expire.put("lifecycle", 1);
		expire.put("ispublished", true);
		List<CmsPageInstanceVersion> expireVersions = cmsPageInstanceVersionManager.findCmsPageInstanceVersionListByParaMap(expire);
		for(CmsPageInstanceVersion expireVersion : expireVersions){
			log.info("publish page's id is " + pageId + "it's expired version's id is " + expireVersion.getId());
			cmsPageInstanceVersionManager.cancelPublishedPageInstanceVersion(expireVersion.getId());	
		}
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
		// 删除所有发布的页面实例实例版本定时器
		Map<String, Object> queryParam = new HashMap<String, Object>();
		queryParam.put("ispublished", true);
		String vids = "";
		List<CmsPageInstanceVersion> publishCmsPageInstanceVersions = cmsPageInstanceVersionManager.findCmsPageInstanceVersionListByParaMap(queryParam);
		Map<String, List<CmsPageInstanceVersionCommand>> cmsPageInstanceVersionList = cacheManager.getMapObject(CacheKeyConstant.CMS_PAGE_KEY, CacheKeyConstant.CMS_PAGE_VERSION_KEY);
		try{
			for(CmsPageInstanceVersion publishCmsPageInstanceVersion : publishCmsPageInstanceVersions){
				publishCmsPageInstanceVersion.setPublished(false);
				cmsPageInstanceVersionManager.saveCmsPageInstanceVersion(publishCmsPageInstanceVersion);
				vids += publishCmsPageInstanceVersion.getId()+",";
			}
			sdkCmsTemplateHtmlManager.removeCmsTemplateHtmlByPageCode(cmsPageInstance.getCode());
			log.debug("cancelPublishedPage removeCmsTemplateHtml error PageCode="+cmsPageInstance.getCode());
			cmsPageInstanceVersionList.remove(cmsPageInstance.getCode());
			cacheManager.setMapObject(CacheKeyConstant.CMS_PAGE_KEY, CacheKeyConstant.CMS_PAGE_VERSION_KEY, cmsPageInstanceVersionList,
					TimeInterval.SECONDS_PER_WEEK);
			cacheManager.removeMapValue(CacheKeyConstant.CMS_PAGE_KEY, cmsPageInstance.getCode());
			log.info("cancelPublishedPage Success, Page's id : "+cmsPageInstance.getId()+", code : " + cmsPageInstance.getCode());
		}catch(Exception e){
			e.printStackTrace();
			log.error("cancelPublishedInstanceVersion Error versionids="+vids);
		}
		zkOperator.noticeZkServer(zkOperator.getPath(UrlMapWatchInvoke.PATH_KEY),"#"+cmsPageInstance.getCode());
		
	}

	

	
}
