package com.baozun.nebula.dao.cms;

import java.util.Date;
import java.util.List;
import java.util.Map;

import loxia.annotation.NativeQuery;
import loxia.annotation.NativeUpdate;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;
import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.command.cms.CmsPageInstanceVersionCommand;
import com.baozun.nebula.model.cms.CmsPageInstanceVersion;

public interface CmsPageInstanceVersionDao extends GenericEntityDao<CmsPageInstanceVersion, Long> {

	@NativeQuery(model = CmsPageInstanceVersion.class)
	public Pagination<CmsPageInstanceVersion> findCmsPageInstanceVersionListByParaMapWithPage(Page page,Sort[] sorts,@QueryParam Map<String, Object> paraMap);

	@NativeQuery(model = CmsPageInstanceVersion.class)
	public CmsPageInstanceVersion getCmsPageInstanceBaseVersion(@QueryParam("instanceId") Long instanceId);

	@NativeQuery(model = CmsPageInstanceVersion.class)
	public List<CmsPageInstanceVersion> findCmsPageInstanceVersionListByIds(@QueryParam("ids") List<Long> ids);

	@NativeUpdate
	public void removeCmsPageInstanceVersionByIds(@QueryParam("ids") List<Long> ids);

	@NativeQuery(model = CmsPageInstanceVersion.class)
	public List<CmsPageInstanceVersion> findCmsPageInstanceVersionListByParaMap(@QueryParam Map<String, Object> paraMap);

	@NativeQuery(model = CmsPageInstanceVersion.class)
	public List<CmsPageInstanceVersion> findCmsPageInstanceVersionInStartTime(@QueryParam("startTime") Date startTime);

	@NativeQuery(model = CmsPageInstanceVersion.class)
	public List<CmsPageInstanceVersion> findCmsPageInstanceVersionInEndTime(@QueryParam("endTime") Date endTime);

	@NativeQuery(model = CmsPageInstanceVersion.class)
	public List<CmsPageInstanceVersion> findInstanceVersionInTimeQuantum(@QueryParam("instanceId") Long instanceId, @QueryParam("versionId") Long versionId, @QueryParam("startTime") Date startTime, @QueryParam("endTime") Date endTime);
	
	@NativeQuery(model = CmsPageInstanceVersionCommand.class)
	public List<CmsPageInstanceVersionCommand> findPublishNotOverInstanceVersion();

	@NativeQuery(alias={"firstTime", "lastTime"}, clazzes={Date.class, Date.class})
	public Map<String, Date> getPublishedPageInstanceVersionsTimeRang(@QueryParam("instanceId") Long instanceId);
}