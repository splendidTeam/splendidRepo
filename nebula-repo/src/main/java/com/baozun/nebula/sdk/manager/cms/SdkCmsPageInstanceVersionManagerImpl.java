package com.baozun.nebula.sdk.manager.cms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baozun.nebula.command.cms.CmsPageInstanceVersionCommand;
import com.baozun.nebula.constant.CacheKeyConstant;
import com.baozun.nebula.dao.cms.CmsPageInstanceVersionDao;
import com.baozun.nebula.manager.CacheManager;
import com.baozun.nebula.manager.TimeInterval;
import com.baozun.nebula.model.cms.CmsPageInstanceVersion;
import com.feilong.core.Validator;

@Service
public class SdkCmsPageInstanceVersionManagerImpl implements SdkCmsPageInstanceVersionManager {
	
	@Autowired
	private CmsPageInstanceVersionDao cmsPageInstanceVersionDao;
	
	@Autowired
	private CacheManager cacheManager;
	
	@Override
	public List<CmsPageInstanceVersionCommand> findPublishNotOverInstanceVersion() {
		// TODO Auto-generated method stub
		return cmsPageInstanceVersionDao.findPublishNotOverInstanceVersion();
	}
	
	@Override
	public CmsPageInstanceVersion findCmsPageInstanceVersionById(Long versionId) {
		// TODO Auto-generated method stub
		return cmsPageInstanceVersionDao.getByPrimaryKey(versionId);
	}
	
	@Override
	public CmsPageInstanceVersion getCmsPageInstanceVersionById(Long versionId) {
		// TODO Auto-generated method stub
		return cmsPageInstanceVersionDao.getByPrimaryKey(versionId);
	}
	
	@Override
	public void setPublicVersionCacheInfo(){
		/**
		 * 1、查询所有未发布的版本页面，且对其按照发布页面code进行分组
		 * 2、将这些版本页面的信息传入到缓存中
		 */
		List<CmsPageInstanceVersionCommand> notOverVersions = findPublishNotOverInstanceVersion();
		Map<String, List<CmsPageInstanceVersionCommand>> publishVersionsQueue = new HashMap<String, List<CmsPageInstanceVersionCommand>>();
		for(CmsPageInstanceVersionCommand cmsPageInstanceVersionCommand : notOverVersions){
			List<CmsPageInstanceVersionCommand> groupVersionCommand = publishVersionsQueue.get(cmsPageInstanceVersionCommand.getCode());
			//cacheManager.get(CacheKeyConstant.CMS_PAGE_KEY, cmsPageInstanceVersionCommand.getCode());	
			if(Validator.isNullOrEmpty(groupVersionCommand)){
				groupVersionCommand = new ArrayList<CmsPageInstanceVersionCommand>();
				groupVersionCommand.add(cmsPageInstanceVersionCommand);
				publishVersionsQueue.put(cmsPageInstanceVersionCommand.getCode(), groupVersionCommand);
			}else{
				groupVersionCommand.add(cmsPageInstanceVersionCommand);
				publishVersionsQueue.put(cmsPageInstanceVersionCommand.getCode(), groupVersionCommand);
			}

		}
		cacheManager.setMapObject(CacheKeyConstant.CMS_PAGE_KEY, CacheKeyConstant.CMS_PAGE_VERSION_KEY, publishVersionsQueue,
				TimeInterval.SECONDS_PER_WEEK);
	}
}
