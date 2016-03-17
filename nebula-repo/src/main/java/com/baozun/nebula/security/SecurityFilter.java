/**
 * Copyright (c) 2015 Baozun All Rights Reserved.
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
package com.baozun.nebula.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

/**
 * 需要注意ROOT WebApplicationContext 和 sub WebApplicationContext的问题，当使用sub WebApplicationContext时需要在web.xml中配置
 * init-param,name为contextAttribute，value为DispatcherServlet的servletName
 * @author D.C
 * @date 2015年10月9日 下午2:54:49
 */
public class SecurityFilter extends OncePerRequestFilter {

	public static final String DEFAULT_MULTIPART_RESOLVER_BEAN_NAME = "multipartResolver";

	private final MultipartResolver defaultMultipartResolver = new StandardServletMultipartResolver();

	private String multipartResolverBeanName = DEFAULT_MULTIPART_RESOLVER_BEAN_NAME;
	
	private static final String CONTEXT_ATTRIBUTE = "contextAttribute";
	

	/**
	 * Set the bean name of the MultipartResolver to fetch from Spring's root
	 * application context. Default is "filterMultipartResolver".
	 */
	public void setMultipartResolverBeanName(String multipartResolverBeanName) {
		this.multipartResolverBeanName = multipartResolverBeanName;
	}

	/**
	 * Return the bean name of the MultipartResolver to fetch from Spring's root
	 * application context.
	 */
	protected String getMultipartResolverBeanName() {
		return this.multipartResolverBeanName;
	}

	protected void initFilterBean() throws ServletException {
	}

	/**
	 * Check for a multipart request via this filter's MultipartResolver, and
	 * wrap the original request with a MultipartHttpServletRequest if
	 * appropriate.
	 * <p>
	 * All later elements in the filter chain, most importantly servlets,
	 * benefit from proper parameter extraction in the multipart case, and are
	 * able to cast to MultipartHttpServletRequest if they need to.
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		MultipartResolver multipartResolver = lookupMultipartResolver(request);

		HttpServletRequest processedRequest = request;
		if (multipartResolver.isMultipart(processedRequest)) {
			if (logger.isDebugEnabled()) {
				logger.debug(
						"Resolving multipart request [" + processedRequest.getRequestURI() + "] with MultipartFilter");
			}
			processedRequest = multipartResolver.resolveMultipart(processedRequest);
		} else {
			// A regular request...
			if (logger.isDebugEnabled()) {
				logger.debug("Request [" + processedRequest.getRequestURI() + "] is not a multipart request");
			}
			processedRequest = new SecurityWrapperRequest(request);
		}

		try {
			filterChain.doFilter(processedRequest, new SecurityWrapperResponse(response));
		} finally {
			if (processedRequest instanceof MultipartHttpServletRequest) {
				multipartResolver.cleanupMultipart((MultipartHttpServletRequest) processedRequest);
			}
		}
	}

	/**
	 * Look up the MultipartResolver that this filter should use, taking the
	 * current HTTP request as argument.
	 * <p>
	 * The default implementation delegates to the
	 * {@code lookupMultipartResolver} without arguments.
	 * 
	 * @return the MultipartResolver to use
	 * @see #lookupMultipartResolver()
	 */
	protected MultipartResolver lookupMultipartResolver(HttpServletRequest request) {
		return lookupMultipartResolver();
	}

	/**
	 * Look for a MultipartResolver bean in the root web application context.
	 * Supports a "multipartResolverBeanName" filter init param; the default
	 * bean name is "filterMultipartResolver".
	 * <p>
	 * This can be overridden to use a custom MultipartResolver instance, for
	 * example if not using a Spring web application context.
	 * 
	 * @return the MultipartResolver instance, or {@code null} if none found
	 */
	protected MultipartResolver lookupMultipartResolver() {
		WebApplicationContext wac = findWebApplicationContext();
		String beanName = getMultipartResolverBeanName();
		if (wac != null && wac.containsBean(beanName)) {
			if (logger.isDebugEnabled()) {
				logger.debug("Using MultipartResolver '" + beanName + "' for MultipartFilter");
			}
			return wac.getBean(beanName, MultipartResolver.class);
		} else {
			return this.defaultMultipartResolver;
		}
	}

	private WebApplicationContext findWebApplicationContext() {
		String attrName = getContextAttribute();
		if (attrName != null) {
			return WebApplicationContextUtils.getWebApplicationContext(getServletContext(), attrName);
		} else {
			return WebApplicationContextUtils.getWebApplicationContext(getServletContext());
		}
	}
	
	/**
	 * Return the name of the ServletContext attribute which should be used to
	 * retrieve the {@link WebApplicationContext} from which to load the
	 * delegate bean.
	 */
	private String getContextAttribute() {
		return this.getFilterConfig().getInitParameter(CONTEXT_ATTRIBUTE);
	}

}
