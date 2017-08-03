package com.baozun.nebula.manager.cms.moduletemplate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.dao.cms.CmsModuleTemplateDao;
import com.baozun.nebula.model.cms.CmsModuleTemplate;
import com.baozun.nebula.sdk.manager.cms.resolver.CmsHtmlReplaceResolver;

@Service("moduleTemplateManager")
@Transactional
public class ModuleTemplateManagerImpl implements ModuleTemplateManager{

    @Autowired
    private CmsModuleTemplateDao cmsModuleTemplateDao;

    @Autowired
    private CmsHtmlReplaceResolver cmsHtmlReplaceResolver;

    @Override
    public String packModuleTemplateById(Long id){
        CmsModuleTemplate template = cmsModuleTemplateDao.getByPrimaryKey(id);
        if (template != null){
            return cmsHtmlReplaceResolver.processTemplateBase(template.getData());
        }
        return "";
    }

}
