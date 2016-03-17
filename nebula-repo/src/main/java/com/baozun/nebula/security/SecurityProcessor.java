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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * @author D.C
 * @date 2015年10月10日 上午10:10:51
 */
public class SecurityProcessor {
	private static List<SecurityActivity> activities = new ArrayList<SecurityActivity>();
	private static final Log LOG = LogFactory.getLog(SecurityProcessor.class);

	static {
		activities.add(new SQLInjectionEncodeSecurityActivity());
		activities.add(new HtmlEscapeSecurityActivity());
	}

	public static String process(HttpServletRequest request, String name, String value) {
		if(!needProcess(request, name)) {
			return value;
		}
		String result = value;
		for (Iterator<SecurityActivity> iterator = activities.iterator(); iterator.hasNext();) {
			SecurityActivity securityActivity = iterator.next();
			result = securityActivity.execute(result);
		}
		LOG.debug(String.format("original data: [%s] new data: [%s]", value, result));
		return result;
	}
	
	private static boolean needProcess(HttpServletRequest request, String name) {
		List<AntPathRequestMatcher> matcherList = SecurityConfig.getMatcherList();
		if (!matcherList.isEmpty()) {
			for (AntPathRequestMatcher matcher : matcherList) {
				if (matcher.matches(request)) {
					return false;
				}
			}
		}
		return !SecurityConfig.ignoreParameters.contains(name);
	}
}
