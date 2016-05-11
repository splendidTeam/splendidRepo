package com.baozun.nebula.manager.cms;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.command.cms.CmsPageInstanceVersionCommand;
import com.baozun.nebula.constant.CacheKeyConstant;
import com.baozun.nebula.manager.CacheManager;
import com.baozun.nebula.manager.TimeInterval;
import com.baozun.nebula.model.cms.CmsPageInstance;
import com.baozun.nebula.model.cms.CmsPageTemplate;
import com.baozun.nebula.model.cms.CmsTemplateHtml;
import com.baozun.nebula.sdk.manager.SdkCmsEditAreaManager;
import com.baozun.nebula.sdk.manager.SdkCmsEditVersionAreaManager;
import com.baozun.nebula.sdk.manager.SdkCmsPageInstanceManager;
import com.baozun.nebula.sdk.manager.SdkCmsPageInstanceVersionManager;
import com.baozun.nebula.sdk.manager.SdkCmsPageTemplateManager;
import com.baozun.nebula.sdk.manager.SdkCmsParseHtmlContentManager;
import com.baozun.nebula.sdk.manager.SdkCmsPublishedManager;
import com.baozun.nebula.sdk.manager.SdkCmsTemplateHtmlManager;
import com.baozun.nebula.zk.ZooKeeperOperator;
import com.feilong.core.Validator;

@Service("publishCmsPageInstanceManager")
@Transactional
public class PublishCmsPageInstanceManagerImpl implements PublishCmsPageInstanceManager {

	
	@Autowired
	private SdkCmsPageTemplateManager	sdkCmsPageTemplateManager;

	@Autowired
	private SdkCmsPublishedManager		sdkCmsPublishedManager;

	@Autowired
	private SdkCmsPageInstanceManager	sdkCmsPageInstanceManager;
	
	@Autowired
	private SdkCmsPageInstanceVersionManager sdkCmsPageInstanceVersionManager;

	@Autowired
	private CacheManager				cacheManager;
	
	@Autowired
	private SdkCmsEditAreaManager sdkCmsEditAreaManager;

	@Autowired
	private SdkCmsEditVersionAreaManager sdkCmsEditVersionAreaManager;
	
	@Autowired
	private SdkCmsTemplateHtmlManager sdkCmsTemplateHtmlManager;
	
	@Autowired
	private ZooKeeperOperator			zooKeeperOperator;
	
	@Autowired
	private SdkCmsParseHtmlContentManager sdkCmsParseHtmlContentManager;
	
	@Override
		// TODO 1.还需要处理 使用公共头尾，也就是去掉body之外的内容
	public Map<String, String> findPublishPage(CmsPageInstance pageInstance) {
		// 2.公共资源的处理方法 <resource></resource>之间的内容
		
		/**
		 * 获取cms页面缓存数据步骤
		 * 1、获取页面未发布版本的缓存的，得到得到版本id
		 * 2、根据页面code获取发布页面缓存对象
		 * 3、存在缓存，在redis缓存中根据map字段中的"versionId"来判断当前需要发布的版本号是否是缓存中页面发布版本(基础版本)，如果不是就重新更新需要发布的缓存，
		 * 4、不存在缓存，重新更新需要发布的缓存
		 */

		Map<String, List<CmsPageInstanceVersionCommand>> versionCommand = cacheManager.getMapObject(CacheKeyConstant.CMS_PAGE_KEY, CacheKeyConstant.CMS_PAGE_VERSION_KEY);
		List<CmsPageInstanceVersionCommand> cmsPageInstanceVersionCommandList = new ArrayList<CmsPageInstanceVersionCommand>();

		if(Validator.isNotNullOrEmpty(versionCommand) && Validator.isNotNullOrEmpty(versionCommand.get(pageInstance.getCode()))){
			cmsPageInstanceVersionCommandList = versionCommand.get(pageInstance.getCode());
		}
		long publicVersionId = -1L;
		Date now = new Date();
		for(CmsPageInstanceVersionCommand cmsPageInstanceVersionCommand : cmsPageInstanceVersionCommandList){
			if(now.compareTo(cmsPageInstanceVersionCommand.getStart_time()) >= 0 && now.compareTo(cmsPageInstanceVersionCommand.getEnd_time()) <= 0){
				publicVersionId = cmsPageInstanceVersionCommand.getId();
				break;
			}
		}
		Map<String, String> result = cacheManager.getMapObject(CacheKeyConstant.CMS_PAGE_KEY, pageInstance.getCode());

		if (result == null) {
			result = new HashMap<String, String>();
			publicPage(pageInstance, publicVersionId, result);
		}else{
			if(Validator.isNullOrEmpty(result.get("version")) || publicVersionId != Long.valueOf(result.get("version"))){
				publicPage(pageInstance, publicVersionId, result);
			}
		}

		return result;
	}
	
	
	/**
	 * 发布页面缓存
	 * @param pageInstance
	 * @param publicVersionId
	 */
	private void publicPage(CmsPageInstance pageInstance, Long publicVersionId, Map<String, String> result){

		CmsPageTemplate template = sdkCmsPageTemplateManager.findCmsPageTemplateById(pageInstance.getTemplateId());

		/**********************/
		
		CmsTemplateHtml cmsTemplateHtml = sdkCmsTemplateHtmlManager.findCmsTemplateHtmlByPageCodeAndVersionId(pageInstance.getCode(), publicVersionId);
		if(Validator.isNotNullOrEmpty(cmsTemplateHtml)){
			result.put("data", cmsTemplateHtml.getData());
			result.put("resource", sdkCmsParseHtmlContentManager.findResource(cmsTemplateHtml.getData()));
			result.put("useCommonHeader", String.valueOf(template.getUseCommonHeader())	);
			result.put("version", String.valueOf(publicVersionId));
			cacheManager.setMapObject(CacheKeyConstant.CMS_PAGE_KEY, pageInstance.getCode(), result,
					TimeInterval.SECONDS_PER_WEEK);	
		}
	}
	
}
