package com.baozun.nebula.manager.cms.pageversion;

import java.util.Date;
import java.util.List;
import java.util.Map;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.model.cms.CmsPageInstanceVersion;
import com.baozun.nebula.web.command.BackWarnEntity;

public interface CmsPageInstanceVersionManager {
	
	/**
	 * 获取CmsPageInstanceVersion分页
	 * @param page 页号
	 * @param sorts 排序
	 * @param queryParam 参数
	 * @return
	 */
	public Pagination<CmsPageInstanceVersion> findCmsPageInstanceVersionListByParaMapWithPage(Page page, Sort[] sorts, Map<String, Object> queryParam);

	public List<CmsPageInstanceVersion> findCmsPageInstanceVersionListByParaMap(Map<String, Object> queryParam);
	
	/**
	 * 根据实例id获取基础版本
	 * @param instanceId 实例id
	 * @return
	 */
	public CmsPageInstanceVersion getCmsPageInstanceBaseVersion(Long instanceId);

	/**
	 * 保存cms页面对象
	 * @param instanceId 实例id
	 * @return
	 */
	public CmsPageInstanceVersion saveCmsPageInstanceVersion(CmsPageInstanceVersion cmsPageInstanceVersion);

	/**
	 * 创建或更新CmsPageInstanceVersion
	 * @param instanceId 实例id
	 * @return
	 */
	public CmsPageInstanceVersion createOrUpdateCmsPageInstanceVersion(CmsPageInstanceVersion cmsPageInstanceVersion, String html);

	/**
	 * 通过版本id获取CmsPageInstanceVersion
	 * @param versionId 版本id
	 * @return
	 */
	public CmsPageInstanceVersion getCmsPageInstanceVersionById(Long versionId);

	/**
	 * 根据ids获取CmsPageInstanceVersion集合
	 * @param ids 实例id
	 * @return
	 */
	public List<CmsPageInstanceVersion> findCmsPageInstanceVersionListByIds(List<Long> ids);

	/**
	 * 通过ids删除CmsPageInstanceVersion
	 * @param ids 实例id
	 * @return
	 */
	public BackWarnEntity removeCmsPageInstanceVersionByIds(List<Long> ids);

	/**
	 * 发布页面实例版本
	 * @param versionId 版本id
	 * @param startTime 发布开始时间
	 * @param endTime   发布结束时间
	 * @return
	 */
	public BackWarnEntity publishPageInstanceVersion(Long versionId, Date startTime, Date endTime);

	/**
	 * 取消页面实例版本
	 * @param versionId 版本id
	 * @return
	 */
	public BackWarnEntity cancelPublishedPageInstanceVersion(Long versionId);

	/**
	 * 拷贝页面版本
	 * @param versionId 版本id
	 * @return
	 */
	public BackWarnEntity copyPageInstanceVersion(Long versionId, String copyVersionName);

}