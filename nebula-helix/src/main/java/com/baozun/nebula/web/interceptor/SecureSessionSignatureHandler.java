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

import java.io.Serializable;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baozun.nebula.utilities.common.EncryptUtil;
import com.baozun.nebula.web.HelixConfig;
import com.baozun.nebula.web.HelixConstants;
import com.baozun.nebula.web.MemberDetails;
import com.baozun.nebula.web.constants.SessionKeyConstants;
import com.feilong.servlet.http.CookieUtil;
import com.feilong.servlet.http.entity.CookieEntity;

public class SecureSessionSignatureHandler implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1703880397075732696L;
	
	private static final Logger LOG = LoggerFactory.getLogger(SecureSessionSignatureHandler.class);
	
	public static final String SECURE_SIGNATURE = "s_ck";
	
	public boolean CheckSignature(HttpServletRequest request, HttpServletResponse response){
		//因为应用都是隐藏在Nginx后面的，所以不能直接通过request.getProtocol()来判断目前是否是https。
		//目前采用header头中的scheme信息来读取，因此需要保证Nginx中将此信息配置完成，且key为scheme
		String scheme=request.getHeader(HelixConfig.getInstance().get(HelixConstants.SITE_WEBHEADER_REQUEST_PROTOCOL));
		
		if(scheme!=null&&scheme.endsWith("https")){
			LOG.debug("Https is detected. Secure Session Signature will be checked");
			String sign = sign(request);
			Cookie cookie = CookieUtil.getCookie(request, SECURE_SIGNATURE);
			if(cookie == null) {
				LOG.info("Secure Session Check Failure: Cookie is NULL");
				return false;
			}
			if(cookie.getValue().equals(sign)) {
				LOG.info("Secure Session Check Success");
				return true;
			}
			LOG.info("Secure Session Check Failure: Signature mismatch");
			return false;
		}else{
			LOG.debug("Https is not detected. Secure Session Signature will be passed");
			return true;
		}
			
	}
	
	public void setSignature(HttpServletRequest request, HttpServletResponse response){		
		String scheme=request.getHeader(HelixConfig.getInstance().get(HelixConstants.SITE_WEBHEADER_REQUEST_PROTOCOL));
		
		if(scheme!=null&&scheme.endsWith("https")){
			LOG.debug("Https is detected. Secure Session Signature will be generated and set");
			String sign = sign(request);

	        CookieEntity cookie = new CookieEntity(SECURE_SIGNATURE, sign);
	        cookie.setHttpOnly(true);
	        cookie.setSecure(true);
	        CookieUtil.addCookie(cookie, response);
	        LOG.info("Secure Cookie {} is set for Session {}", cookie.getValue(), request.getSession().getId());
		}     
	}
	
	public void deleteSignature(HttpServletResponse response){
		CookieUtil.deleteCookie(SECURE_SIGNATURE, response);;
	}
	
	protected String sign(HttpServletRequest request){
		String salt = HelixConfig.getInstance().get(HelixConstants.SECURITY_HTTPS_SECURESESSION_SALT);
		assert salt != null : "Cannot read configuration for " + HelixConstants.SECURITY_HTTPS_SECURESESSION_SALT;
		
		String sessionId = request.getSession().getId();
		MemberDetails memberDetails = (MemberDetails) request.getSession().getAttribute(SessionKeyConstants.MEMBER_CONTEXT);
		StringBuffer sb = new StringBuffer();
		sb.append(sessionId);
		sb.append(memberDetails == null? "-": memberDetails.getMemberId());
		return EncryptUtil.getInstance().hash(sb.toString(), salt);
	}

}
