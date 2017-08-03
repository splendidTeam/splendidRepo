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
package com.baozun.nebula.sdk.manager.cms.builder;

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

    /** 静态base标识. */
    private final static String STATIC_BASE_CHAR = "#{staticbase}";

    /** 图片base标识. */
    private final static String IMG_BASE_CHAR = "#{imgbase}";

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

        //以 / 开头
        if (imageSrc.startsWith("/")){
            return STATIC_BASE_CHAR + imageSrc + "?" + VERSION;
        }

        //不是绝对地址
        if (!URIUtil.create(imageSrc).isAbsolute()){
            return STATIC_BASE_CHAR + "/" + imageSrc + "?" + VERSION;
        }

        // 以 imgbase 开头
        if (imageSrc.startsWith(UPLOAD_IMG_DOMAIN)){
            return IMG_BASE_CHAR + "/" + imageSrc.replace(UPLOAD_IMG_DOMAIN, "") + "?" + VERSION;
        }
        return imageSrc;
    }
}
