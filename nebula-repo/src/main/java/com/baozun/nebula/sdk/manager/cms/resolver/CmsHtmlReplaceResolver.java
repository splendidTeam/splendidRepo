package com.baozun.nebula.sdk.manager.cms.resolver;

import com.baozun.nebula.manager.BaseManager;

public interface CmsHtmlReplaceResolver extends BaseManager{

    /**
     * 根据配置文件将#{staticbase},#{pagebase}转换成真实的路径
     * 
     * @param html
     * @return
     */
    String processTemplateBase(String html);

}
