package com.baozun.nebula.sdk.manager.cms;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.dao.cms.CmsEditVersionAreaDao;
import com.baozun.nebula.model.cms.CmsEditVersionArea;

@Service
public class SdkCmsEditVersionAreaManagerImpl implements SdkCmsEditVersionAreaManager {

	@Autowired
	private CmsEditVersionAreaDao cmsEditVersionAreaDao;
	@Override
	public List<CmsEditVersionArea> findCmsEditVersionAreaListByQueryMap(Map<String, Object> paraMap) {
		// TODO Auto-generated method stub
		return cmsEditVersionAreaDao.findCmsEditVersionAreaListByQueryMap(paraMap);
	}

	@Override
	public CmsEditVersionArea findCmsEditVersionAreaById(Long id) {
		// TODO Auto-generated method stub
		return cmsEditVersionAreaDao.getByPrimaryKey(id);
	}

	@Override
	public CmsEditVersionArea saveCmsEditVersionArea(CmsEditVersionArea cmsEditVersionArea) {
		// TODO Auto-generated method stub
		return cmsEditVersionAreaDao.save(cmsEditVersionArea);
	}
	
	@Override
	@Transactional(readOnly=true)
	public CmsEditVersionArea queryEditVersionAreaHide(
			Map<String, Object> paraMap) {
		// TODO Auto-generated method stub
		return cmsEditVersionAreaDao.queryEditVersionAreaHide(paraMap);
	}

	@Override
	public void editVersionAreaHide(Map<String, Object> paraMap) {
		// TODO Auto-generated method stub
		cmsEditVersionAreaDao.editVersionAreaHide(paraMap);
	}

	@Override
	public List<CmsEditVersionArea> findEffectCmsEditVersionAreaListByQueryMap(
			Map<String, Object> paraMap) {
		// TODO Auto-generated method stub
		return cmsEditVersionAreaDao.findEffectCmsEditVersionAreaListByQueryMap(paraMap);
	}

	@Override
	public void removeCmsEditVersionAreaByTemplateIdAndPageCode(Long templateId, String code, Long versionId) {
		// TODO Auto-generated method stub
		cmsEditVersionAreaDao.removeCmsEditVersionAreaByTemplateIdAndPageCode(templateId, code, versionId);
	}
	
	@Override
	public void removeCmsEditVersionAreaByTemplateIdAndModuleCode(Long templateId, String moduleCode, Long versionId) {
		// TODO Auto-generated method stub
		cmsEditVersionAreaDao.removeCmsEditVersionAreaByTemplateIdAndModuleCode(templateId, moduleCode, versionId);
	}
}
