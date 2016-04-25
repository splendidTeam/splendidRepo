package com.baozun.nebula.utils;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.baozun.nebula.exception.BusinessException;

/**
 * Token添加与校验类
 * 
 * @author songdianchao
 * 
 */
@Deprecated
public class TokenUtil {
	public static final String TOKEN = "_http";
	public static final String PARAM_TOKEN = "_t";

	public static void addToken(HttpServletRequest request) {
		HttpSession session = request.getSession();
		String getTokenCode = TokenUtil.getTokenCode(session.getId());
		session.setAttribute(TOKEN, getTokenCode);
	}
	
	public static void addToken(HttpSession session) {
		String getTokenCode = TokenUtil.getTokenCode(session.getId());
		session.setAttribute(TOKEN, getTokenCode);
	}
	
	public static String queryToken(HttpServletRequest request) {
		HttpSession session = request.getSession();
		
		return (String)session.getAttribute(TOKEN);
	}

	public static boolean checkToken(HttpServletRequest request) {
		String token = request.getParameter(PARAM_TOKEN);
		
		if(token!=null&&token.equals(request.getSession().getAttribute(TOKEN))){
			removeToken(request);
			return true;
		}
		return false;
	}

	private static String getTokenCode(String sessionId) {
		String uuid = UUID.randomUUID().toString();
		return uuid.replaceAll("-", "");
	}

	public static void removeToken(HttpServletRequest request){
		request.getSession().removeAttribute(TOKEN);
	}

	
}
