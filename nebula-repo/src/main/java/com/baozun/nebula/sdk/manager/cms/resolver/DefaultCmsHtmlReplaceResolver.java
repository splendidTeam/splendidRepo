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

import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.utilities.common.ProfileConfigUtil;

/**
 * The Class SdkCmsCommonManagerImpl.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 */
@Transactional
@Service("cmsHtmlReplaceResolver")
public class DefaultCmsHtmlReplaceResolver implements CmsHtmlReplaceResolver{

    /** The Constant log. */
    private final static Logger LOGGER = LoggerFactory.getLogger(DefaultCmsHtmlReplaceResolver.class);

    /** 静态base标识. */
    public final static String STATIC_BASE_CHAR = "#{staticbase}";

    /** 页面base标识. */
    public final static String PAGE_BASE_CHAR = "#{pagebase}";

    /** 图片base标识. */
    public final static String IMG_BASE_CHAR = "#{imgbase}";

    /** 上传图片的域名. */
    @Value("#{meta['upload.img.domain.base']}")
    private String UPLOAD_IMG_DOMAIN = "";

    @Override
    public String processTemplateBase(String html){
        if (StringUtils.isBlank(html)){
            return "";
        }
        Properties properties = ProfileConfigUtil.findPro("config/metainfo.properties");

        String pagebase = StringUtils.trim(properties.getProperty("page.base"));

        String staticbase = StringUtils.trim(properties.getProperty("static.domain.base"));

        String imgbase = UPLOAD_IMG_DOMAIN;
        LOGGER.info("pagebase:[{}], staticbase:[{}],imgbase:[{}]", pagebase, staticbase, imgbase);

        if (StringUtils.isBlank(pagebase)){
            pagebase = "";
        }else if ("/".equals(pagebase)){
            pagebase = "";
        }else if (pagebase.endsWith("/")){
            pagebase = pagebase.substring(0, pagebase.length() - 1);
        }

        if (StringUtils.isBlank(staticbase)){
            staticbase = "";
        }else if ("/".equals(staticbase)){
            staticbase = "";
        }else if (staticbase.endsWith("/")){
            staticbase = staticbase.substring(0, staticbase.length() - 1);
        }

        if (StringUtils.isBlank(imgbase)){
            imgbase = "";
        }else if ("/".equals(imgbase)){
            imgbase = "";
        }else if (imgbase.endsWith("/")){
            imgbase = imgbase.substring(0, imgbase.length() - 1);
        }
        html = html.replace(PAGE_BASE_CHAR, pagebase);
        html = html.replace(STATIC_BASE_CHAR, staticbase);
        html = html.replace(IMG_BASE_CHAR, imgbase);
        return html;
    }

}
