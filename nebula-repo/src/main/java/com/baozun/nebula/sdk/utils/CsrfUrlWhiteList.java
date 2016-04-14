package com.baozun.nebula.sdk.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * 白名单中的URL将不执行CSRF token验证
 * 
 * @author Richard Zhao
 * 
 */
public class CsrfUrlWhiteList implements RequestMatcher,
		ApplicationContextAware {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private ApplicationContext ac;

	private RequestMatcher whiteListRequestMatcher;
	private RequestMatcher defaultRequiresCsrfMatcher;

	@SuppressWarnings("unchecked")
	@PostConstruct
	public void init() {
		
		List<String> csrfUrlWhiteList = null;
		try {
			// 在spring xml中配置白名单，以跳过CSRF token验证
			// <util:list id="csrfUrlWhiteList" value-type="java.lang.String" >
			//   <value>/aaa/bbb</value>
			// </util:list>
			csrfUrlWhiteList = (List<String>) ac.getBean("csrfUrlWhiteList");
		} catch (BeansException e) {
			logger.debug("bean 'csrfUrlWhiteList' not found", e);
		}

		List<RequestMatcher> requestMatchers = new ArrayList<RequestMatcher>();
		if (csrfUrlWhiteList != null) {
			for (String url : csrfUrlWhiteList) {
				RequestMatcher requestMatcher = new AntPathRequestMatcher(url);
				requestMatchers.add(requestMatcher);
			}
		}

		if (requestMatchers.isEmpty()) {
			whiteListRequestMatcher = AnyRequestNotMatcher.INSTANCE;
		} else {
			whiteListRequestMatcher = new OrRequestMatcher(requestMatchers);
		}

		defaultRequiresCsrfMatcher = new DefaultRequiresCsrfMatcher();
	}

	@Override
	public boolean matches(HttpServletRequest request) {

		boolean result = defaultRequiresCsrfMatcher.matches(request)
				&& !whiteListRequestMatcher.matches(request);

		if (logger.isDebugEnabled()) {
			logger.debug(String.format("CSRF URL %s matche (%s--%s)",
					result ? "" : "【not】", request.getMethod(),
					request.getServletPath()));
		}

		return result;
	}

	private static final class DefaultRequiresCsrfMatcher implements
			RequestMatcher {
		private final Pattern allowedMethods = Pattern
				.compile("^(GET|HEAD|TRACE|OPTIONS)$");

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.springframework.security.web.util.matcher.RequestMatcher#matches
		 * (javax.servlet.http.HttpServletRequest)
		 */
		@Override
		public boolean matches(HttpServletRequest request) {
			return !allowedMethods.matcher(request.getMethod()).matches();
		}
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
			throws BeansException {
		ac = applicationContext;

	}

	private static final class AnyRequestNotMatcher implements RequestMatcher {
		public static final RequestMatcher INSTANCE = new AnyRequestNotMatcher();

		@Override
		public boolean matches(HttpServletRequest request) {
			return false;
		}

		private AnyRequestNotMatcher() {
		}
	}

}
