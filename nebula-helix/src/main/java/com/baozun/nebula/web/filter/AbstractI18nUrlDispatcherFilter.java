/**
 * 
 */
package com.baozun.nebula.web.filter;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.baozun.nebula.manager.cms.PublishCmsPageInstanceManager;
import com.baozun.nebula.model.cms.CmsPageInstance;
import com.baozun.nebula.sdk.manager.SdkI18nLangManager;
import com.baozun.nebula.sdk.manager.cms.SdkCmsPageInstanceManagerImpl;
import com.baozun.nebula.solr.utils.Validator;
import com.baozun.nebula.utilities.common.ProfileConfigUtil;

/**
 * @author xianze.zhang
 * @creattime 2013-7-22
 * @deprecated by D.C
 */
public abstract class AbstractI18nUrlDispatcherFilter implements Filter {

	private Log log = LogFactory.getLog(getClass());

	private static final String DEFAULT_LOCALE_PARAM_NAME = "locale";

	private static final String I18N_LOCALE_PARAM_NAME = "i18n.lang.localeParamName";

	private WebApplicationContext webApplicationContext;

	private PublishCmsPageInstanceManager cmsPageInstanceManager;

	private SdkI18nLangManager sdkI18nLangManager;

	/**
	 * 支持的类型 0:综合 ,1:pc,2:mobile
	 */
	private static String supportType;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		ServletContext servletContext = filterConfig.getServletContext();

		webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);

		cmsPageInstanceManager = (PublishCmsPageInstanceManager) webApplicationContext
				.getBean(PublishCmsPageInstanceManager.class);
		sdkI18nLangManager = (SdkI18nLangManager) webApplicationContext.getBean(SdkI18nLangManager.class);

		supportType = filterConfig.getInitParameter("supportType");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");

		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;

		String path = httpServletRequest.getRequestURI();

		Map<String, CmsPageInstance> urlMap = SdkCmsPageInstanceManagerImpl.urlMap;

		String key = path;
		String lang = getLang(httpServletRequest, httpServletResponse);
		String i18nKey = getI18nPath(lang, path);

		if (supportType != null) {
			key = path + "-" + supportType;
			i18nKey = i18nKey + "-" + supportType;
		}

		CmsPageInstance pageInstance = urlMap.get(i18nKey);
		if (pageInstance == null) {
			log.debug("1.cms信息未找到:" + "[" + i18nKey + "]");
			pageInstance = urlMap.get(i18nKey + "-0");

			if (pageInstance == null && !key.equals(i18nKey)) {
				log.debug("2.cms信息未找到:" + "[" + i18nKey + "-0" + "]");
				pageInstance = urlMap.get(key);

				if (pageInstance == null) {
					log.debug("3.cms信息未找到:" + "[" + key + "]");
					pageInstance = urlMap.get(key + "-0");

					if (pageInstance == null) {
						log.debug("4.cms信息未找到:" + "[" + key + "-0" + "]");
					}
				}
			}
		}

		// 包含自定义的url
		if (pageInstance != null && dealPublish(pageInstance)) {

			Map<String, String> result = cmsPageInstanceManager.findPublishPage(pageInstance);

			request.setAttribute("page", pageInstance);
			request.setAttribute("data", result.get("data"));
			request.setAttribute("resource", result.get("resource"));
			if (result.get("useCommonHeader").equals("true")) {
				log.debug("useCommonHeader:" + path);
				request.getRequestDispatcher("/pages/commons/cms-page.jsp").forward(request, response);
			} else {
				log.debug("no-useCommonHeader:" + path);
				request.getRequestDispatcher("/pages/commons/cms-page-nocommon.jsp").forward(request, response);
			}
		} else {
			chain.doFilter(request, response);
		}
	}

	/**
	 * 
	 * @author 何波 @Description: 处理是否到正确的发布时间 void @throws
	 */
	private boolean dealPublish(CmsPageInstance pageInstance) {
		Date stime = pageInstance.getStartTime();
		Date etime = pageInstance.getEndTime();

		if (stime != null && stime.compareTo(new Date()) > 0) {
			return false;
		}
		if (etime != null && etime.compareTo(new Date()) < 0) {
			return false;
		}

		return true;

	}

	@Override
	public void destroy() {

	}

	private String getI18nPath(String lang, String path) {

		String i18nPath = path;

		if (Validator.isNotNullOrEmpty(i18nPath) && Validator.isNotNullOrEmpty(lang)) {
			i18nPath = path.concat("-").concat(lang);
		}

		return i18nPath;
	}

	/**
	 * 获取当前语言
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	protected abstract String getLang(HttpServletRequest request, HttpServletResponse response);

	/**
	 * 获取webApplicationContext
	 * 
	 * @return
	 */
	public WebApplicationContext getWebApplicationContext() {
		return webApplicationContext;
	}

	/**
	 * 获取 sdkI18nLangManager
	 * 
	 * @return
	 */
	public SdkI18nLangManager getSdkI18nLangManager() {
		return sdkI18nLangManager;
	}

	/**
	 * 取得通过url设置的语言
	 * 
	 * @param request
	 * @return
	 */
	public String getNewLocale(HttpServletRequest request) {
		return request.getParameter(getLocaleParameterName());
	}

	/**
	 * 取得通过url设置语言时的参数名称
	 * 
	 * @return
	 */
	public String getLocaleParameterName() {
		Properties properties = ProfileConfigUtil.findCommonPro("config/common.properties");
		return properties.getProperty(I18N_LOCALE_PARAM_NAME, DEFAULT_LOCALE_PARAM_NAME);
	}
}
