package com.baozun.nebula.sdk.manager.cms;

import java.util.List;
import java.util.Map;

import com.baozun.nebula.model.cms.CmsEditArea;
import com.baozun.nebula.model.cms.CmsEditVersionArea;

/**
 * CmsEditVersionAreaManager
 * @author xienan
 *
 */
public interface SdkCmsEditVersionAreaManager {
	
	/**
	 * 根据查询条件获取cms版本区域编辑数据
	 * @param paraMap 查询条件（key对应CmsEditVersionArea的字段名称）
	 * @return
	 */
	public List<CmsEditVersionArea> findCmsEditVersionAreaListByQueryMap(Map<String, Object> paraMap);

	/**
	 * 根据id获取CmsEditVersionArea
	 * @param id
	 * @return
	 */
	public CmsEditVersionArea findCmsEditVersionAreaById(Long id);

	/**
	 * 保存CmsEditVersionArea
	 * @param cmsEditVersionArea
	 * @return
	 */
	public CmsEditVersionArea saveCmsEditVersionArea(CmsEditVersionArea cmsEditVersionArea);
	
	
	/**
	 * 查询版本编辑区域信息
	 * @author 谢楠
	 * @param paraMap
	 * @return
	 */
	CmsEditVersionArea queryEditVersionAreaHide(Map<String, Object> paraMap);

	void editVersionAreaHide(Map<String, Object> paraMap);

	public List<CmsEditVersionArea> findEffectCmsEditVersionAreaListByQueryMap(
			Map<String, Object> paraMap);
	
	/**
	 * 根据模块id，版本id和代码来删除版本信息
	 * @param templateId 模板Id
	 * @param code pageCode
	 * @param versionId 版本Id
	 */
	public void removeCmsEditVersionAreaByTemplateIdAndPageCode(Long templateId, String code, Long versionId);

	/**
	 * 根据模块id，版本id和代码来删除版本信息
	 * @param templateId 模板Id
	 * @param code pageCode
	 * @param versionId 版本Id
	 */
	public void removeCmsEditVersionAreaByTemplateIdAndModuleCode(Long templateId,
			String moduleCode, Long versionId);

}
