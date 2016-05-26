package com.baozun.nebula.sdk.manager.cms;

import java.util.List;

import com.baozun.nebula.command.cms.CmsPageInstanceVersionCommand;
import com.baozun.nebula.model.cms.CmsPageInstanceVersion;


public interface SdkCmsPageInstanceVersionManager {
	
	public List<CmsPageInstanceVersionCommand> findPublishNotOverInstanceVersion();

	public CmsPageInstanceVersion findCmsPageInstanceVersionById(Long versionId);

	/**
	 * 讲发布版本信息放入到缓存中
	 */
	public void setPublicVersionCacheInfo();
	
	
	public CmsPageInstanceVersion getCmsPageInstanceVersionById(Long versionId);
}