/**
 * Copyright (c) 2010 Jumbomart All Rights Reserved.
 * 
 * This software is the confidential and proprietary information of Jumbomart.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with
 * Jumbo.
 * 
 * JUMBOMART MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE, OR
 * NON-INFRINGEMENT. JUMBOMART SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY
 * LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES.
 * 
 */
package com.baozun.nebula.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baozun.nebula.dao.member.MemberDao;
import com.baozun.nebula.event.EventPublisher;
import com.baozun.nebula.model.i18n.I18nLang;
import com.baozun.nebula.model.member.Member;
import com.baozun.nebula.sdk.manager.SdkI18nLangManager;
import com.baozun.nebula.sdk.utils.CookieUtil;
import com.baozun.nebula.utilities.common.LangUtil;
import com.baozun.nebula.utils.TokenUtil;
import com.baozun.nebula.utils.Validator;
import com.baozun.nebula.web.MemberDetails;

@Controller
public class CommonController extends BaseController {
	
	private static final Logger log = LoggerFactory.getLogger(CommonController.class);
	
	@Autowired
	EventPublisher publisher;
	@Autowired
	MemberDao memberDao;
//	@Autowired
//	ConditionAudienceManager conditionAudienceManager;
	
	@Autowired
	private SdkI18nLangManager sdkI18nLangManager;


	@RequestMapping(value = "/errors/{errorCode}")
	public String errors(@PathVariable("errorCode") String errorCode) {
		return "errors/" + errorCode;
	}

	/**
	 * 主入口
	 * 
	 * @return
	 */
	@RequestMapping({  "/main.html"})
	public String index(HttpServletRequest request, Model model) {
		MemberDetails userDetails = getUserDetails(request);
		if (userDetails != null) {
			Member member = memberDao.getByPrimaryKey(userDetails.getMemberId());
//			List<CustomMemberGroup> gList = conditionAudienceManager.getModelListByRefer(member);
//			model.addAttribute("tagList", gList);
		}
		return "index";
	}
	
	@RequestMapping(value = "/common/token")
	@ResponseBody
	public String commonToken(HttpServletRequest request) {

		TokenUtil.addToken(request);
		return TokenUtil.queryToken(request);
	}
	
	@RequestMapping("/i18n/switchLang.json")
	@ResponseBody
	public String switchI18nLocale(HttpServletRequest request,
			HttpServletResponse response,String lang) {
		if(lang != null){
			List<I18nLang> i18nLangCache = sdkI18nLangManager.geti18nLangCache();
			if (Validator.isNotNullOrEmpty(i18nLangCache)) {
				for (I18nLang i18nLang : i18nLangCache) {
					String key = i18nLang.getKey();
					if (lang.equals(key)) {
						CookieUtil.setCookie(request, response, LangUtil.I18_LANG_KEY, lang);
						return "success";
					}
				}
			}
		}
		return "failure";
	}
}
