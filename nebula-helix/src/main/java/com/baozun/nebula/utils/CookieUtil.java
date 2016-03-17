package com.baozun.nebula.utils;

import java.security.AccessController;
import java.security.PrivilegedAction;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.security.SecurityUtil;
import org.apache.tomcat.util.http.ServerCookie;

import com.baozun.nebula.utils.cookie.TimeIntervalCookie;

public class CookieUtil {

  public static Cookie getCookie(HttpServletRequest request, String name) {
    Cookie cookies[] = request.getCookies();
    if (cookies == null || name == null || name.length() == 0) {
      return null;
    }
    for (int i = 0; i < cookies.length; i++) {
      if (name.equals(cookies[i].getName())
          /*&& request.getServerName().equals(cookies[i].getDomain())*/) {
        return cookies[i];
      }
    }
    return null;
  }

  public static void deleteCookie(HttpServletRequest request,
      HttpServletResponse response, Cookie cookie) {
    if (cookie != null) {
      cookie.setPath(getPath(request));
      cookie.setValue("");
      cookie.setMaxAge(0);
      response.addCookie(cookie);
    }
  }
  
  public static void deleteCookie(HttpServletRequest request,
	      HttpServletResponse response, String cookieName) {
	    Cookie cookie=new Cookie(cookieName,"");
	    deleteCookie(request,response,cookie);
	  }

  public static void setCookie(HttpServletRequest request,
      HttpServletResponse response, String name, String value) {
    setCookie(request, response, name, value, 0x278d00);
  }

  public static void setCookie(HttpServletRequest request,
      HttpServletResponse response, String name, String value, int maxAge) {
    Cookie cookie = new Cookie(name, value == null ? "" : value);
    cookie.setMaxAge(maxAge);
    cookie.setPath(getPath(request));
    response.addCookie(cookie);
  }
  
  
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
  
  public static void setCookieHttp(HttpServletRequest request,
	      HttpServletResponse response, String name, String value, int maxAge) {
	  	StringBuffer cookie = generateCookieStringInTomcat(
				new TimeIntervalCookie(name, value,maxAge), true);
	  
		response.addHeader("Set-Cookie", cookie.toString());
	  }

  private static String getPath(HttpServletRequest request) {
    String path = request.getContextPath();
    return (path == null || path.length()==0) ? "/" : path;
  }

  
}