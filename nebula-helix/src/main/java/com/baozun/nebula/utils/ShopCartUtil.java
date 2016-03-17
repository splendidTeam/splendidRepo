package com.baozun.nebula.utils;

import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.web.util.WebUtils;

import com.baozun.nebula.model.member.Member;
import com.baozun.nebula.web.command.MemberFrontendCommand;
import com.baozun.nebula.web.constants.Constants;


public class ShopCartUtil {
	public static final String GUESTINDENTIFY="g_c";
	/**
	 * 生成游客标识
	 * @return
	 */
	public static String createGuestIndentifyCode(){
		return UUID.randomUUID().toString();
	}
	
	/**
	 * 从cookie中得到游客标识
	 * @param request
	 * @param memberId
	 * @return
	 */
	public static String getGuestIndentify(HttpServletRequest request, Long memberId) {
		String guestIndentify=null;
		if(memberId==null){
			Cookie cookie = WebUtils.getCookie(request,GUESTINDENTIFY);
			if(cookie!=null){
				guestIndentify=cookie.getValue();
			}
			
		}
		return guestIndentify;
	}
	/**
	 * 从session中得到会员
	 * @param request
	 * @return
	 */
	public static Long getMemberId(HttpServletRequest request) {
		Long memberId=null;
		MemberFrontendCommand member=(MemberFrontendCommand) request.getSession().getAttribute(Constants.BAOZUN_SEESSION_MEMBER);
		if(null!=member){
			memberId=member.getId();
		}
		return memberId;
	}
	

}
