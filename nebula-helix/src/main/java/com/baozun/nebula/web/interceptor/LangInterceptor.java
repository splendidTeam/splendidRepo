package com.baozun.nebula.web.interceptor;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.support.RequestContextUtils;

import com.baozun.nebula.command.i18n.LangProperty;
import com.baozun.nebula.model.i18n.I18nLang;
import com.baozun.nebula.sdk.manager.SdkI18nLangManager;
import com.baozun.nebula.utilities.common.LangUtil;
import com.baozun.nebula.utils.Validator;
import com.baozun.nebula.utils.cache.GuavaAbstractLoadingCache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 * 
 * @Description: 需要配置到spring国际化拦截器之后
 * @author 何波
 * @date 2015年1月15日 上午11:29:50
 *
 */
public class LangInterceptor extends HandlerInterceptorAdapter {

    public static final Logger log = LoggerFactory.getLogger(LangInterceptor.class);

    private static final String I18N_LANGS = "i18nLangs";

    private static final String CURRENT_LANG_KEY = "currentLangKey";

    private static final String CURRENT_LANG_VALUE = "currentLangValue";

    @Autowired
    private SdkI18nLangManager sdkI18nLangManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        List<I18nLang> i18nLangList = langListCache.getValue("i18nLang");

        // 是否启用多语言 
        boolean i18nOnOff = langSwitchCache.getValue("switch").booleanValue();
        request.setAttribute("i18nOnOff", i18nOnOff);

        LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);

        Locale locale = RequestContextUtils.getLocale(request);

        String lang = locale.toString();
        String defaultLang = langCache.getValue("default");
        if (i18nOnOff){
            // 如果当前Lang已经不存在
            String langValue = langCache.getValue(lang);
            if (Validator.isNullOrEmpty(langValue)){
                lang = defaultLang;
                langValue = langCache.getValue(lang);

                localeResolver.setLocale(request, response, StringUtils.parseLocaleString(lang));
            }

            LangUtil.setCurrentLang(lang);

            // 默认语言
            request.setAttribute("defaultlang", defaultLang);
            // 多语言信息
            request.setAttribute(I18N_LANGS, i18nLangList);
            // 当前语言
            request.setAttribute(CURRENT_LANG_KEY, lang);
            // 当前语言名称
            request.setAttribute(CURRENT_LANG_VALUE, langValue);
        }else{
            request.setAttribute(CURRENT_LANG_KEY, defaultLang);
            request.setAttribute(CURRENT_LANG_VALUE, langCache.getValue(lang));
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 清除国际化信息
        LangUtil.remove();
    }

    private GuavaAbstractLoadingCache<String, String> langCache = new GuavaAbstractLoadingCache<String, String>() {
        @Override
        protected String fetchData(String key) {
            if("default".equals(key)) {
                return sdkI18nLangManager.getDefaultlang();
            } else {
                return sdkI18nLangManager.getCurrentLangValue(key);
            }
        }
    };

    private GuavaAbstractLoadingCache<String,  List<I18nLang>> langListCache = new GuavaAbstractLoadingCache<String,  List<I18nLang>>() {
        @Override
        protected  List<I18nLang> fetchData(String key) {
            return  sdkI18nLangManager.geti18nLangCache();
        }
    };
    
    private GuavaAbstractLoadingCache<String, Boolean> langSwitchCache = new GuavaAbstractLoadingCache<String, Boolean>() {
        @Override
        protected Boolean fetchData(String key) {
            return LangProperty.getI18nOnOff();
        }
    };
}
