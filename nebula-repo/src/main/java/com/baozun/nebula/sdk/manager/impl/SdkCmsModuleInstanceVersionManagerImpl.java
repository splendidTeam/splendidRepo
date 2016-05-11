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
package com.baozun.nebula.sdk.manager.impl;

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

import com.baozun.nebula.command.cms.CmsModuleInstanceVersionCommand;
import com.baozun.nebula.constant.CacheKeyConstant;
import com.baozun.nebula.dao.cms.CmsModuleInstanceVersionDao;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.manager.CacheManager;
import com.baozun.nebula.manager.TimeInterval;
import com.baozun.nebula.model.cms.CmsEditArea;
import com.baozun.nebula.model.cms.CmsEditVersionArea;
import com.baozun.nebula.model.cms.CmsModuleInstance;
import com.baozun.nebula.model.cms.CmsModuleInstanceVersion;
import com.baozun.nebula.model.cms.CmsPageInstance;
import com.baozun.nebula.model.cms.CmsPublished;
import com.baozun.nebula.model.cms.CmsTemplateHtml;
import com.baozun.nebula.sdk.constants.Constants;
import com.baozun.nebula.sdk.manager.SdkCmsEditVersionAreaManager;
import com.baozun.nebula.sdk.manager.SdkCmsModuleInstanceManager;
import com.baozun.nebula.sdk.manager.SdkCmsModuleInstanceVersionManager;
import com.baozun.nebula.sdk.manager.SdkCmsModuleTemplateManager;
import com.baozun.nebula.sdk.manager.SdkCmsPageTemplateManager;
import com.baozun.nebula.sdk.manager.SdkCmsParseHtmlContentManager;
import com.baozun.nebula.sdk.manager.SdkCmsPublishedManager;
import com.baozun.nebula.sdk.manager.SdkCmsTemplateHtmlManager;
import com.baozun.nebula.zk.ModuleMapWatchInvoke;
import com.baozun.nebula.zk.ZooKeeperOperator;
import com.feilong.core.Validator;

/**
 * SdkCmsModuleInstanceVersionManagerImpl
 * @author  xienan
 *
 */
@Transactional
@Service("sdkCmsModuleInstancVersioneManager") 
public class SdkCmsModuleInstanceVersionManagerImpl implements SdkCmsModuleInstanceVersionManager {
	
	private Logger logger = LoggerFactory.getLogger(SdkCmsModuleInstanceVersionManagerImpl.class);
	
	@Autowired
	private CmsModuleInstanceVersionDao cmsModuleInstanceVersionDao;
	
	@Autowired
	private SdkCmsPageTemplateManager sdkCmsPageTemplateManager;
	
	@Autowired
	private SdkCmsModuleTemplateManager sdkCmsModuleTemplateManager;
	
	@Autowired
	private SdkCmsEditVersionAreaManager sdkCmsEditVersionAreaManager;
	
	@Autowired
	private SdkCmsModuleInstanceManager sdkCmsModuleInstanceManager;
	
	@Autowired
	private SdkCmsPublishedManager sdkCmsPublishedManager;
	
	@Autowired
	private CacheManager cacheManager;
	
	@Autowired
	private ZooKeeperOperator			zooKeeperOperator;
	
	@Autowired
	private SdkCmsTemplateHtmlManager sdkCmsTemplateHtmlManager;
	
	@Autowired
	private SdkCmsParseHtmlContentManager sdkCmsParseHtmlContentManager; 
	
	public final static String			CMS_HTML_EDIT_CLASS			= ".cms-html-edit";

	public final static String			CMS_DIV_EDIT_BUTTON_CLASS	= ".wui-tips";

	public final static String			NOEDIT_START				= "<!--noedit-start-->";

	public final static String			NOEDIT_END					= "<!--noedit-end-->";
	
	public final static String			ONLYEDIT_START				= "<!--onlyedit-start-->";
	public final static String			ONLYEDIT_END				= "<!--onlyedit-end-->";
	
	public final static Integer			PULISHED					= 1;
	public final static String			CMS_IMGARTICLE_EDIT_CLASS			= ".cms-imgarticle-edit";
	
	public final static String			CMS_PRODUCT_EDIT_CLASS			= ".cms-product-edit";
	
	public static Map<String,List<CmsModuleInstanceVersionCommand>> moduleVersionMap = new HashMap<String,List<CmsModuleInstanceVersionCommand>>();
	
	@Override
	public Pagination<CmsModuleInstanceVersion> findCmsModuleInstanceVersionListByQueryMapWithPage(
			Page page, Sort[] sorts, Map<String, Object> paraMap) {
		// TODO Auto-generated method stub
		if(paraMap.containsKey("ispublished")){
			Integer isPublished = (Integer)paraMap.get("ispublished");
			if(PULISHED.equals(isPublished)){
				paraMap.put("ispublished", true);
			}else{
				paraMap.put("ispublished", false);
			}
		}
		return cmsModuleInstanceVersionDao.findCmsModuleInstanceVersionListByQueryMapWithPage(page, sorts, paraMap);
	}

	@Override
	public List<CmsModuleInstanceVersion> findCmsModuleInstanceVersionListByIds(
			List<Long> ids) {
		// TODO Auto-generated method stub
		return cmsModuleInstanceVersionDao.findCmsModuleInstanceVersionListByIds(ids);
	}
	
//	@Override
//	@Transactional(readOnly=true)
//	public String findUpdatedCmsModuleInstanceVersion(Long templateId, Long moduleId, Long versionId,
//			Boolean isEdit) {
//
//		CmsModuleTemplate template = sdkCmsModuleTemplateManager.findCmsModuleTemplateById(templateId);
//
//		String data = template.getData();
//		
//		if(StringUtils.isBlank(data)){
//			return "";
//		}
//
//		if (null != versionId) {
//			Map<String, Object> paraMap = new HashMap<String, Object>();
//			paraMap.put("versionId", versionId);
//			paraMap.put("moduleId", moduleId);
//			List<CmsEditVersionArea> editAreaList = sdkCmsEditVersionAreaManager.findCmsEditVersionAreaListByQueryMap(paraMap);
//
//			Document document = Jsoup.parse(data);
//			for (CmsEditVersionArea area : editAreaList) {
//				String code = area.getModuleCode();
//				Elements elements = document.select(CMS_HTML_EDIT_CLASS);
//				if (null != elements && elements.size() > 0) {
//					for (Element element : elements) {
//						if (code.equals(element.attr("code"))) {
//							//预览，处理隐藏
//							if(CmsEditArea.CMS_EDIT_AREA_HIDE.equals(area.getHide()) && !isEdit){
//								element.remove();
//							//修改，处理隐藏
//							}else if(CmsEditArea.CMS_EDIT_AREA_HIDE.equals(area.getHide()) && isEdit){
//								element.attr("hide", "0");
//								element.html(area.getData());
//							}else{
//								element.html(area.getData());
//							}
//						}
//					}
//
//				}
//				Elements imgArtiEles = document.select(CMS_IMGARTICLE_EDIT_CLASS);
//				if (null != imgArtiEles && imgArtiEles.size() > 0) {
//					for (Element element : imgArtiEles) {
//						if (code.equals(element.attr("code"))) {
//							if(CmsEditArea.CMS_EDIT_AREA_HIDE.equals(area.getHide()) && !isEdit){
//								element.remove();
//							}else if(CmsEditArea.CMS_EDIT_AREA_HIDE.equals(area.getHide()) && isEdit){
//								element.attr("hide", "0");
//								element.html(area.getData());								
//							}else{
//								element.html(area.getData());
//							}
//						}
//					}
//
//				}
//				
//				Elements proArtiEles = document.select(CMS_PRODUCT_EDIT_CLASS);
//				if (null != proArtiEles && proArtiEles.size() > 0) {
//					for (Element element : proArtiEles) {
//						if (code.equals(element.attr("code"))) {
//							element.html(area.getData());
//						}
//					}
//
//				}
//				
//			}
//			data = document.toString();
//		}
//		data = sdkCmsPageTemplateManager.processTemplateBase(data);
//		if (isEdit) {
//			data = processNoEditData(data);
//		}
//		return data;
//	
//	}
	
	@Override
	public CmsModuleInstanceVersion findCmsModuleInstanceVersionById(
			Long versionId) {
		// TODO Auto-generated method stub
		return cmsModuleInstanceVersionDao.getByPrimaryKey(versionId);
	}
	
	@Override
	public CmsModuleInstanceVersion createOrUpdateModuleInstanceVersion(CmsModuleInstanceVersion cmsModuleInstanceVersion, String html) {
		/** 保存/更新模块版本步骤
		 *  1、
		 */
		CmsModuleInstanceVersion version = null;
		Long id = cmsModuleInstanceVersion.getId();
		
		//checkPageInstanceCode(cmsModuleInstanceVersion, id);
		
		if (null != id) {
			// 修改
			CmsModuleInstanceVersion dbModuleVersion = cmsModuleInstanceVersionDao.getByPrimaryKey(id);
			//dbModuleVersion.setCode(cmsModuleInstanceVersion.getCode());
			dbModuleVersion.setName(cmsModuleInstanceVersion.getName());
			dbModuleVersion.setTemplateId(cmsModuleInstanceVersion.getTemplateId());
			dbModuleVersion.setModifyTime(new Date());
			version = cmsModuleInstanceVersionDao.save(dbModuleVersion);
		} else {
			// 保存
			cmsModuleInstanceVersion.setCreateTime(new Date());
			cmsModuleInstanceVersion.setLifecycle(CmsPageInstance.LIFECYCLE_ENABLE);
			cmsModuleInstanceVersion.setIsPublished(false);
			version = cmsModuleInstanceVersionDao.save(cmsModuleInstanceVersion);
		}

		Map<String, String> editAreaMap = sdkCmsParseHtmlContentManager.processPageHtml(html, Constants.CMS_MODULE);
		Long versionId = version.getId();
		// 保存页面编辑区域
		CmsEditVersionArea cmsEditArea = null;
		for (Map.Entry<String, String> entry : editAreaMap.entrySet()) {
			//根据moduleCode查询
			Map<String, Object> paraMap = new  HashMap<String, Object>();
			paraMap.put("moduleCode", entry.getKey());
			paraMap.put("moduleTemplateId", version.getTemplateId());
			paraMap.put("moduleId", version.getInstanceId());	
			paraMap.put("versionId", versionId);
			List<CmsEditVersionArea> cmsEditAreas = sdkCmsEditVersionAreaManager.findCmsEditVersionAreaListByQueryMap(paraMap);
			//修改
			if(cmsEditAreas!=null && cmsEditAreas.size()>0){
				cmsEditArea = sdkCmsEditVersionAreaManager.findCmsEditVersionAreaById(cmsEditAreas.get(0).getId());
				cmsEditArea.setData(entry.getValue());
				cmsEditArea.setModifyTime(new Date());
				sdkCmsEditVersionAreaManager.saveCmsEditVersionArea(cmsEditArea);
			}else{
				//新增
				cmsEditArea = new CmsEditVersionArea();
				cmsEditArea.setModuleCode(entry.getKey());
				cmsEditArea.setData(entry.getValue());
				cmsEditArea.setLifecycle(CmsEditArea.LIFECYCLE_ENABLE);
				cmsEditArea.setModuleId(version.getInstanceId());
				cmsEditArea.setModuleTemplateId(version.getTemplateId());
				cmsEditArea.setCreateTime(new Date());
				cmsEditArea.setVersionId(version.getId());
				sdkCmsEditVersionAreaManager.saveCmsEditVersionArea(cmsEditArea);
			}
		}
		logger.info("save ModuleInstanceVersion Success moduleVersion id : "+version.getId());
		return version;
	}
	
	
//	/**
//	 * 去掉不要加载的数据, 如:不要加载的js 去掉<!--noedit-start-->到<!--noedit-end-->中间的数据
//	 * 
//	 * @param Data
//	 * @return
//	 */
//	private static String processNoEditData(String data) {
//		StringBuffer sb = new StringBuffer();
//		int indexStart = data.indexOf(NOEDIT_START);
//		int indexEnd = data.indexOf(NOEDIT_END);
//
//		if (indexStart != -1 && indexEnd != -1) {
//			sb.append(data.substring(0, indexStart));
//			sb.append(data.substring(indexEnd + NOEDIT_END.length(), data.length()));
//			data = sb.toString();
//			data = processNoEditData(data);
//		}
//		return data;
//	}
	
//	/**
//	 * 处理页面的html, 获取code与html
//	 * 
//	 * @param html
//	 * @return
//	 */
//	private Map<String, String> processPageHtml(String html) {
//		/** key:areaCode, value:html */
//		Map<String, String> pageAreaMap = new HashMap<String, String>();
//		Document document = Jsoup.parse(html);
//		// 去掉 "编辑"按钮的div
//		Elements editButtonElements = document.select(CMS_DIV_EDIT_BUTTON_CLASS);
//		editButtonElements.remove();
//		Elements editHideButtonElements = document.select(".wui-tips-shade");
//		editHideButtonElements.remove();
//		//编辑html模式
//		Elements elements = document.select(CMS_HTML_EDIT_CLASS);
//		dealHtml(pageAreaMap, elements);
//		//图文模式
//		Elements imgElements = document.select(CMS_IMGARTICLE_EDIT_CLASS);
//		dealHtml(pageAreaMap, imgElements);
//		
//		//商品模式
//		Elements proElements =document.select(CMS_PRODUCT_EDIT_CLASS);
//		dealHtml(pageAreaMap, proElements);
//		
//		return pageAreaMap;
//	}
	
//	private  void dealHtml(Map<String, String> pageAreaMap ,Elements elements){
//		Integer i = 1;
//		for (Element element : elements) {
//			String areaCode = element.attr("code");
//			if (StringUtils.isBlank(areaCode)) {
//				throw new BusinessException("模块编辑区域的code不存在 ");
//			}
//			String areaHtml = element.html();
//			//处理静态base信息
//			areaHtml = sdkCmsPageTemplateManager.addTemplateBase(areaHtml);
//			pageAreaMap.put(areaCode, areaHtml);
//			i++;
//		}
//	}
	
//	@Override
//	public void setPublishNotOverModuleVersionInMap(){
//		List<CmsModuleInstanceVersionCommand> notOverModuleVersions = cmsModuleInstanceVersionDao.findPublishNotOverModuleVersion();
//		for(CmsModuleInstanceVersionCommand cmsModuleInstanceVersionCommand : notOverModuleVersions){
//			List<CmsModuleInstanceVersionCommand> groupVersionCommand = moduleVersionMap.get(cmsModuleInstanceVersionCommand.getCode());
//			//cacheManager.get(CacheKeyConstant.CMS_PAGE_KEY, cmsPageInstanceVersionCommand.getCode());	
//			if(Validator.isNullOrEmpty(groupVersionCommand)){
//				groupVersionCommand = new ArrayList<CmsModuleInstanceVersionCommand>();
//				groupVersionCommand.add(cmsModuleInstanceVersionCommand);
//				moduleVersionMap.put(cmsModuleInstanceVersionCommand.getCode(), groupVersionCommand);
//			}else{
//				groupVersionCommand.add(cmsModuleInstanceVersionCommand);
//				moduleVersionMap.put(cmsModuleInstanceVersionCommand.getCode(), groupVersionCommand);
//			}
//
//		}
//	}

	@Override
	public CmsModuleInstanceVersion getPublishingModuleVersion(Long id) {
		// TODO Auto-generated method stub
		return cmsModuleInstanceVersionDao.getPublishingModuleVersion(id);
	}
	
	/**
	 * 发布模块版本
	 */
	@Override
	public void publishModuleInstanceVersion(Long versionId, Date startTime, Date endTime) {
		// TODO Auto-generated method stub
		/**
		 * 发布模块版本步骤
		 * 1、获取模块版本
		 * 2、验证发布实例版本的时间是否正确（不能重复）
		 * 3、判断该模块版本是否是发布状态
		 * 4、更新该模块版本
		 * 5、发布模块信息
		 */
		CmsModuleInstanceVersion moduleVersion = cmsModuleInstanceVersionDao.getByPrimaryKey(versionId);
		if(checkPublicCmsModuleVersionConfilct(moduleVersion.getInstanceId(), versionId, startTime, endTime)){
			CmsModuleInstance module = sdkCmsModuleInstanceManager.findCmsModuleInstanceById(moduleVersion.getInstanceId());
			if(module.getIsPublished() && module.getLifecycle()!=2){
				try {
					// 修改成已发布状态
					moduleVersion.setIsPublished(true);
					moduleVersion.setStartTime(startTime);
					moduleVersion.setEndTime(endTime);
					cmsModuleInstanceVersionDao.save(moduleVersion);
					/************看下事务是否提交***********/
					List<CmsPublished> editAreaList = savePublishModuleInstanceVersion(versionId, startTime, endTime);
					CmsTemplateHtml cmsTemplateHtml = sdkCmsTemplateHtmlManager.findCmsTemplateHtmlByModuleCodeAndVersionId(module.getCode(), versionId);
					if(Validator.isNullOrEmpty(cmsTemplateHtml)){
						cmsTemplateHtml = new CmsTemplateHtml();
						cmsTemplateHtml.setCreateTime(new Date());	
						cmsTemplateHtml.setModuleCode(module.getCode());
						cmsTemplateHtml.setVersionId(versionId);
					}
					cmsTemplateHtml.setLifecycle(1);
					cmsTemplateHtml.setStartTime(startTime);
					cmsTemplateHtml.setEndTime(endTime);
					//String data = sdkCmsModuleInstanceManager.getHtmlDataByArea(editAreaList, module.getTemplateId());
					String data = sdkCmsParseHtmlContentManager.getParseModuleData(editAreaList, module.getTemplateId());
					cmsTemplateHtml.setData(data);
					sdkCmsTemplateHtmlManager.saveCmsTemplateHtml(cmsTemplateHtml);
					zooKeeperOperator.noticeZkServer(ModuleMapWatchInvoke.LISTEN_PATH);
					logger.info("publish moduleversion success : moduleId="+module.getId()+", versionId="+versionId+", startTime="+startTime+", endTime="+endTime);
					//return PublishResult.SUCCESS;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					logger.error("publish moduleversion error : moduleId="+module.getId()+", versionId="+versionId+", startTime="+startTime+", endTime="+endTime);
					e.printStackTrace();
					//return PublishResult.ERROR;
					throw new BusinessException(Constants.CMS_PUBLISH_ERROR);
				}
			}else{
				throw new BusinessException(Constants.CMS_INSTANCE_NOTPUBLISH);
				//return PublishResult.INSTANCENOTPUBLISH;
			}
		}else{
			throw new BusinessException(Constants.CMS_EXISTPUBLISHINSTANCE);
			//return PublishResult.EXISTPUBLISHINSTANCE;
		}
	}
	
	@Override
	public List<CmsPublished> savePublishModuleInstanceVersion(Long versionId, Date startTime, Date endTime) {
		/**
		 * 发布模块版本信息步骤
		 * 1、验证发布时间
		 * 2、获取模块版本及其实例
		 * 3、删除发布信息表中的发布模块信息
		 * 4、同步模块版本区域元素到发布信息表中
		 * 5、更新模块的状态
		 * 6、zk通知发布模块信息的变更（重新将发布模块信息保存到缓存中）
		 */
		List<CmsPublished> cmsPublishedList = new ArrayList<CmsPublished>();
		CmsModuleInstanceVersion moduleVersion = cmsModuleInstanceVersionDao.getByPrimaryKey(versionId);
		CmsModuleInstance moduleInstance = sdkCmsModuleInstanceManager.findCmsModuleInstanceById(moduleVersion.getInstanceId());
		String moduleCode = moduleInstance.getCode();
		// 先删除, 再添加
		sdkCmsPublishedManager.removeCmsPubulishedByModuleCode(moduleCode);
		Map<String, Object> paraMap = new HashMap<String, Object>();
		paraMap.put("versionId", moduleVersion.getId());
		paraMap.put("moduleId", moduleVersion.getInstanceId());
		List<CmsEditVersionArea> editAreaList = sdkCmsEditVersionAreaManager.findCmsEditVersionAreaListByQueryMap(paraMap);
		for (CmsEditVersionArea cmsEditArea : editAreaList) {
			CmsPublished cmsPublished = new CmsPublished();
			cmsPublished.setAreaCode(cmsEditArea.getModuleCode());
			cmsPublished.setModuleCode(moduleCode);
			cmsPublished.setHide(cmsEditArea.getHide());
			cmsPublished.setData(cmsEditArea.getData());
			cmsPublished.setPublishTime(new Date());
			cmsPublishedList.add(sdkCmsPublishedManager.saveCmsPublished(cmsPublished));
		}
		// 修改成已发布状态
		moduleVersion.setIsPublished(true);
		CmsModuleInstanceVersion moduleVersionNew = cmsModuleInstanceVersionDao.save(moduleVersion);
		logger.info("update ModuleInstanceVersion Success moduleVersion id : "+moduleVersionNew.getId());
		return cmsPublishedList;
	}
	
	/**
	 * 验证发布模块发布时间上是否冲突
	 * @param moduleId 模块id
	 * @param versionId 版本id
	 * @param startTime 发布开始时间
	 * @param endTime   发布结束时间
	 * @return
	 */
	private boolean checkPublicCmsModuleVersionConfilct(Long moduleId, Long versionId, Date startTime, Date endTime) {
		// TODO Auto-generated method stub
		if(Validator.isNotNullOrEmpty(startTime) && Validator.isNotNullOrEmpty(endTime)){
			List<CmsModuleInstanceVersion> versionList = cmsModuleInstanceVersionDao.findModuleVersionInTimeQuantum(moduleId, versionId, startTime, endTime);
			if(Validator.isNullOrEmpty(versionList)){
				return true;
			}
		}
		return false;
	}

	@Override
	public void cancelPublishedModuleInstanceVersion(Long versionId) {
		/**
		 * 取消发布模块版本步骤
		 * 1、获取模块版本集齐实例
		 * 2、判断模块是否发布，如果已发布
		 *   2.1、删除发布信息表中的该模块发布的信息
		 *   2.2、更新模块发布状态为未发布
		 *   2.3、发布模块实例（模块实例就是模块的基础版本）
		 */
		CmsModuleInstanceVersion moduleVersion = cmsModuleInstanceVersionDao.getByPrimaryKey(versionId);
		boolean isPublished = moduleVersion.getIsPublished();
		CmsModuleInstance moduleInstance = sdkCmsModuleInstanceManager.findCmsModuleInstanceById(moduleVersion.getInstanceId());
		if(moduleInstance.getIsPublished()){
			try{
				// 先删除, 再添加
				sdkCmsPublishedManager.removeCmsPubulishedByModuleCode(moduleInstance.getCode());
				moduleVersion.setIsPublished(false);
				cmsModuleInstanceVersionDao.save(moduleVersion);
				//sdkCmsModuleInstanceManager.publishModuleInstance(moduleInstance.getId());
				if(isPublished){
					CmsTemplateHtml cmsTemplateHtml = sdkCmsTemplateHtmlManager.findCmsTemplateHtmlByModuleCodeAndVersionId(moduleInstance.getCode(), versionId);		
					sdkCmsTemplateHtmlManager.removeCmsTemplateHtml(cmsTemplateHtml.getId());
				}
				zooKeeperOperator.noticeZkServer(ModuleMapWatchInvoke.LISTEN_PATH);
			}catch(Exception e){
				logger.error("cancelpublish moduleversion error cause of publich baseversion error or removepublic error, modulecode="+moduleInstance.getCode()+", moduleId="+moduleInstance.getId()+", versionId="+versionId);
				e.printStackTrace();
				throw new BusinessException(Constants.CMS_PUBLISH_ERROR);
				//return PublishResult.ERROR;
			}
			logger.info("cancelpublish pageversion success, modulecode="+moduleInstance.getCode()+", moduleId="+moduleInstance.getId()+", versionId="+versionId);
			//return PublishResult.SUCCESS;
		}else{
			throw new BusinessException(Constants.CMS_INSTANCE_CANCELED);
			//return PublishResult.INSTANCECANCELED;
		}
		
	}

	
	@Override
	public CmsModuleInstanceVersion findPublishModuleVersion(Long moduleid) {
		// TODO Auto-generated method stub
		return cmsModuleInstanceVersionDao.findPublishModuleVersion(moduleid, new Date());
	}


//	@Override
//	public void loadModuleVersionMap() {
//		// TODO Auto-generated method stub
//		
//		moduleVersionMap.clear();
//		
//		List<CmsModuleInstanceVersionCommand> notOverModuleVersions = cmsModuleInstanceVersionDao.findPublishNotOverModuleVersion();
//		for(CmsModuleInstanceVersionCommand cmsModuleInstanceVersionCommand : notOverModuleVersions){
//			List<CmsModuleInstanceVersionCommand> groupVersionCommand = moduleVersionMap.get(cmsModuleInstanceVersionCommand.getCode());
//			//cacheManager.get(CacheKeyConstant.CMS_PAGE_KEY, cmsPageInstanceVersionCommand.getCode());	
//			if(Validator.isNullOrEmpty(groupVersionCommand)){
//				groupVersionCommand = new ArrayList<CmsModuleInstanceVersionCommand>();
//				groupVersionCommand.add(cmsModuleInstanceVersionCommand);
//				moduleVersionMap.put(cmsModuleInstanceVersionCommand.getCode(), groupVersionCommand);
//			}else{
//				groupVersionCommand.add(cmsModuleInstanceVersionCommand);
//				moduleVersionMap.put(cmsModuleInstanceVersionCommand.getCode(), groupVersionCommand);
//			}
//
//		}
//	}
	
	@Override
	public void setPublicModuleVersionCacheInfo(){
		/**
		 * 1、查询所有未发布的版本页面，且对其按照发布页面code进行分组
		 * 2、将这些版本页面的信息传入到缓存中（这个缓存是按照模块code进行分组）
		 */
		List<CmsModuleInstanceVersionCommand> notOverModuleVersions = cmsModuleInstanceVersionDao.findPublishNotOverModuleVersion();
		Map<String, List<CmsModuleInstanceVersionCommand>> publishVersionsQueue = new HashMap<String, List<CmsModuleInstanceVersionCommand>>();
		for(CmsModuleInstanceVersionCommand cmsPageInstanceVersionCommand : notOverModuleVersions){
			List<CmsModuleInstanceVersionCommand> groupVersionCommand = publishVersionsQueue.get(cmsPageInstanceVersionCommand.getCode());
			//cacheManager.get(CacheKeyConstant.CMS_PAGE_KEY, cmsPageInstanceVersionCommand.getCode());	
			if(Validator.isNullOrEmpty(groupVersionCommand)){
				groupVersionCommand = new ArrayList<CmsModuleInstanceVersionCommand>();
				groupVersionCommand.add(cmsPageInstanceVersionCommand);
				publishVersionsQueue.put(cmsPageInstanceVersionCommand.getCode(), groupVersionCommand);
			}else{
				groupVersionCommand.add(cmsPageInstanceVersionCommand);
				publishVersionsQueue.put(cmsPageInstanceVersionCommand.getCode(), groupVersionCommand);
			}

		}
		cacheManager.setMapObject(CacheKeyConstant.CMS_MODULE_KEY, CacheKeyConstant.CMS_MODULE_VERSION_KEY, publishVersionsQueue,
				TimeInterval.SECONDS_PER_WEEK);
	}

	@Override
	public void removeCmsModuleInstanceVersionPublishByIds(List<Long> versionIds) {
		// TODO Auto-generated method stub
		//Set<Long> moduleIds = new HashSet<Long>();
		String ids="";
		for(Long versionId : versionIds){
			CmsModuleInstanceVersion version = findCmsModuleInstanceVersionById(versionId);
			CmsModuleInstance module = sdkCmsModuleInstanceManager.findCmsModuleInstanceById(version.getInstanceId());
			//moduleIds.add(version.getInstanceId());
			//已发布状态
			if(version.getIsPublished()){
				CmsTemplateHtml cmsTemplateHtml = sdkCmsTemplateHtmlManager.findCmsTemplateHtmlByModuleCodeAndVersionId(module.getCode(), version.getId());
				sdkCmsTemplateHtmlManager.removeCmsTemplateHtml(cmsTemplateHtml.getId());
			}	
			ids+=versionIds+",";
		}
		//CmsTemplateHtml cmsTemplateHtml = sdkCmsTemplateHtmlManager.findCmsTemplateHtmlByModuleCodeAndVersionId(code, versionId)
//		for(Long moduleId : moduleIds){
//			sdkCmsModuleInstanceManager.publishModuleInstance(moduleId);
//		}
		removeModuleVersionByIds(versionIds);
		logger.info("remove moduleVersion's ids is " + ids);
		zooKeeperOperator.noticeZkServer(ModuleMapWatchInvoke.LISTEN_PATH);
	}
	
	@Override
	public void removeModuleVersionByIds(List<Long> versionIds) {
		// TODO Auto-generated method stub
		cmsModuleInstanceVersionDao.removeCmsModuleInstanceVersionByIds(versionIds);	
	}

	@Override
	public CmsModuleInstanceVersion saveCmsModuleInstanceVersion(CmsModuleInstanceVersion version) {
		// TODO Auto-generated method stub
		return cmsModuleInstanceVersionDao.save(version);
	}

	@Override
	public void removeModuleVersionByModuleIds(List<Long> moduleIds) {
		// TODO Auto-generated method stub
		cmsModuleInstanceVersionDao.removeModuleVersionByModuleIds(moduleIds);
	}
	
	@Override
	public void cancelInstanceVersionInModuleId(Long moduleId) {
		// TODO Auto-generated method stub
		cmsModuleInstanceVersionDao.cancelInstanceVersionInModuleId(moduleId);
	}

	@Override
	public void copyModuleInstanceVersion(Long versionId, String name) {
		// TODO Auto-generated method stub
		CmsModuleInstanceVersion moduleVersion = findCmsModuleInstanceVersionById(versionId);
		if(Validator.isNotNullOrEmpty(moduleVersion)){
			try{
				CmsModuleInstanceVersion copyCmsModuleInstanceVersion = new CmsModuleInstanceVersion();
				copyCmsModuleInstanceVersion.setCreateTime(new Date());
				copyCmsModuleInstanceVersion.setInstanceId(moduleVersion.getInstanceId());
				copyCmsModuleInstanceVersion.setLifecycle(moduleVersion.getLifecycle());
				copyCmsModuleInstanceVersion.setTemplateId(moduleVersion.getTemplateId());
				copyCmsModuleInstanceVersion.setType(moduleVersion.getType());
				copyCmsModuleInstanceVersion.setIsPublished(false);
				copyCmsModuleInstanceVersion.setName(name);
				copyCmsModuleInstanceVersion = saveCmsModuleInstanceVersion(copyCmsModuleInstanceVersion);
				Long copyVersionId = copyCmsModuleInstanceVersion.getId();
				HashMap<String, Object> paraMap = new HashMap<String, Object>();
				paraMap.put("moduleId", moduleVersion.getInstanceId());  
				paraMap.put("versionId", versionId);  
				List<CmsEditVersionArea> cmsEditVersionAreas = sdkCmsEditVersionAreaManager.findEffectCmsEditVersionAreaListByQueryMap(paraMap);
				for(CmsEditVersionArea cmsEditVersionArea : cmsEditVersionAreas){
					CmsEditVersionArea copyEditVersionArea = new CmsEditVersionArea();
					copyEditVersionArea.setModuleCode(cmsEditVersionArea.getModuleCode());
					copyEditVersionArea.setCreateTime(new Date());
					copyEditVersionArea.setData(cmsEditVersionArea.getData());
					copyEditVersionArea.setHide(cmsEditVersionArea.getHide());
					copyEditVersionArea.setLifecycle(cmsEditVersionArea.getLifecycle());
					copyEditVersionArea.setModuleId(cmsEditVersionArea.getModuleId());
					copyEditVersionArea.setTemplateId(cmsEditVersionArea.getTemplateId());
					copyEditVersionArea.setVersionId(copyVersionId);
					sdkCmsEditVersionAreaManager.saveCmsEditVersionArea(copyEditVersionArea);
				}
				//result = CopyVersionResult.SUCCESS;
			}catch(Exception e){
				e.printStackTrace();
				//result = CopyVersionResult.ERROR;
				throw new BusinessException(Constants.CMS_COPY_ERROR);
			}
		}else{
			//result = CopyVersionResult.NOEXISTVERSION;
			throw new BusinessException(Constants.CMS_COPY_NOEXISTVERSION);
		}
		//return result;

	}

}
