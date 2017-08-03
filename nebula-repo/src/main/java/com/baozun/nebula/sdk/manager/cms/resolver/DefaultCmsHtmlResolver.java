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

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * The Class DefaultCmsSrcResolver.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.22
 */
@Component("cmsHtmlResolver")
public class DefaultCmsHtmlResolver implements CmsHtmlResolver{

    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultCmsHtmlResolver.class);

    /** The cms image src resolver. */
    @Autowired
    private CmsImageSrcResolver cmsImageSrcResolver;

    /** The cms anchor href resolver. */
    @Autowired
    private CmsAnchorHrefResolver cmsAnchorHrefResolver;

    /** 静态base标识. */
    private final static String STATIC_BASE_CHAR = "#{staticbase}";

    /** version. */
    private final static String VERSION = "version=000000";

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.cms.builder.CmsSrcResolver#resolver(java.lang.String)
     */
    @Override
    public String resolver(String html){
        if (StringUtils.isBlank(html)){
            return EMPTY;
        }

        Document document = Jsoup.parse(html);

        //---------------------------------------------------------------------
        //process img
        Elements imgs = document.select("img");
        for (Element element : imgs){
            String src = element.attr("src");
            String result = cmsImageSrcResolver.resolver(src);

            if (LOGGER.isDebugEnabled()){
                LOGGER.debug("element:[{}], attr:[{}], result:{}", element.toString(), src, result);
            }
            element.attr("src", result);
        }

        //PROCESS a
        Elements as = document.select("a");
        for (Element element : as){
            String attr = element.attr("href");
            String result = cmsAnchorHrefResolver.resolver(attr);

            if (LOGGER.isDebugEnabled()){
                LOGGER.debug("element:[{}], attr:[{}], result:{}", element.toString(), attr, result);
            }

            element.attr("href", result);
        }
        //---------------------------------------------------------------------

        //process script
        Elements scripts = document.select("script");
        for (Element element : scripts){
            String src = element.attr("src");
            if (StringUtils.isBlank(src)){
                continue;
            }else if (src.startsWith("/")){
                src = STATIC_BASE_CHAR + src + "?" + VERSION;
            }else if (!src.startsWith("http")){
                src = STATIC_BASE_CHAR + "/" + src + "?" + VERSION;
            }
            element.attr("src", src);
        }

        //process css
        Elements links = document.select("link");
        for (Element element : links){
            String href = element.attr("href");
            if (StringUtils.isBlank(href)){
                continue;
            }else if (href.startsWith("/")){
                href = STATIC_BASE_CHAR + href + "?" + VERSION;
            }else if (!href.startsWith("http")){
                href = STATIC_BASE_CHAR + "/" + href + "?" + VERSION;
            }
            element.attr("href", href);
        }

        String string = document.toString();
        if (LOGGER.isDebugEnabled()){
            LOGGER.debug("final result:{}", string);
        }

        return string;
    }
}
