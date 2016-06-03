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
package com.baozun.nebula.manager.cms;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.curator.ZkOperator;
import com.baozun.nebula.curator.invoke.ModuleMapWatchInvoke;
import com.baozun.nebula.curator.invoke.UrlMapWatchInvoke;
import com.baozun.nebula.model.cms.CmsEditArea;
import com.baozun.nebula.model.cms.CmsModuleInstance;
import com.baozun.nebula.model.cms.CmsPageInstance;
import com.baozun.nebula.model.cms.CmsTemplateHtml;
import com.baozun.nebula.sdk.manager.cms.SdkCmsEditAreaManager;
import com.baozun.nebula.sdk.manager.cms.SdkCmsModuleInstanceManager;
import com.baozun.nebula.sdk.manager.cms.SdkCmsPageInstanceManager;
import com.baozun.nebula.sdk.manager.cms.SdkCmsPageTemplateManager;
import com.baozun.nebula.sdk.manager.cms.SdkCmsParseHtmlContentManager;
import com.baozun.nebula.sdk.manager.cms.SdkCmsTemplateHtmlManager;
import com.feilong.core.Validator;

/**
 * CmsPageTemplateManager
 * @author  Justin
 *
 */
@Transactional
@Service("sdkCmsInitManager") 
public class SdkCmsInitManagerImpl implements SdkCmsInitManager {
	
	@Autowired
	private SdkCmsModuleInstanceManager sdkCmsModuleInstanceManager;
	@Autowired
	private SdkCmsPageTemplateManager sdkCmsPageTemplateManager;
	@Autowired
	private SdkCmsTemplateHtmlManager sdkCmsTemplateHtmlManager;
	@Autowired
	private SdkCmsPageInstanceManager sdkCmsPageInstanceManager;
	@Autowired
	private SdkCmsEditAreaManager sdkCmsEditAreaManager;
	@Autowired
	private SdkCmsParseHtmlContentManager sdkCmsParseHtmlContentManager;
	
	@Autowired
	private ZkOperator zkOperator;
	private final static Logger log = LoggerFactory.getLogger(SdkCmsInitManagerImpl.class);
	

	@Override
	public void megerCacheUrl(){
		/**
		 * 同步缓存信息步骤（兼容性同步）步骤
		 * 1、查询
		 */
		log.info("=====================================MegerCache Begin=========================================");
		try{
			Map<String, Object> paraMap = new HashMap<String, Object>();
			paraMap.put("isPublished", true);
			List<CmsPageInstance> cmsPageInstances = sdkCmsPageInstanceManager.findEffectCmsPageInstanceListByQueryMap(paraMap);
			Map<String, Object> tempMap = new HashMap<String, Object>();
			tempMap.put("lifecycle", 1);
			tempMap.put("versionId", -1);
			for(CmsPageInstance cmsPageInstance : cmsPageInstances){
				tempMap.put("pageCode", cmsPageInstance.getCode());
				List<CmsTemplateHtml> publishePage = sdkCmsTemplateHtmlManager.findCmsTemplateHtmlListByQueryMap(tempMap);	
				if(Validator.isNullOrEmpty(publishePage)){
					CmsTemplateHtml cmsTemplateHtml = new CmsTemplateHtml();
					cmsTemplateHtml.setCreateTime(new Date());
					cmsTemplateHtml.setLifecycle(1);
					cmsTemplateHtml.setPageCode(cmsPageInstance.getCode());
					cmsTemplateHtml.setVersionId(-1L);
					cmsTemplateHtml.setStartTime(cmsPageInstance.getStartTime());
					cmsTemplateHtml.setEndTime(cmsPageInstance.getEndTime());
					String data = sdkCmsParseHtmlContentManager.getParsePageData(cmsPageInstance.getTemplateId(), cmsPageInstance.getId(), null); 
					cmsTemplateHtml.setData(data);
					CmsTemplateHtml cmsTemplateHtmlNew = sdkCmsTemplateHtmlManager.saveCmsTemplateHtml(cmsTemplateHtml);
					log.info("This published Basic page's code is "+cmsPageInstance.getCode()+", save cmsTemplateHtml id is "+cmsTemplateHtmlNew.getId());
					zkOperator.noticeZkServer(zkOperator.getPath(UrlMapWatchInvoke.PATH_KEY),"#"+cmsPageInstance.getCode());
				}

			}

			/*****************************module**************************************/
			Map<String, Object> paraMapModuel = new HashMap<String, Object>();
			paraMapModuel.put("isPublished", true);
			List<CmsModuleInstance> cmsModulenstances = sdkCmsModuleInstanceManager.findEffectCmsModuleInstanceListByQueryMap(paraMapModuel);
			Map<String, Object> tempMapModule = new HashMap<String, Object>();
			tempMapModule.put("lifecycle", 1);
			tempMapModule.put("versionId", -1);
			Map<String, Object> paraMaparea = new HashMap<String, Object>();
			for(CmsModuleInstance cmsModulenstance : cmsModulenstances){
				tempMapModule.put("moduleCode", cmsModulenstance.getCode());
				List<CmsTemplateHtml> publishePage = sdkCmsTemplateHtmlManager.findCmsTemplateHtmlListByQueryMap(tempMapModule);	
				if(Validator.isNullOrEmpty(publishePage)){
					CmsTemplateHtml cmsTemplateHtml = new CmsTemplateHtml();
					cmsTemplateHtml.setCreateTime(new Date());
					cmsTemplateHtml.setLifecycle(1);
					cmsTemplateHtml.setModuleCode(cmsModulenstance.getCode());
					cmsTemplateHtml.setVersionId(-1L);			
					paraMaparea.put("moduleId", cmsModulenstance.getId());
					List<CmsEditArea> editAreaList = sdkCmsEditAreaManager.findCmsEditAreaListByQueryMap(paraMaparea);
					String data = sdkCmsParseHtmlContentManager.getParseModuleData(editAreaList, cmsModulenstance.getTemplateId());
					cmsTemplateHtml.setData(data);
					CmsTemplateHtml cmsTemplateHtmlNew = sdkCmsTemplateHtmlManager.saveCmsTemplateHtml(cmsTemplateHtml);
					log.info("This published Basic module's code is "+cmsModulenstance.getCode()+", save cmsTemplateHtml id is "+cmsTemplateHtmlNew.getId());
					zkOperator.noticeZkServer(zkOperator.getPath(ModuleMapWatchInvoke.PATH_KEY));
				}

			}
		}catch(Exception e){
			log.error("InitCms error, "+e.getMessage());
		}
		log.info("=====================================MegerCache End=========================================");
		
	}
	
	

}
