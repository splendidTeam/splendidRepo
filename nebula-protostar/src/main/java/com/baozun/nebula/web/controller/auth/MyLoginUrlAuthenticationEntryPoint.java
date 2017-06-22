/**
 * Copyright (c) 2012 Baozun All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Baozun.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Baozun.
 *
 * BAOZUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. BAOZUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */

package com.baozun.nebula.web.controller.auth;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import loxia.support.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.PortMapper;
import org.springframework.security.web.PortMapperImpl;
import org.springframework.security.web.PortResolver;
import org.springframework.security.web.PortResolverImpl;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.RedirectUrlBuilder;
import org.springframework.security.web.util.UrlUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.exception.ErrorCodes;
import com.feilong.servlet.http.RequestUtil;

/**
 * Used by the {@link ExceptionTranslationFilter} to commence a form login authentication via the
 * {@link UsernamePasswordAuthenticationFilter}.
 * <p>
 * Holds the location of the login form in the {@code loginFormUrl} property, and uses that to build a redirect URL to the login page.
 * Alternatively, an absolute URL can be set in this property and that will be used exclusively.
 * <p>
 * When using a relative URL, you can set the {@code forceHttps} property to true, to force the protocol used for the login form to be
 * {@code HTTPS}, even if the original intercepted request for a resource used the {@code HTTP} protocol. When this happens, after a
 * successful login (via HTTPS), the original resource will still be accessed as HTTP, via the original request URL. For the forced HTTPS
 * feature to work, the {@link PortMapper} is consulted to determine the HTTP:HTTPS pairs. The value of {@code forceHttps} will have no
 * effect if an absolute URL is used.
 * 
 * @author songdianchao
 * @author Ben Alex
 * @author colin sampaleanu
 * @author Omri Spector
 * @author Luke Taylor
 * @since 3.0 添加了未登录的切入点，对ajax调用的处理
 */
public class MyLoginUrlAuthenticationEntryPoint implements AuthenticationEntryPoint,InitializingBean{

	// ~ Static fields/initializers
	// =====================================================================================

	private static final Log		logger				= LogFactory.getLog(MyLoginUrlAuthenticationEntryPoint.class);

	// ~ Instance fields
	// ================================================================================================

	private PortMapper				portMapper			= new PortMapperImpl();

	private PortResolver			portResolver		= new PortResolverImpl();

	private String					loginFormUrl;

	private boolean					forceHttps			= false;

	private boolean					useForward			= false;

	private final RedirectStrategy	redirectStrategy	= new DefaultRedirectStrategy();

	@Autowired
	private MessageSource			messageSource;

	/**
	 * @deprecated Use constructor injection
	 */
	@Deprecated
	public MyLoginUrlAuthenticationEntryPoint(){}

	/**
	 * @param loginFormUrl
	 *            URL where the login page can be found. Should either be relative to the web-app context path (include a leading {@code /})
	 *            or an absolute URL.
	 */
	public MyLoginUrlAuthenticationEntryPoint(String loginFormUrl){
		this.loginFormUrl = loginFormUrl;
	}

	// ~ Methods
	// ========================================================================================================

	public void afterPropertiesSet() throws Exception{
		Assert.isTrue(
				StringUtils.hasText(loginFormUrl) && UrlUtils.isValidRedirectUrl(loginFormUrl),
				"loginFormUrl must be specified and must be a valid redirect URL");
		if (useForward && UrlUtils.isAbsoluteUrl(loginFormUrl)){
			throw new IllegalArgumentException("useForward must be false if using an absolute loginFormURL");
		}
		Assert.notNull(portMapper, "portMapper must be specified");
		Assert.notNull(portResolver, "portResolver must be specified");
	}

	/**
	 * Allows subclasses to modify the login form URL that should be applicable for a given request.
	 * 
	 * @param request
	 *            the request
	 * @param response
	 *            the response
	 * @param exception
	 *            the exception
	 * @return the URL (cannot be null or empty; defaults to {@link #getLoginFormUrl()})
	 */
	protected String determineUrlToUseForThisRequest(
			HttpServletRequest request,
			HttpServletResponse response,
			AuthenticationException exception){

		return getLoginFormUrl();
	}

	/**
	 * Performs the redirect (or forward) to the login form URL.
	 */
	public void commence(HttpServletRequest request,HttpServletResponse response,AuthenticationException authException) throws IOException,
			ServletException{
		if (RequestUtil.isAjaxRequest(request)){
			Map<String, Object> exceptionMap = new HashMap<String, Object>();
			Map<String, Map<String, Object>> result = new HashMap<String, Map<String, Object>>(1);
			exceptionMap.put("statusCode", ErrorCodes.INVALID_SESSION);
			exceptionMap.put(
					"message",
					messageSource.getMessage(
							ErrorCodes.BUSINESS_EXCEPTION_PREFIX + ErrorCodes.INVALID_SESSION,
							null,
							LocaleContextHolder.getLocale()));
			response.setContentType("application/json");
			response.setHeader("Content-Disposition", "inline");
			response.setCharacterEncoding("utf-8");
			PrintWriter writer = response.getWriter();
			result.put("exception", exceptionMap);
			writer.write(new ObjectMapper().writeValueAsString(result));
			if (writer != null){
				writer.flush();
				writer.close();
			}
			response.flushBuffer();
			return;
		}else{
			String redirectUrl = null;

			if (useForward){

				if (forceHttps && "http".equals(request.getScheme())){
					// First redirect the current request to HTTPS.
					// When that request is received, the forward to the login
					// page will be used.
					redirectUrl = buildHttpsRedirectUrlForRequest(request);
				}

				if (redirectUrl == null){
					String loginForm = determineUrlToUseForThisRequest(request, response, authException);

					if (logger.isDebugEnabled()){
						logger.debug("Server side forward to: " + loginForm);
					}

					RequestDispatcher dispatcher = request.getRequestDispatcher(loginForm);

					dispatcher.forward(request, response);

					return;
				}
			}else{
				// redirect to login page. Use https if forceHttps true

				redirectUrl = buildRedirectUrlToLoginPage(request, response, authException);

			}

			redirectStrategy.sendRedirect(request, response, redirectUrl);
		}
	}

	protected String buildRedirectUrlToLoginPage(
			HttpServletRequest request,
			HttpServletResponse response,
			AuthenticationException authException){

		String loginForm = determineUrlToUseForThisRequest(request, response, authException);

		if (UrlUtils.isAbsoluteUrl(loginForm)){
			return loginForm;
		}

		int serverPort = portResolver.getServerPort(request);
		String scheme = request.getScheme();

		RedirectUrlBuilder urlBuilder = new RedirectUrlBuilder();

		urlBuilder.setScheme(scheme);
		urlBuilder.setServerName(request.getServerName());
		urlBuilder.setPort(serverPort);
		urlBuilder.setContextPath(request.getContextPath());
		urlBuilder.setPathInfo(loginForm);

		if (forceHttps && "http".equals(scheme)){
			Integer httpsPort = portMapper.lookupHttpsPort(Integer.valueOf(serverPort));

			if (httpsPort != null){
				// Overwrite scheme and port in the redirect URL
				urlBuilder.setScheme("https");
				urlBuilder.setPort(httpsPort.intValue());
			}else{
				logger.warn("Unable to redirect to HTTPS as no port mapping found for HTTP port " + serverPort);
			}
		}

		return urlBuilder.getUrl();
	}

	/**
	 * Builds a URL to redirect the supplied request to HTTPS. Used to redirect the current request to HTTPS, before doing a forward to the
	 * login page.
	 */
	protected String buildHttpsRedirectUrlForRequest(HttpServletRequest request) throws IOException,ServletException{

		int serverPort = portResolver.getServerPort(request);
		Integer httpsPort = portMapper.lookupHttpsPort(Integer.valueOf(serverPort));

		if (httpsPort != null){
			RedirectUrlBuilder urlBuilder = new RedirectUrlBuilder();
			urlBuilder.setScheme("https");
			urlBuilder.setServerName(request.getServerName());
			urlBuilder.setPort(httpsPort.intValue());
			urlBuilder.setContextPath(request.getContextPath());
			urlBuilder.setServletPath(request.getServletPath());
			urlBuilder.setPathInfo(request.getPathInfo());
			urlBuilder.setQuery(request.getQueryString());

			return urlBuilder.getUrl();
		}

		// Fall through to server-side forward with warning message
		logger.warn("Unable to redirect to HTTPS as no port mapping found for HTTP port " + serverPort);

		return null;
	}

	/**
	 * Set to true to force login form access to be via https. If this value is true (the default is false), and the incoming request for
	 * the protected resource which triggered the interceptor was not already <code>https</code>, then the client will first be redirected
	 * to an https URL, even if <tt>serverSideRedirect</tt> is set to <tt>true</tt>.
	 */
	public void setForceHttps(boolean forceHttps){
		this.forceHttps = forceHttps;
	}

	protected boolean isForceHttps(){
		return forceHttps;
	}

	/**
	 * The URL where the <code>UsernamePasswordAuthenticationFilter</code> login page can be found. Should either be relative to the web-app
	 * context path (include a leading {@code /}) or an absolute URL.
	 * 
	 * @deprecated use constructor injection
	 */
	@Deprecated
	public void setLoginFormUrl(String loginFormUrl){
		this.loginFormUrl = loginFormUrl;
	}

	public String getLoginFormUrl(){
		return loginFormUrl;
	}

	public void setPortMapper(PortMapper portMapper){
		this.portMapper = portMapper;
	}

	protected PortMapper getPortMapper(){
		return portMapper;
	}

	public void setPortResolver(PortResolver portResolver){
		this.portResolver = portResolver;
	}

	protected PortResolver getPortResolver(){
		return portResolver;
	}

	/**
	 * Tells if we are to do a forward to the {@code loginFormUrl} using the {@code RequestDispatcher}, instead of a 302 redirect.
	 * 
	 * @param useForward
	 *            true if a forward to the login page should be used. Must be false (the default) if {@code loginFormUrl} is set to an
	 *            absolute value.
	 */
	public void setUseForward(boolean useForward){
		this.useForward = useForward;
	}

	protected boolean isUseForward(){
		return useForward;
	}
}
