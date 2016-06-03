/**
 * Copyright (c) 2010 Jumbomart All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Jumbomart.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Jumbo.
 *
 * JUMBOMART MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. JUMBOMART SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.baozun.nebula.web.interceptor;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baozun.nebula.web.constants.SessionKeyConstants;
import com.feilong.core.CharsetType;

/**
 * 登录跳转控制器
 * @author Benjamin.Liu
 *
 */
public class LoginForwardHandler {
    
    /** The Constant log. */
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginForwardHandler.class);

	/**
	 * 设置跳转URL，默认实现是放入Session，可重载
	 * @param request
	 * @param url
	 * @throws IOException
	 */
	public void setForwardURL(HttpServletRequest request, String url) throws IOException{
		String encodeUrl = URLEncoder.encode(url, CharsetType.UTF8);
        request.getSession().setAttribute(SessionKeyConstants.MEMBER_IBACK_URL, encodeUrl);
        
        LOGGER.debug("set [MEMBER_IBACK_URL] Attribute to session,url :[{}],encodeUrl:[{}]",url, encodeUrl);
	}
	
	/**
	 * 获取跳转URL，默认实现是从Session中取出并删除Session中对应的值，可重载
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException 
	 * @throws IOException
	 */
	public String getForwardURL(HttpServletRequest request) throws UnsupportedEncodingException{
		String url = (String)request.getSession().getAttribute(SessionKeyConstants.MEMBER_IBACK_URL);
		if(url != null){
			request.getSession().removeAttribute(SessionKeyConstants.MEMBER_IBACK_URL);
			
			String decodeUrl = URLDecoder.decode(url, CharsetType.UTF8);
		    LOGGER.debug("MEMBER_IBACK_URL in session:[{}],remove it,and return decodeUrl:[{}]", url,decodeUrl);
            return decodeUrl;
		}else{
			return null;
		}
	}
	
	/**
	 * 默认跳转方法
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void forward(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String url = getForwardURL(request);
		if(url != null)
			response.sendRedirect(url);
	}
}
