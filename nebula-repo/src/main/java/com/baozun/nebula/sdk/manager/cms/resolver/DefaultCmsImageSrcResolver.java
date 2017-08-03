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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.feilong.core.net.URIUtil;

import static com.feilong.core.Validator.isNullOrEmpty;

/**
 * The Class DefaultCmsSrcResolver.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.22
 */
@Component("cmsImageSrcResolver")
public class DefaultCmsImageSrcResolver implements CmsImageSrcResolver{

    /** 上传图片的域名. */
    @Value("#{meta['upload.img.domain.base']}")
    private String UPLOAD_IMG_DOMAIN = "";

    /** version. */
    private final static String VERSION = "version=000000";

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.cms.builder.CmsSrcResolver#resolver(java.lang.String)
     */
    @Override
    public String resolver(String imageSrc){
        if (isNullOrEmpty(imageSrc)){
            return EMPTY;
        }

        if (imageSrc.indexOf("version=") != -1){
            return imageSrc;
        }

        //---------------------------------------------------------------------

        //以 #{imgbase}开头,那么不动, 后面有替换
        //since 5.3.2.22
        if (imageSrc.startsWith(DefaultCmsHtmlReplaceResolver.IMG_BASE_PLACE_HOLDER)){
            return imageSrc + "?" + VERSION;
        }

        //---------------------------------------------------------------------

        //以 / 开头
        if (imageSrc.startsWith("/")){
            return DefaultCmsHtmlReplaceResolver.STATIC_BASE_PLACE_HOLDER + imageSrc + "?" + VERSION;
        }

        //不是绝对地址
        if (!URIUtil.create(imageSrc).isAbsolute()){
            return DefaultCmsHtmlReplaceResolver.STATIC_BASE_PLACE_HOLDER + "/" + imageSrc + "?" + VERSION;
        }

        // 以 imgbase 开头
        if (imageSrc.startsWith(UPLOAD_IMG_DOMAIN)){
            return DefaultCmsHtmlReplaceResolver.IMG_BASE_PLACE_HOLDER + "/" + imageSrc.replace(UPLOAD_IMG_DOMAIN, "") + "?" + VERSION;
        }
        return imageSrc;
    }
}
