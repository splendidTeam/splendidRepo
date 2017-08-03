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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultCmsImageSrcResolver.class);

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
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("will do with imageSrc:[{}]", imageSrc);
        }

        if (isNullOrEmpty(imageSrc)){
            return EMPTY;
        }

        //---------------------------------------------------------------------

        imageSrc = imageSrc.trim();

        if (imageSrc.indexOf("version=") != -1){
            LOGGER.debug("imageSrc:[{}] contains version param,do nothing,return it", imageSrc);
            return imageSrc;
        }

        //---------------------------------------------------------------------

        //以 #{imgbase}开头,那么不动, 后面有替换
        //since 5.3.2.22
        String imageSrcAndVersion = imageSrc + "?" + VERSION;
        if (imageSrc.startsWith(DefaultCmsHtmlReplaceResolver.IMG_BASE_PLACE_HOLDER)){
            LOGGER.debug("imageSrc:[{}] starts with [{}],append version and return:[{}]", imageSrc, DefaultCmsHtmlReplaceResolver.IMG_BASE_PLACE_HOLDER, imageSrcAndVersion);
            return imageSrcAndVersion;
        }

        //---------------------------------------------------------------------

        //以 / 开头
        if (imageSrc.startsWith("/")){
            String result = DefaultCmsHtmlReplaceResolver.STATIC_BASE_PLACE_HOLDER + imageSrcAndVersion;
            LOGGER.debug("imageSrc:[{}] starts with [/],append before with:[{}],return [{}]", imageSrc, DefaultCmsHtmlReplaceResolver.STATIC_BASE_PLACE_HOLDER, result);
            return result;
        }

        //不是绝对地址
        if (!URIUtil.create(imageSrc).isAbsolute()){
            String result = DefaultCmsHtmlReplaceResolver.STATIC_BASE_PLACE_HOLDER + "/" + imageSrcAndVersion;
            LOGGER.debug("imageSrc:[{}] is not isAbsolute,append before with:[{}],return [{}]", imageSrc, DefaultCmsHtmlReplaceResolver.STATIC_BASE_PLACE_HOLDER, result);
            return result;
        }

        // 以 imgbase 开头
        if (imageSrc.startsWith(UPLOAD_IMG_DOMAIN)){
            String result = DefaultCmsHtmlReplaceResolver.IMG_BASE_PLACE_HOLDER + "/" + imageSrc.replace(UPLOAD_IMG_DOMAIN, "") + "?" + VERSION;
            LOGGER.debug("imageSrc:[{}] start with [{}],append before with:[{}],return [{}]", imageSrc, UPLOAD_IMG_DOMAIN, DefaultCmsHtmlReplaceResolver.IMG_BASE_PLACE_HOLDER, result);
            return result;
        }
        return imageSrc;
    }
}
