package com.baozun.nebula.manager.cms.moduletemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.dao.cms.CmsModuleTemplateDao;
import com.baozun.nebula.model.cms.CmsModuleTemplate;
import com.baozun.nebula.sdk.manager.SdkCmsCommonManager;


@Service("moduleTemplateManager")
@Transactional
public class ModuleTemplateManagerImpl implements ModuleTemplateManager{

	@Autowired
	private CmsModuleTemplateDao 	cmsModuleTemplateDao;
	
	@Autowired
	private SdkCmsCommonManager 	sdkCmsCommonManager;
	
	/**
	 * 静态base标识
	 */
	public final static String STATIC_BASE_CHAR="#{staticbase}";
	
	/**
	 * 页面base标识
	 */
	public final static String PAGE_BASE_CHAR="#{pagebase}";
	
	/**
	 * 图片base标识
	 */
	public final static String IMG_BASE_CHAR="#{imgbase}";
	
	@Override
	public String packModuleTemplateById(Long id) {
		
		CmsModuleTemplate template =cmsModuleTemplateDao.getByPrimaryKey(id);
		
		if(template!=null){
			
			return sdkCmsCommonManager.processTemplateBase(template.getData());
		}
		
		return "";
	}

}
