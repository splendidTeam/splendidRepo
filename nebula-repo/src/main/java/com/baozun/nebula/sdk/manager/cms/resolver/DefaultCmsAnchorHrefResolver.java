/**
 * Copyright (c) 2010 Jumbomart All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Jumbomart.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Jumbo.
 *
 * JUMBOMART MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. JUMBOMART SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.sdk.manager.cms.resolver;

import static org.apache.commons.lang3.StringUtils.EMPTY;

import org.springframework.stereotype.Component;

import com.feilong.core.net.URIUtil;

import static com.feilong.core.Validator.isNullOrEmpty;

/**
 * The Class DefaultCmsSrcResolver.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.22
 */
@Component("cmsAnchorHrefResolver")
public class DefaultCmsAnchorHrefResolver implements CmsAnchorHrefResolver{

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.cms.builder.CmsSrcResolver#resolver(java.lang.String)
     */
    @Override
    public String resolver(String href){
        if (isNullOrEmpty(href)){
            return EMPTY;
        }
        if (href.startsWith("javascript:void(0)") || href.startsWith("JAVASCRIPT:VOID(0)") || href.startsWith("#")){
            return EMPTY;
        }

        //---------------------------------------------------------------------

        //以 #{pagebase} 开头,那么不动, 后面有替换
        //since 5.3.2.22
        if (href.startsWith(DefaultCmsHtmlReplaceResolver.PAGE_BASE_CHAR)){
            return href;
        }
        //---------------------------------------------------------------------

        //以 / 开头
        if (href.startsWith("/")){
            return DefaultCmsHtmlReplaceResolver.PAGE_BASE_CHAR + href;
        }

        //不是以 http 开头
        if (!URIUtil.create(href).isAbsolute()){
            return DefaultCmsHtmlReplaceResolver.PAGE_BASE_CHAR + "/" + href;
        }

        return href;
    }
}
