package com.baozun.nebula.utilities.integration.oauth;

import javax.servlet.http.HttpServletRequest;

public interface ThirdPartyMemberAdaptor {

	/**
	 * 生成跳转到第三方网站登录页面的url地址
	 * @return
	 */
	public String generateLoginUrl();
	
	
	/**
	 * 通过回调返回第三方会员信息
	 * @param request
	 * @return
	 */
	public ThirdPartyMember returnMember(HttpServletRequest request);
}
