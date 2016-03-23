/**
 * 
 */
package com.baozun.nebula.web.filter;

import java.util.Properties;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baozun.nebula.command.i18n.LangProperty;
import com.baozun.nebula.sdk.manager.SdkI18nLangManager;
import com.baozun.nebula.solr.utils.Validator;
import com.baozun.nebula.utilities.common.ProfileConfigUtil;
import com.feilong.servlet.http.CookieUtil;


/**
 * @author xianze.zhang
 *@creattime 2013-7-22
 */
public class CookieI18nUrlDispatcherFilter extends AbstractI18nUrlDispatcherFilter{
	
	private static final Logger logger = LoggerFactory.getLogger(CookieI18nUrlDispatcherFilter.class);
	
	private static final String DEFAULT_COOKIE_NAME = "clientlanguage";
	
	private static final String I18N_LANG_COOKIE_NAME = "i18n.lang.cookieName";
	
	
	@Override
	protected String getLang(HttpServletRequest request, HttpServletResponse response) {
			
		// 是否启用多语言 
		boolean i18nOnOff = LangProperty.getI18nOnOff();
		if(i18nOnOff) {
			
			SdkI18nLangManager sdkI18nLangManager = getSdkI18nLangManager();
			
			String newLocale = getNewLocale(request);
			if(Validator.isNotNullOrEmpty(newLocale) && sdkI18nLangManager.isExistLang(newLocale)) {
				//把新的语言设置到cookie中
				CookieUtil.addCookie(getCookieName(), newLocale, response);
				return newLocale;
			}
			
			Cookie cookie = CookieUtil.getCookie(request, getCookieName());
			if (cookie != null) {
				String lang = cookie.getValue();
				logger.debug("Geted lang from cookie name [{}] value [{}]", getCookieName(), lang);
				
				String langValue = sdkI18nLangManager.getCurrentLangValue(lang);
				if(Validator.isNotNullOrEmpty(langValue)) {
					//如果配置中存在该语言， 就返回该语言
					return lang;
				}
			}
			
			return sdkI18nLangManager.getDefaultlang();
		}
		
		return null;
	}
	
	private String getCookieName() {
		Properties properties = ProfileConfigUtil.findCommonPro("config/common.properties");
		return properties.getProperty(I18N_LANG_COOKIE_NAME, DEFAULT_COOKIE_NAME);
	}
	
}
