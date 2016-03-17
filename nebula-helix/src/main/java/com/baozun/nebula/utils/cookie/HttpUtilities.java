package com.baozun.nebula.utils.cookie;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.security.SecurityUtil;
import org.apache.tomcat.util.http.ServerCookie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpUtilities {
	public static final int SECONDS_PER_YEAR = 60 * 60 * 24 * 365;
	public static final int SECONDS_PER_MONTH = 60 * 60 * 24 * 30;
	public static final int SECONDS_PER_WEEK = 60 * 60 * 24 * 7;
	public static final int SECONDS_PER_DAY = 60 * 60 * 24;
	public static final int SECONDS_PER_HOUR = 60 * 60;

	private static final Logger logger = LoggerFactory
			.getLogger(HttpUtilities.class);

	private static HttpUtilities instance = null;

	private HttpUtilities() {
	}

	private ThreadLocalRequest currentRequest = new ThreadLocalRequest();
	private ThreadLocalResponse currentResponse = new ThreadLocalResponse();
	private ThreadLocalCookieMap currentCookieMap = new ThreadLocalCookieMap();

	public static HttpUtilities getInstance() {
		if (instance == null) {
			synchronized (HttpUtilities.class) {
				if (instance == null)
					instance = new HttpUtilities();
			}
		}
		return instance;
	}

	public void setCurrentHTTP(HttpServletRequest request,
			HttpServletResponse response) {
		currentRequest.setRequest(request);
		currentResponse.setResponse(response);
		currentCookieMap.setCookieMap(getCookieMap(request));
	}

	public void clearCurrent() {
		currentRequest.set(null);
		currentResponse.set(null);
		currentCookieMap.set(null);
	}

	public HttpServletRequest getCurrentRequest() {
		return currentRequest.getRequest();
	}

	public HttpServletResponse getCurrentResponse() {
		return currentResponse.getResponse();
	}

	public Map<String, Cookie> getCurrentCookieMap() {
		return currentCookieMap.getCookieMap();
	}

	public static String getClientIp(HttpServletRequest request, boolean useCDN) {
		String ip = request.getHeader("x-Forwared-For");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Real-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		logger.debug("remote address: {}", ip);
		return ip == null ? "" : ip;
	}

	/**
	 * Defines the ThreadLocalRequest to store the current request for this
	 * thread.
	 */
	private class ThreadLocalRequest extends
			InheritableThreadLocal<HttpServletRequest> {

		public HttpServletRequest getRequest() {
			return super.get();
		}

		public HttpServletRequest initialValue() {
			return null;
		}

		public void setRequest(HttpServletRequest newRequest) {
			super.set(newRequest);
		}
	}

	/**
	 * Defines the ThreadLocalResponse to store the current response for this
	 * thread.
	 */
	private class ThreadLocalResponse extends
			InheritableThreadLocal<HttpServletResponse> {

		public HttpServletResponse getResponse() {
			return super.get();
		}

		public HttpServletResponse initialValue() {
			return null;
		}

		public void setResponse(HttpServletResponse newResponse) {
			super.set(newResponse);
		}
	}

	private class ThreadLocalCookieMap extends
			InheritableThreadLocal<Map<String, Cookie>> {
		public Map<String, Cookie> getCookieMap() {
			return super.get();
		}

		public Map<String, Cookie> initialValue() {
			return null;
		}

		public void setCookieMap(Map<String, Cookie> cookieMap) {
			super.set(cookieMap);
		}
	}

	public static Map<String, Cookie> getCookieMap(HttpServletRequest request) {
		Map<String, Cookie> cookieMap = new HashMap<String, Cookie>();
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getValue() != null && !cookie.getValue().equals("")) {
					cookieMap.put(cookie.getName(), cookie);
				}
			}
		}
		return cookieMap;
	}

	public String getCookieValue(String name) {
		if (getCurrentCookieMap() != null)
			return getCookieValue(name, getCurrentCookieMap());
		else
			throw new IllegalStateException(
					"CurrentHTTP is not set, please use SecurityWrapper filter or use static method instead");
	}

	public Cookie getCookie(String name) {
		if (getCurrentCookieMap() != null)
			return getCookie(name, getCurrentCookieMap());
		else
			throw new IllegalStateException(
					"CurrentHTTP is not set, please use SecurityWrapper filter or use static method instead");
	}

	public void removeCookie(String name) {
		if (getCurrentCookieMap() != null)
			removeCookie(name, getCurrentCookieMap(), getCurrentResponse());
		else
			throw new IllegalStateException(
					"CurrentHTTP is not set, please use SecurityWrapper filter or use static method instead");
	}

	public void addCookieValue(String name, String value) {
		if (getCurrentResponse() == null)
			throw new IllegalStateException(
					"CurrentHTTP is not set, please use SecurityWrapper filter or use static method instead");
		addCookieValue(name, value, getCurrentResponse());
	}

	public void addCookie(Cookie cookie) {
		if (getCurrentResponse() == null)
			throw new IllegalStateException(
					"CurrentHTTP is not set, please use SecurityWrapper filter or use static method instead");
		getCurrentResponse().addCookie(cookie);
	}

	public void setCookieValue(String name, String value) {
		if (getCurrentCookieMap() != null)
			setCookieValue(name, value, getCurrentCookieMap(),
					getCurrentResponse());
		else
			throw new IllegalStateException(
					"CurrentHTTP is not set, please use SecurityWrapper filter or use static method instead");
	}

	public static String getCookieValue(String name,
			Map<String, Cookie> cookieMap) {
		if (cookieMap == null)
			return null;
		Cookie cookie = cookieMap.get(name);
		logger.debug("[Cookie {}] {}", name,
				cookie == null ? "null" : cookie.getValue());
		if (cookie == null)
			return null;
		return cookie.getValue();
	}

	public static String getCookieValue(String name, HttpServletRequest request) {
		Cookie cookie = getCookie(name, request);
		logger.debug("[Cookie {}] {}", name,
				cookie == null ? "null" : cookie.getValue());
		if (cookie == null)
			return null;
		return cookie.getValue();
	}

	public static Cookie getCookie(String name, Map<String, Cookie> cookieMap) {
		if (cookieMap == null)
			return null;
		return cookieMap.get(name);
	}

	public static Cookie getCookie(String name, HttpServletRequest request) {
		logger.debug("[get Cookie {}]", name);
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				// log.info("All Cookies:" + cookie.getName() + "," +
				// cookie.getValue());
				if (cookie.getName().equals(name)) {
					if (cookie.getValue() != null
							&& !cookie.getValue().equals("")) {
						return cookie;
					}
				}

			}
		}
		return null;
	}

	public static void removeCookie(Cookie cookie, HttpServletResponse response) {
		cookie.setValue(null);
		cookie.setPath("/");
		cookie.setMaxAge(0);
		response.addCookie(cookie);
	}

	public static void removeCookie(String cookieName,
			HttpServletRequest request, HttpServletResponse response) {
		logger.debug("[remove Cookie {}]", cookieName);
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				// log.info("All Cookies:" + cookie.getName() + "," +
				// cookie.getValue());
				if (cookie.getName().equals(cookieName)) {
					removeCookie(cookie, response);
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private static StringBuffer generateCookieStringInTomcat(
			final Cookie cookie, final boolean httpOnly) {
		final StringBuffer sb = new StringBuffer();
		// web application code can receive a IllegalArgumentException
		// from the appendCookieValue invokation
		if (SecurityUtil.isPackageProtectionEnabled()) {
			AccessController.doPrivileged(new PrivilegedAction() {
				public Object run() {
					ServerCookie.appendCookieValue(sb, cookie.getVersion(),
							cookie.getName(), cookie.getValue(),
							cookie.getPath(), cookie.getDomain(),
							cookie.getComment(), cookie.getMaxAge(),
							cookie.getSecure(), httpOnly);
					return null;
				}
			});
		} else {
			ServerCookie.appendCookieValue(sb, cookie.getVersion(),
					cookie.getName(), cookie.getValue(), cookie.getPath(),
					cookie.getDomain(), cookie.getComment(),
					cookie.getMaxAge(), cookie.getSecure(), httpOnly);
		}
		return sb;
	}

	public static void removeCookie(String cookieName,
			Map<String, Cookie> cookieMap, HttpServletResponse response) {
		logger.debug("[remove Cookie {}]", cookieName);
		Cookie cookie = cookieMap.get(cookieName);
		if (cookie != null) {
			cookieMap.remove(cookieName);
			removeCookie(cookie, response);
		}
	}

	public static void addCookieValue(String name, String value,
			HttpServletResponse response) {
		addCookieValue(name, value, response, true);
	}

	public static void addCookieValue(String name, String value,
			HttpServletResponse response, boolean httpOnly) {
		logger.debug("[add Cookie {}] {}", name, value);
		if (httpOnly) {
			StringBuffer cookie = generateCookieStringInTomcat(
					new TimeIntervalCookie(name, value), true);
			response.addHeader("Set-Cookie", cookie.toString());
		} else {
			response.addCookie(new TimeIntervalCookie(name, value));
		}
	}

	public static void addCookieValue(String name, String value,
			HttpServletResponse response, int interval) {
		addCookieValue(name, value, interval, response, true);
	}

	public static void addCookieValue(String name, String value, int interval,
			HttpServletResponse response, boolean httpOnly) {
		logger.debug("[add Cookie {}] {}", name, value);
		if (httpOnly) {
			StringBuffer cookie = generateCookieStringInTomcat(
					new TimeIntervalCookie(name, value, interval), true);
			response.addHeader("Set-Cookie", cookie.toString());
		} else {
			response.addCookie(new TimeIntervalCookie(name, value));
		}
	}

	public static void setCookieValue(String name, String value,
			HttpServletRequest request, HttpServletResponse response) {
		setCookieValue(name, value, request, response, true);
	}

	public static void setCookieValue(String name, String value,
			HttpServletRequest request, HttpServletResponse response,
			boolean httpOnly) {
		logger.debug("[set Cookie {}] {}", name, value);
		if (httpOnly) {
			StringBuffer cookie = generateCookieStringInTomcat(
					new TimeIntervalCookie(name, value), true);
			response.addHeader("Set-Cookie", cookie.toString());
		} else {
			Cookie cookie = getCookie(name, request);
			if (cookie == null)
				response.addCookie(new TimeIntervalCookie(name, value));
			else {
				cookie.setValue(value);
				cookie.setPath("/");
				response.addCookie(cookie);
			}
		}
	}

	public static void setCookieValue(String name, String value,
			HttpServletRequest request, HttpServletResponse response,
			int interval) {
		setCookieValue(name, value, request, response, interval, true);
	}

	public static void setCookieValue(String name, String value,
			HttpServletRequest request, HttpServletResponse response,
			int interval, boolean httpOnly) {
		logger.debug("[set Cookie {}] {}", name, value);
		if (httpOnly) {
			StringBuffer cookie = generateCookieStringInTomcat(
					new TimeIntervalCookie(name, value, interval), true);
			response.addHeader("Set-Cookie", cookie.toString());
		} else {
			Cookie cookie = getCookie(name, request);
			if (cookie == null)
				response.addCookie(new TimeIntervalCookie(name, value, interval));
			else {
				cookie.setValue(value);
				cookie.setPath("/");
				cookie.setMaxAge(interval);
				response.addCookie(cookie);
			}
		}
	}

	public static void setCookieValue(String name, String value,
			Map<String, Cookie> cookieMap, HttpServletResponse response) {
		setCookieValue(name, value, cookieMap, response, true);
	}

	public static void setCookieValue(String name, String value,
			Map<String, Cookie> cookieMap, HttpServletResponse response,
			boolean httpOnly) {
		logger.debug("[set Cookie {}] {}", name, value);
		if (httpOnly) {
			Cookie cookie = cookieMap.get(name);
			if (cookie == null)
				cookie = new TimeIntervalCookie(name, value);
			else {
				cookie.setValue(value);
				cookie.setPath("/");
			}
			StringBuffer sb = generateCookieStringInTomcat(cookie, true);
			response.addHeader("Set-Cookie", sb.toString());
		} else {
			Cookie cookie = cookieMap.get(name);
			if (cookie == null)
				response.addCookie(new TimeIntervalCookie(name, value));
			else {
				cookie.setValue(value);
				cookie.setPath("/");
				response.addCookie(cookie);
			}
		}
	}

	public static void setCookieValue(String name, String value,
			Map<String, Cookie> cookieMap, HttpServletResponse response,
			int interval) {
		setCookieValue(name, value, cookieMap, response, interval, true);
	}

	public static void setCookieValue(String name, String value,
			Map<String, Cookie> cookieMap, HttpServletResponse response,
			int interval, boolean httpOnly) {
		logger.debug("[set Cookie {}] {}", name, value);
		if (httpOnly) {
			Cookie cookie = cookieMap.get(name);
			if (cookie == null)
				cookie = new TimeIntervalCookie(name, value, interval);
			else {
				cookie.setMaxAge(interval);
				cookie.setValue(value);
				cookie.setPath("/");
			}
			StringBuffer sb = generateCookieStringInTomcat(cookie, true);
			response.addHeader("Set-Cookie", sb.toString());
		} else {
			Cookie cookie = cookieMap.get(name);
			if (cookie == null)
				response.addCookie(new TimeIntervalCookie(name, value, interval));
			else {
				cookie.setValue(value);
				cookie.setPath("/");
				cookie.setMaxAge(interval);
				response.addCookie(cookie);
			}
		}
	}
	

	
	public static void setCookieValue(String name, String value,
			HttpServletRequest request, HttpServletResponse response,
			int interval, boolean httpOnly,boolean secure) {
		logger.debug("[set Cookie {}] {}", name, value);
		if (httpOnly) {
			Cookie ck=new TimeIntervalCookie(name, value, interval);
			ck.setSecure(secure);
			StringBuffer cookie = generateCookieStringInTomcat(
					ck, true);
			response.addHeader("Set-Cookie", cookie.toString());
		} else {
			Cookie cookie = getCookie(name, request);
			if (cookie == null)
				response.addCookie(new TimeIntervalCookie(name, value, interval));
			else {
				cookie.setValue(value);
				cookie.setPath("/");
				cookie.setMaxAge(interval);
				cookie.setSecure(secure);
				response.addCookie(cookie);
			}
		}
	}

	@Deprecated
	public static void setCookieValue(String name, String value,
			HttpServletResponse response) {
		logger.debug("[set Cookie {}] {}", name, value);
		response.addCookie(new TimeIntervalCookie(name, value));
	}

	@Deprecated
	public static void setCookieValue(String name, String value,
			HttpServletResponse response, int interval) {
		logger.debug("[set Cookie {}] {}", name, value);
		response.addCookie(new TimeIntervalCookie(name, value, interval));
	}
}
