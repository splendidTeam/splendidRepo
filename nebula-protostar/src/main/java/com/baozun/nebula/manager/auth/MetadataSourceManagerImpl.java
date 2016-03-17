/**
 * Copyright (c) 2012 Baozun All Rights Reserved.
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
package com.baozun.nebula.manager.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.AntPathRequestMatcher;
import org.springframework.security.web.util.RequestMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.command.RolePrivilegeCommand;
import com.baozun.nebula.command.auth.PrivilegeUrlCommand;
import com.baozun.nebula.dao.auth.PrivilegeDao;

/**
 * @author songdianchao
 * 
 *         该过滤器的主要作用就是通过spring著名的IoC生成securityMetadataSource。
 *         securityMetadataSource相当于本包中自定义的MyInvocationSecurityMetadataSourceService
 *         。
 *         该MyInvocationSecurityMetadataSourceService的作用提从数据库提取权限和资源，装配到HashMap中
 *         ， 供Spring Security使用，用于权限校验。
 * 
 */
@Service
@Transactional(readOnly = true)
public class MetadataSourceManagerImpl implements
		FilterInvocationSecurityMetadataSource {
	@Autowired
	private PrivilegeDao privilegeDao;

	public MetadataSourceManagerImpl() {
	}

	/**
	 * url为key,Collection是对应的权限列表
	 */
	private static Map<String, Collection<ConfigAttribute>> resourceMap = null;
	

	
	private RequestMatcher pathMatcher;

	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return new ArrayList<ConfigAttribute>();
	}

	public boolean supports(Class<?> clazz) {
		return true;
	}

	/*
	 * 加载所有权限与权限url的数据
	 * 
	 */
	@PostConstruct
	public void loadResourceDefine() throws Exception {
		Map<String, Collection<ConfigAttribute>> resourceMapTemp = new HashMap<String, Collection<ConfigAttribute>>();
		

		List<PrivilegeUrlCommand> priUrls=privilegeDao.findAllEffectivePrivilegeUrl();
		
		Set<String> urls = new HashSet<String>();
		List<String> acls=new ArrayList<String>();
		for (PrivilegeUrlCommand priUrl : priUrls) {
			urls.add(priUrl.getUrl());
			acls.add(priUrl.getAcl());
		}
		Iterator<String> it = urls.iterator();
		while (it.hasNext()) {
			String url = it.next();
			Collection<ConfigAttribute> configAttributes = new ArrayList<ConfigAttribute>();
			for (PrivilegeUrlCommand priUrl : priUrls) {
				if (url.equals(priUrl.getUrl())) {
					configAttributes.add(new SecurityConfig(""
							+ priUrl.getAcl()));
				}
			}
			resourceMapTemp.put(url, configAttributes);
		}
		


		resourceMap = resourceMapTemp;
	}

	// 返回所请求资源所需要的权限
	public Collection<ConfigAttribute> getAttributes(Object object)
			throws IllegalArgumentException {
		Iterator<String> it = resourceMap.keySet().iterator();
		while (it.hasNext()) {
			String resURL = it.next();
			if(StringUtils.isNotBlank(resURL)){
				pathMatcher = new AntPathRequestMatcher(resURL);
				if (pathMatcher.matches(((FilterInvocation) object).getRequest())) {
					Collection<ConfigAttribute> returnCollection = resourceMap
							.get(resURL);
					return returnCollection;
				}
			}
		}
		return null;
	}
	


}