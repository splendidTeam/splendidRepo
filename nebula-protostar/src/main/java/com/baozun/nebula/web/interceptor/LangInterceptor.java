package com.baozun.nebula.web.interceptor;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.baozun.nebula.command.i18n.LangProperty;
import com.baozun.nebula.model.i18n.I18nLang;
import com.baozun.nebula.sdk.manager.SdkI18nLangManager;
import com.baozun.nebula.sdk.utils.CookieUtil;
import com.baozun.nebula.utilities.common.LangUtil;

public class LangInterceptor extends HandlerInterceptorAdapter {
	public static final Logger	log			= LoggerFactory.getLogger(LangInterceptor.class);

	@Autowired
	private SdkI18nLangManager	sdkI18nLangManager;

	private static final String	I18N_LANGS	= "i18nLangs";

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		Cookie cookie = CookieUtil.getCookie(request, LangUtil.I18_LANG_KEY);
		List<I18nLang> i18nLangCache = sdkI18nLangManager.geti18nLangCache();
		String defaultLang = sdkI18nLangManager.getDefaultlang();
		if (cookie != null) {
			String lang = cookie.getValue();

			if (lang != null) {
				LangUtil.setCurrentLang(lang);
			} else {
				LangUtil.setCurrentLang(defaultLang);
			}
		} else {
			LangUtil.setCurrentLang(defaultLang);
		}
		boolean i18nOnOff = LangProperty.getI18nOnOff();
		// 是否启用多语言
		request.setAttribute("i18nOnOff", i18nOnOff);
		// 默认语言
		request.setAttribute("defaultlang", defaultLang);
		// 多语言信息
		request.setAttribute(I18N_LANGS, i18nLangCache);
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// 清除国际化信息
		LangUtil.remove();
	}
}
