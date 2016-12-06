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
package com.baozun.nebula.web.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baozun.nebula.command.EmailCommand;
import com.baozun.nebula.sdk.utils.CookieUtil;
import com.baozun.nebula.utilities.common.LangUtil;
import com.baozun.nebula.utils.history.FilterHistoryUtils;
import com.baozun.nebula.web.command.BackWarnEntity;

@Controller
public class CommonController extends BaseController{

    private static final Logger log = LoggerFactory.getLogger(CommonController.class);

    @RequestMapping(value = "/errors/{errorCode}")
    public String errors(@PathVariable("errorCode") String errorCode){
        return "errors/" + errorCode;
    }

    @RequestMapping(value = "/error/mail.htm")
    @ResponseBody
    public String errorMail(@RequestParam("errorCode") String errorCode,@RequestParam("error") String error,@RequestParam("msg") String msg){
        EmailCommand email = new EmailCommand();
        email.setAddress("xx@qq.com");
        email.setSubject("[" + errorCode + "] [" + error + "]");
        email.setContent(msg);
        return "1";
    }

    @RequestMapping(value = "/test.htm")
    public void test(HttpServletResponse response){

    }

    @RequestMapping(value = "/common/saveSearchFilter.json")
    @ResponseBody
    public BackWarnEntity saveSearchFilter(HttpServletRequest request){
        FilterHistoryUtils.saveFilterParam(request);
        return SUCCESS;
    }

    @RequestMapping(value = "/common/readSearchFilter.json")
    @ResponseBody
    public Map<String, String> readSearchFilter(HttpServletRequest request){
        Map<String, String> paramMap = FilterHistoryUtils.readFilterParamMap(request);
        return paramMap;
    }

    @RequestMapping(value = "/common/clearSearchFilter.json")
    @ResponseBody
    public BackWarnEntity clearSearchFilter(HttpServletRequest request){
        FilterHistoryUtils.clearFilterParam(request);
        return SUCCESS;
    }

    /**
     * 
     * @author 何波
     * @Description:设置语言
     * @param request
     * @param response
     * @param lang
     * @return
     * BackWarnEntity
     * @throws
     */
    @RequestMapping("/i18n/switchI18nLang.json")
    @ResponseBody
    public BackWarnEntity switchI18nLocale(HttpServletRequest request,HttpServletResponse response,String lang){
        if (lang != null){
            CookieUtil.setCookie(request, response, LangUtil.I18_LANG_KEY, lang);
        }else{
            CookieUtil.setCookie(request, response, LangUtil.I18_LANG_KEY, LangUtil.ZH_CN);
        }
        return SUCCESS;
    }
}
