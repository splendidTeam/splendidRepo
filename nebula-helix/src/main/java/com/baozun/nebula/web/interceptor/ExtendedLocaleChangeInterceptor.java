package com.baozun.nebula.web.interceptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.manager.system.ChooseOptionManager;
import com.baozun.nebula.model.i18n.I18nLang;
import com.baozun.nebula.model.system.ChooseOption;
import com.baozun.nebula.sdk.manager.SdkI18nLangManager;

/**
 * 扩展spring默认地区切换拦截器
 * 
 * @author yimin.qiao
 * 
 */
public class ExtendedLocaleChangeInterceptor extends LocaleChangeInterceptor {
    
	private String paramName = DEFAULT_PARAM_NAME;
	
	/**
	 * 网站的语言列表
	 */
	private List<String> langs;
	
	/**
	 * 语言的别名，有的网站会对显示在url中的语言有要求，可以通过别名映射到实际的语言
	 */
	private Map<String, String> langAlias;
	
	@Autowired
	private SdkI18nLangManager sdkI18nLangManager;
	
	@Autowired
	private ChooseOptionManager chooseOptionManager;
	
	private AtomicBoolean inited = new AtomicBoolean(false);
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

	    /**
	     * 只初始化一次，无论langs和langAlias是否有值
	     */
	    if(!inited.get()) {
    		//初始化langs
    		initLangs();
    		//初始化langAlias
    		initLangAlias();
    		inited.set(true);
	    }
		
		String newLocale = null;
		try {
			newLocale = request.getParameter(this.paramName);
			
			if(langAlias != null && langAlias.containsKey(newLocale)) {
				newLocale = langAlias.get(newLocale);
			}
			
		} catch (Exception e) {
			throw new BusinessException("Get local paramName error.");
		}
		
		if (newLocale != null) {
			if (CollectionUtils.isNotEmpty(langs))
				if (langs.contains(newLocale)) {
					LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
					if (localeResolver == null)
						throw new IllegalStateException("No LocaleResolver found: not in a DispatcherServlet request?");
					localeResolver.setLocale(request, response, StringUtils.parseLocaleString(newLocale));
				}
		}
		return true;
	}

	public List<String> getLangs() {
		return langs;
	}

	public void setLangs(List<String> langs) {
		this.langs = langs;
	}

	public Map<String, String> getLangAlias() {
		return langAlias;
	}

	public void setLangAlias(Map<String, String> langAlias) {
		this.langAlias = langAlias;
	}
	
	/**
	 * 初始化langs 
	 */
	public void initLangs() {
		if(langs == null || langs.size() == 0) {
			List<I18nLang> i18nLangCache = sdkI18nLangManager.geti18nLangCache();
			if(i18nLangCache != null && i18nLangCache.size() > 0) {
				langs = new ArrayList<String>(i18nLangCache.size());
				for(I18nLang i18nLang : i18nLangCache) {
					langs.add(i18nLang.getKey());
				}
			}
		}
	}
	
	/**
	 * 初始化langAlias
	 */
	public void initLangAlias() {
		if(langAlias == null || langAlias.isEmpty()) {
			List<ChooseOption>  chooseOptions = chooseOptionManager.findEffectChooseOptionListByGroupCode("I18N_LANG_ALIAS");
			if(chooseOptions != null && chooseOptions.size() > 0) {
				langAlias = new HashMap<String, String>();
				for(ChooseOption co : chooseOptions) {
					langAlias.put(co.getOptionLabel(), co.getOptionValue());
				}
			}
		}
	}
}
