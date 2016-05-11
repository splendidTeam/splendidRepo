package com.baozun.nebula.manager.cms.pageversion;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baozun.nebula.dao.cms.CmsPageInstanceVersionDao;
import com.baozun.nebula.manager.CacheManager;
import com.baozun.nebula.manager.cms.pageinstance.CmsPageInstanceManager;
import com.baozun.nebula.model.cms.CmsEditArea;
import com.baozun.nebula.model.cms.CmsEditVersionArea;
import com.baozun.nebula.model.cms.CmsPageInstance;
import com.baozun.nebula.model.cms.CmsPageInstanceVersion;
import com.baozun.nebula.model.cms.CmsTemplateHtml;
import com.baozun.nebula.sdk.constants.Constants;
import com.baozun.nebula.sdk.manager.SdkCmsEditVersionAreaManager;
import com.baozun.nebula.sdk.manager.SdkCmsPageInstanceManager;
import com.baozun.nebula.sdk.manager.SdkCmsPageInstanceVersionManager;
import com.baozun.nebula.sdk.manager.SdkCmsPageTemplateManager;
import com.baozun.nebula.sdk.manager.SdkCmsParseHtmlContentManager;
import com.baozun.nebula.sdk.manager.SdkCmsPublishedManager;
import com.baozun.nebula.sdk.manager.SdkCmsTemplateHtmlManager;
import com.baozun.nebula.web.command.BackWarnEntity;
import com.baozun.nebula.zk.UrlMapWatchInvoke;
import com.baozun.nebula.zk.ZooKeeperOperator;
import com.feilong.core.Validator;

@Service
public class CmsPageInstanceVersionManagerImpl implements CmsPageInstanceVersionManager {

	private Logger logger = LoggerFactory.getLogger(CmsPageInstanceVersionManagerImpl.class);

	public final static String			CMS_HTML_EDIT_CLASS			= ".cms-html-edit";

	public final static String			CMS_DIV_EDIT_BUTTON_CLASS	= ".wui-tips";

	public final static String			NOEDIT_START				= "<!--noedit-start-->";
	
	public final static String			NOEDIT_END					= "<!--noedit-end-->";
	
	public final static String			CMS_IMGARTICLE_EDIT_CLASS			= ".cms-imgarticle-edit";
	
	public final static String			CMS_PRODUCT_EDIT_CLASS			= ".cms-product-edit";
	
	public final static Integer			PULISHED					= 1;
	
	@Autowired
	private CmsPageInstanceVersionDao cmsPageInstanceVersionDao;

	@Autowired
	private SdkCmsPageTemplateManager sdkCmsPageTemplateManager;
	
	@Autowired
	private SdkCmsEditVersionAreaManager sdkCmsEditVersionAreaManager;
	
	@Autowired
	private SdkCmsPageInstanceManager sdkCmsPageInstanceManager;
	
	@Autowired
	private SdkCmsPublishedManager sdkCmsPublishedManager;
	
	@Autowired
	private ZooKeeperOperator			zooKeeperOperator;

	@Autowired
	private CmsPageInstanceManager cmsPageInstanceManager;
	
	@Autowired
	private CacheManager				cacheManager;
	
	@Autowired
	private SdkCmsPageInstanceVersionManager sdkCmsPageInstanceVersionManager;
	
	@Autowired
	private SdkCmsTemplateHtmlManager sdkCmsTemplateHtmlManager;
	
	@Autowired
	private SdkCmsParseHtmlContentManager sdkCmsParseHtmlContentManager;

	@Override
	public Pagination<CmsPageInstanceVersion> findCmsPageInstanceVersionListByParaMapWithPage(
			Page page, Sort[] sorts, Map<String, Object> queryParam) {
		if(queryParam.containsKey("ispublished")){
			Integer isPublished = (Integer)queryParam.get("ispublished");
			if(PULISHED.equals(isPublished)){
				queryParam.put("ispublished", true);
			}else{
				queryParam.put("ispublished", false);
			}
		}
		return cmsPageInstanceVersionDao.findCmsPageInstanceVersionListByParaMapWithPage(page,sorts, queryParam);
	}

	@Override
	public List<CmsPageInstanceVersion> findCmsPageInstanceVersionListByParaMap(Map<String, Object> queryParam) {
		return cmsPageInstanceVersionDao.findCmsPageInstanceVersionListByParaMap(queryParam);
	}
	
	@Override
	public CmsPageInstanceVersion getCmsPageInstanceBaseVersion(Long instanceId) {
		return cmsPageInstanceVersionDao.getCmsPageInstanceBaseVersion(instanceId);
	}

	@Override
	public CmsPageInstanceVersion saveCmsPageInstanceVersion(CmsPageInstanceVersion cmsPageInstanceVersion) {
		return cmsPageInstanceVersionDao.save(cmsPageInstanceVersion);
	}

	@Override
	public CmsPageInstanceVersion getCmsPageInstanceVersionById(Long versionId) {
		return cmsPageInstanceVersionDao.getByPrimaryKey(versionId);
	}
	
	
	@Override
	public CmsPageInstanceVersion createOrUpdateCmsPageInstanceVersion(CmsPageInstanceVersion cmsPageInstanceVersion, String html) {
		/** 保存页面实例 */
		CmsPageInstanceVersion pageInstanceVersion = null;
		Long id = cmsPageInstanceVersion.getId();
		
		if (null != id) {
			// 修改
			CmsPageInstanceVersion pageversion = getCmsPageInstanceVersionById(id);
			pageversion.setInstanceId(cmsPageInstanceVersion.getInstanceId());
			pageversion.setName(cmsPageInstanceVersion.getName());
			pageversion.setLifecycle(CmsPageInstanceVersion.LIFECYCLE_ENABLE);
			pageversion.setModifyTime(new Date());
			pageversion.setCreateTime(new Date());
			pageversion.setType(CmsPageInstanceVersion.NORMAL_VERSION);			
			pageInstanceVersion = cmsPageInstanceVersionDao.save(pageversion);
		} else {
			// 保存
			cmsPageInstanceVersion.setCreateTime(new Date());
			cmsPageInstanceVersion.setLifecycle(CmsPageInstance.LIFECYCLE_ENABLE);
			cmsPageInstanceVersion.setType(CmsPageInstanceVersion.NORMAL_VERSION);
			cmsPageInstanceVersion.setVersion(new Date());
			cmsPageInstanceVersion.setPublished(false);
			pageInstanceVersion = cmsPageInstanceVersionDao.save(cmsPageInstanceVersion);
		}

		Map<String, String> editAreaMap = sdkCmsParseHtmlContentManager.processPageHtml(html, Constants.CMS_PAGE);
		Long versionId = pageInstanceVersion.getId();
		Long pageId = pageInstanceVersion.getInstanceId();
		Long tempId = pageInstanceVersion.getTemplateId();
		// 通过pageId删除所有的editArea 
		//不需要再删除
		//sdkCmsEditAreaManager.removeCmsEditAreaByPageId(pageId);
		// 保存页面编辑区域
		CmsEditVersionArea cmsEditVersionArea = null;
		for (Map.Entry<String, String> entry : editAreaMap.entrySet()) {
			//根据code查询
			Map<String, Object> paraMap = new  HashMap<String, Object>();
			paraMap.put("code", entry.getKey());
			paraMap.put("templateId", tempId);
			paraMap.put("pageId", pageId);
			paraMap.put("versionId", versionId);
			List<CmsEditVersionArea> cmsEditVersionAreas = sdkCmsEditVersionAreaManager.findCmsEditVersionAreaListByQueryMap(paraMap);
			//修改
			if(cmsEditVersionAreas!=null && cmsEditVersionAreas.size()>0){
				cmsEditVersionArea = sdkCmsEditVersionAreaManager.findCmsEditVersionAreaById(cmsEditVersionAreas.get(0).getId());
				cmsEditVersionArea.setData(entry.getValue());
				sdkCmsEditVersionAreaManager.saveCmsEditVersionArea(cmsEditVersionArea);
			}else{
				//新增
				cmsEditVersionArea = new CmsEditVersionArea();
				cmsEditVersionArea.setCode(entry.getKey());
				cmsEditVersionArea.setData(entry.getValue());
				cmsEditVersionArea.setLifecycle(CmsEditArea.LIFECYCLE_ENABLE);
				cmsEditVersionArea.setHide(CmsEditArea.LIFECYCLE_ENABLE);
				cmsEditVersionArea.setPageId(pageId);
				cmsEditVersionArea.setTemplateId(tempId);
				cmsEditVersionArea.setVersionId(versionId);
				cmsEditVersionArea.setCreateTime(new Date());
				
				sdkCmsEditVersionAreaManager.saveCmsEditVersionArea(cmsEditVersionArea);
			}
		}
		logger.info("save pageVersion id " + pageInstanceVersion.getId());
		return pageInstanceVersion;
	}
	
	@Override
	public BackWarnEntity removeCmsPageInstanceVersionByIds(List<Long> ids) {
		/**
		 * 删除实例版本的步骤
		 * 1、删除页面实例版本
		 * 2、删除页面发布版本缓存
		 * 3、判断该实例版本是否发布，如已发布进行如下操作
		 * 	1）在缓存中删除该页面实例
		 *  2）重新发布页面实例，该页面实例采用的是基础版本
		 */
		BackWarnEntity result = new BackWarnEntity();
		List<CmsPageInstanceVersion> cmsPageInstanceVersionList = findCmsPageInstanceVersionListByIds(ids);
		String removeids = "";
		try {
			// 删除页面实例版本
			cmsPageInstanceVersionDao.removeCmsPageInstanceVersionByIds(ids);
			//判断该实例是否发布，如已发布进行如下操作
			Set<String> pageCodeList = new HashSet<String>();
		
			for(CmsPageInstanceVersion cmsPageInstanceVersion : cmsPageInstanceVersionList){
				if(cmsPageInstanceVersion.isPublished()){
					CmsPageInstance cmsPageInstance = sdkCmsPageInstanceManager.findCmsPageInstanceById(cmsPageInstanceVersion.getInstanceId());
					pageCodeList.add(cmsPageInstance.getCode());
					CmsTemplateHtml cmsTemplateHtml = sdkCmsTemplateHtmlManager.findCmsTemplateHtmlByPageCodeAndVersionId(cmsPageInstance.getCode(), cmsPageInstanceVersion.getId());
					sdkCmsTemplateHtmlManager.removeCmsTemplateHtml(cmsTemplateHtml.getId());
				}
				removeids+=cmsPageInstanceVersion.getId()+",";
			}	

			sdkCmsPageInstanceVersionManager.setPublicVersionCacheInfo();
			zooKeeperOperator.noticeZkServer(UrlMapWatchInvoke.LISTEN_PATH);
			result.setIsSuccess(true);
			logger.info("removeCmsPageInstanceVersion Success, page id is "+removeids);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("removeCmsPageInstanceVersion Error, page id is "+removeids);
			result.setIsSuccess(false);
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public List<CmsPageInstanceVersion> findCmsPageInstanceVersionListByIds(List<Long> ids) {
		return cmsPageInstanceVersionDao.findCmsPageInstanceVersionListByIds(ids);
	}


	@Override
	public BackWarnEntity publishPageInstanceVersion(Long versionId, Date startTime, Date endTime) {
		/**
		 * 发布实例版本页面步骤
		 * 1、验证发布实例版本的时间是否正确（不能重复）
		 * 2、获取发布的实例版本
		 * 3、查询是否存在该发布实例版本的编辑html内容，如果不存在就新建
		 * 4、更新发布实例版本的编辑html内容
		 * 5、通知zk
		 */
		BackWarnEntity backWarnEntity = new BackWarnEntity();
		CmsPageInstanceVersion cmsPageInstanceVersion = cmsPageInstanceVersionDao.getByPrimaryKey(versionId);
		CmsPageInstance cmsPageInstance = sdkCmsPageInstanceManager.findCmsPageInstanceById(cmsPageInstanceVersion.getInstanceId());
		if(checkPublicCmsPageInstanceVersionConfilct(cmsPageInstance.getId(), versionId, startTime, endTime)){
			String pageCode =  cmsPageInstance.getCode();
			try {
				// 修改成已发布状态
				cmsPageInstanceVersion.setPublished(true);
				cmsPageInstanceVersion.setStartTime(startTime);
				cmsPageInstanceVersion.setEndTime(endTime);
				cmsPageInstanceVersionDao.save(cmsPageInstanceVersion);
				sdkCmsPageInstanceVersionManager.setPublicVersionCacheInfo();
				
				CmsTemplateHtml cmsTemplateHtml = sdkCmsTemplateHtmlManager.findCmsTemplateHtmlByPageCodeAndVersionId(pageCode, versionId);
				if(Validator.isNullOrEmpty(cmsTemplateHtml)){
					cmsTemplateHtml = new CmsTemplateHtml();
					cmsTemplateHtml.setCreateTime(new Date());	
					cmsTemplateHtml.setPageCode(pageCode);
					cmsTemplateHtml.setVersionId(versionId);
				}
				cmsTemplateHtml.setLifecycle(1);
				cmsTemplateHtml.setStartTime(startTime);
				cmsTemplateHtml.setEndTime(endTime);
				String data = sdkCmsParseHtmlContentManager.getParsePageData(cmsPageInstance.getTemplateId(), cmsPageInstance.getId(), versionId);
				cmsTemplateHtml.setData(data);
				sdkCmsTemplateHtmlManager.saveCmsTemplateHtml(cmsTemplateHtml);
				logger.info("publish PageInstanceVersion Success : versionId="+versionId+", startTime="+startTime+", endTime="+endTime);
				zooKeeperOperator.noticeZkServer(UrlMapWatchInvoke.LISTEN_PATH,"#"+cmsPageInstance.getCode());
				backWarnEntity.setIsSuccess(true);
			} catch (Exception e) {
				logger.error("publish PageInstanceVersion error : versionId="+versionId+", startTime="+startTime+", endTime="+endTime);
				e.printStackTrace();
				backWarnEntity.setDescription("实例发布失败，请重新发布");
				backWarnEntity.setIsSuccess(false);
			}
		}else{
			backWarnEntity.setDescription("已有页面实例在该时段发布了，请重新调整时间");
			backWarnEntity.setIsSuccess(false);
		}
		return backWarnEntity;

	}

	
	@Override
	public BackWarnEntity cancelPublishedPageInstanceVersion(Long versionId) {
		/**
		 * 取消发布实例版本流程
		 * 1、获取页面版本和页面实例
		 * 2、清除发布表中的要发布的实例的数据
		 * 2、更改实例版本的发布状态
		 * 3、重新发布基础版本
		 * 4、更新发布页面版本的缓存
		 */
		BackWarnEntity result = new BackWarnEntity();
		CmsPageInstanceVersion cmsPageInstanceVersion = getCmsPageInstanceVersionById(versionId);
		CmsPageInstance cmsPageInstance = sdkCmsPageInstanceManager.findCmsPageInstanceById(cmsPageInstanceVersion.getInstanceId());
		String pageCode = cmsPageInstance.getCode();
		try{		
			sdkCmsPublishedManager.removeCmsPubulishedByPageCode(pageCode);
			cmsPageInstanceVersion.setPublished(false);
			cmsPageInstanceVersionDao.save(cmsPageInstanceVersion);
			sdkCmsPageInstanceVersionManager.setPublicVersionCacheInfo();
			CmsTemplateHtml cmsTemplateHtml = sdkCmsTemplateHtmlManager.findCmsTemplateHtmlByPageCodeAndVersionId(pageCode, versionId);
			if(Validator.isNotNullOrEmpty(cmsTemplateHtml)){
				sdkCmsTemplateHtmlManager.removeCmsTemplateHtml(cmsTemplateHtml.getId());
			}
		}catch(Exception e){
			logger.error("cancelpublish pageversion error cause of publich baseversion error or removepublic error, pagecode="+cmsPageInstance.getCode()+", instanceId="+cmsPageInstance.getId()+", versionId="+versionId);
			result.setIsSuccess(false);
			result.setDescription("已发布的定时任务取消失败");
			e.printStackTrace();
			return result;
		}	
		zooKeeperOperator.noticeZkServer(UrlMapWatchInvoke.LISTEN_PATH,"#"+pageCode);
		result.setIsSuccess(true);
		logger.info("cancelpublish pageversion success, pagecode="+pageCode+", instanceId="+cmsPageInstance.getId()+", versionId="+versionId);
		return result;
	}
	
	/**
	 * 检测某时间段内是否有发布的页面版本实例如果有返回值的话，说明有包含该时间段的发布页面实例
	 * @param versionId 版本id
	 * @param startTime 版本发布开始时间
	 * @param endTime   版本发布结束时间
	 * @return
	 */
	private boolean checkPublicCmsPageInstanceVersionConfilct(Long instanceId, Long versionId, Date startTime, Date endTime) {
		if(Validator.isNotNullOrEmpty(startTime) && Validator.isNotNullOrEmpty(endTime)){
			List<CmsPageInstanceVersion> versionList = cmsPageInstanceVersionDao.findInstanceVersionInTimeQuantum(instanceId, versionId, startTime, endTime);
			if(Validator.isNullOrEmpty(versionList)){
				return true;
			}
		}
		return false;
	}

	@Override
	public BackWarnEntity copyPageInstanceVersion(Long versionId, String copyVersionName) {
		BackWarnEntity backWarnEntity = new BackWarnEntity();
		CmsPageInstanceVersion pageVersion = getCmsPageInstanceVersionById(versionId);
		if(Validator.isNotNullOrEmpty(pageVersion)){
			try{
				CmsPageInstanceVersion copyCmsPageInstanceVersion = new CmsPageInstanceVersion();
				copyCmsPageInstanceVersion.setCreateTime(new Date());
				copyCmsPageInstanceVersion.setInstanceId(pageVersion.getInstanceId());
				copyCmsPageInstanceVersion.setLifecycle(pageVersion.getLifecycle());
				copyCmsPageInstanceVersion.setTemplateId(pageVersion.getTemplateId());
				copyCmsPageInstanceVersion.setType(pageVersion.getType());
				copyCmsPageInstanceVersion.setPublished(false);
				copyCmsPageInstanceVersion.setName(copyVersionName);
				copyCmsPageInstanceVersion = saveCmsPageInstanceVersion(copyCmsPageInstanceVersion);
				Long copyVersionId = copyCmsPageInstanceVersion.getId();
				HashMap<String, Object> paraMap = new HashMap<String, Object>();
				paraMap.put("pageId", pageVersion.getInstanceId());  
				paraMap.put("versionId", versionId);  
				List<CmsEditVersionArea> cmsEditVersionAreas = sdkCmsEditVersionAreaManager.findEffectCmsEditVersionAreaListByQueryMap(paraMap);
				for(CmsEditVersionArea cmsEditVersionArea : cmsEditVersionAreas){
					CmsEditVersionArea copyEditVersionArea = new CmsEditVersionArea();
					copyEditVersionArea.setCode(cmsEditVersionArea.getCode());
					copyEditVersionArea.setCreateTime(new Date());
					copyEditVersionArea.setData(cmsEditVersionArea.getData());
					copyEditVersionArea.setHide(cmsEditVersionArea.getHide());
					copyEditVersionArea.setLifecycle(cmsEditVersionArea.getLifecycle());
					copyEditVersionArea.setPageId(cmsEditVersionArea.getPageId());
					copyEditVersionArea.setTemplateId(cmsEditVersionArea.getTemplateId());
					copyEditVersionArea.setVersionId(copyVersionId);
					sdkCmsEditVersionAreaManager.saveCmsEditVersionArea(copyEditVersionArea);
				}
				backWarnEntity.setIsSuccess(true);
			}catch(Exception e){
				e.printStackTrace();
				backWarnEntity.setDescription("复制页面版本失败");
				backWarnEntity.setIsSuccess(false);
			}
		}else{
			backWarnEntity.setIsSuccess(false);
			backWarnEntity.setDescription("页面版本不存在");
		}
		return backWarnEntity;

	}

}
