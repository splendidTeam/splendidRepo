package com.baozun.nebula.sdk.manager.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.dao.cms.CmsTemplateHtmlDao;
import com.baozun.nebula.model.cms.CmsTemplateHtml;
import com.baozun.nebula.sdk.manager.SdkCmsTemplateHtmlManager;

@Transactional
@Service("sdkCmsTemplateHtmlManager") 
public class SdkCmsTemplateHtmlManagerImpl implements SdkCmsTemplateHtmlManager {

	@Autowired
	private CmsTemplateHtmlDao cmsTemplateHtmlDao;
	
	@Override
	public CmsTemplateHtml saveCmsTemplateHtml(CmsTemplateHtml model) {
		// TODO Auto-generated method stub
		return cmsTemplateHtmlDao.save(model);
	}
	
	@Override
	@Transactional(readOnly=true)
	public CmsTemplateHtml findCmsTemplateHtmlById(Long id) {
		// TODO Auto-generated method stub
		return cmsTemplateHtmlDao.getByPrimaryKey(id);
	}

	@Override
	@Transactional(readOnly=true)
	public CmsTemplateHtml findCmsTemplateHtmlByModuleCodeAndVersionId(String code, Long versionId) {
		// TODO Auto-generated method stub
		return cmsTemplateHtmlDao.findCmsTemplateHtmlByModuleCodeAndVersionId(code, versionId);
	}
	
	@Override
	@Transactional(readOnly=true)
	public CmsTemplateHtml findCmsTemplateHtmlByPageCodeAndVersionId(String code, Long versionId) {
		// TODO Auto-generated method stub
		return cmsTemplateHtmlDao.findCmsTemplateHtmlByPageCodeAndVersionId(code, versionId);
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<CmsTemplateHtml> findCmsTemplateHtmlListByQueryMap(Map<String, Object> paraMap) {
		// TODO Auto-generated method stub
		return cmsTemplateHtmlDao.findCmsTemplateHtmlListByQueryMap(paraMap);
	}

	@Override
	public void removeCmsTemplateHtml(Long id) {
		// TODO Auto-generated method stub
		cmsTemplateHtmlDao.removeCmsTemplateHtml(id);
	}

	@Override
	public void removeCmsTemplateHtmlByModuleCode(String moduleCode) {
		// TODO Auto-generated method stub
		cmsTemplateHtmlDao.removeCmsTemplateHtmlByModuleCode(moduleCode);
	}

	@Override
	public void removeCmsTemplateHtmlByPageCode(String pageCode) {
		// TODO Auto-generated method stub
		cmsTemplateHtmlDao.removeCmsTemplateHtmlByPageCode(pageCode);
	}
}
