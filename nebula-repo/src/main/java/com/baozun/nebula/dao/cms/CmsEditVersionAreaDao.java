package com.baozun.nebula.dao.cms;

import java.util.List;
import java.util.Map;

import loxia.annotation.NativeQuery;
import loxia.annotation.NativeUpdate;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;

import com.baozun.nebula.model.cms.CmsEditVersionArea;



public interface CmsEditVersionAreaDao extends GenericEntityDao<CmsEditVersionArea, Long> {

	@NativeQuery(model = CmsEditVersionArea.class)
	public List<CmsEditVersionArea> findCmsEditVersionAreaListByQueryMap(@QueryParam Map<String, Object> paraMap);

	/**
	 * 
	* @author 谢楠
	* @Description: 修改显示或隐藏
	* @param paraMap   
	* void   
	* @throws
	 */
	@NativeUpdate
	void editVersionAreaHide(@QueryParam Map<String, Object> paraMap);
	
	
	@NativeQuery(model = CmsEditVersionArea.class)
	CmsEditVersionArea queryEditVersionAreaHide(@QueryParam Map<String, Object> paraMap);

	@NativeQuery(model = CmsEditVersionArea.class)
	List<CmsEditVersionArea> findEffectCmsEditVersionAreaListByQueryMap(@QueryParam Map<String, Object> paraMap);

	@NativeUpdate
	public void removeCmsEditVersionAreaByTemplateIdAndPageCode(@QueryParam("templateId")Long templateId,
			@QueryParam("code")String code, @QueryParam("versionId")Long versionId);
	
	@NativeUpdate
	public void removeCmsEditVersionAreaByTemplateIdAndModuleCode(@QueryParam("templateId")Long templateId,
			@QueryParam("moduleCode")String moduleCode, @QueryParam("versionId")Long versionId);
}