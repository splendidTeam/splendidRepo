package com.baozun.nebula.sdk.manager.cms;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.dao.cms.CmsTemplateHtmlDao;
import com.baozun.nebula.model.cms.CmsTemplateHtml;

@Transactional
@Service("sdkCmsTemplateHtmlManager") 
public class SdkCmsTemplateHtmlManagerImpl implements SdkCmsTemplateHtmlManager {

	@Autowired
	private CmsTemplateHtmlDao cmsTemplateHtmlDao;
	
	@Override
	public CmsTemplateHtml saveCmsTemplateHtml(CmsTemplateHtml model) {
		return cmsTemplateHtmlDao.save(model);
	}
	
	@Override
	@Transactional(readOnly=true)
	public CmsTemplateHtml findCmsTemplateHtmlById(Long id) {
		return cmsTemplateHtmlDao.getByPrimaryKey(id);
	}

	@Override
	@Transactional(readOnly=true)
	public CmsTemplateHtml findCmsTemplateHtmlByModuleCodeAndVersionId(String code, Long versionId) {
		return cmsTemplateHtmlDao.findCmsTemplateHtmlByModuleCodeAndVersionId(code, versionId);
	}
	
	@Override
	@Transactional(readOnly=true)
	public CmsTemplateHtml findCmsTemplateHtmlByPageCodeAndVersionId(String code, Long versionId) {
		return cmsTemplateHtmlDao.findCmsTemplateHtmlByPageCodeAndVersionId(code, versionId);
	}
	
	@Override
	@Transactional(readOnly=true)
	public List<CmsTemplateHtml> findCmsTemplateHtmlListByQueryMap(Map<String, Object> paraMap) {
		return cmsTemplateHtmlDao.findCmsTemplateHtmlListByQueryMap(paraMap);
	}

	@Override
	public void removeCmsTemplateHtml(Long id) {
		cmsTemplateHtmlDao.removeCmsTemplateHtml(id);
	}

	@Override
	public void removeCmsTemplateHtmlByModuleCode(String moduleCode) {
		cmsTemplateHtmlDao.removeCmsTemplateHtmlByModuleCode(moduleCode);
	}

	@Override
	public void removeCmsTemplateHtmlByPageCode(String pageCode) {
		cmsTemplateHtmlDao.removeCmsTemplateHtmlByPageCode(pageCode);
	}
}
