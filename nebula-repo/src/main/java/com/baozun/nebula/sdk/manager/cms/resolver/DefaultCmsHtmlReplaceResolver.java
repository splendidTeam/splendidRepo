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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.feilong.tools.jsonlib.JsonUtil;

import static com.feilong.core.bean.ConvertUtil.toMapUseEntrys;

/**
 * The Class DefaultCmsHtmlReplaceResolver.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.22
 */
@Transactional
@Service("cmsHtmlReplaceResolver")
public class DefaultCmsHtmlReplaceResolver implements CmsHtmlReplaceResolver{

    /** The Constant log. */
    private final static Logger LOGGER = LoggerFactory.getLogger(DefaultCmsHtmlReplaceResolver.class);

    //---------------------------------------------------------------------

    /** 静态base标识. */
    public final static String STATIC_BASE_PLACE_HOLDER = "#{staticbase}";

    /** 页面base标识. */
    public final static String PAGE_BASE_PLACE_HOLDER = "#{pagebase}";

    /** 图片base标识. */
    public final static String IMG_BASE_PLACE_HOLDER = "#{imgbase}";

    //---------------------------------------------------------------------

    /** 上传图片的域名. */
    @Value("#{meta['upload.img.domain.base']}")
    private String UPLOAD_IMG_DOMAIN = "";

    /** The page url base. */
    @Value("#{meta['page.base']}")
    private String pageUrlBase = "";

    /** The static base. */
    @Value("#{meta['static.domain.base']}")
    private String staticBase = "";

    //---------------------------------------------------------------------

    /** The map. */
    private Map<String, String> formatUrlMap;

    //---------------------------------------------------------------------

    /**
     * Post construct.
     */
    @PostConstruct
    protected void postConstruct(){
        Map<String, String> map = new HashMap<>();
        map.put(STATIC_BASE_PLACE_HOLDER, format(staticBase));
        map.put(IMG_BASE_PLACE_HOLDER, format(UPLOAD_IMG_DOMAIN));
        map.put(PAGE_BASE_PLACE_HOLDER, format(pageUrlBase));

        formatUrlMap = Collections.unmodifiableMap(map);
        if (LOGGER.isInfoEnabled()){
            LOGGER.info(
                            "init config ,[{}],formatUrlMap:[{}]",
                            JsonUtil.format(toMapUseEntrys(//
                                            Pair.of("upload.img.domain.base", UPLOAD_IMG_DOMAIN),
                                            Pair.of("page.base", pageUrlBase),
                                            Pair.of("static.domain.base", staticBase))),

                            JsonUtil.format(formatUrlMap));

        }
    }

    //---------------------------------------------------------------------

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.cms.resolver.CmsHtmlReplaceResolver#resolver(java.lang.String)
     */
    @Override
    public String resolver(String html){
        if (StringUtils.isBlank(html)){
            return EMPTY;
        }

        //---------------------------------------------------------------------

        String result = html;
        for (Map.Entry<String, String> entry : formatUrlMap.entrySet()){
            String key = entry.getKey();
            String value = entry.getValue();

            if (LOGGER.isDebugEnabled()){
                LOGGER.debug("will replace html ,use :[{}] ,replace :[{}]", value, key);
            }
            result = result.replace(key, value);
        }

        //---------------------------------------------------------------------
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("html:[{}],return:[{}]", html, result);
        }
        return result;
    }

    /**
     * Format.
     *
     * @param url
     *            the url
     * @return the string
     */
    private static String format(String url){
        if (StringUtils.isBlank(url)){
            return EMPTY;
        }
        if ("/".equals(url)){
            return EMPTY;
        }
        if (url.endsWith("/")){
            return url.substring(0, url.length() - 1);
        }
        return url;
    }

}
