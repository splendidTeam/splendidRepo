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
package com.baozun.nebula.manager.breadcrumb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.baozun.nebula.exception.IllegalItemStateException;
import com.baozun.nebula.sdk.command.CurmbCommand;
import com.baozun.nebula.utilities.common.LangUtil;
import com.baozun.nebula.utilities.common.ProfileConfigUtil;
import com.feilong.tools.jsonlib.JsonUtil;

/**   
 * @Description 
 * @author dongliang ma
 * @date 2016年5月19日 下午2:01:17 
 * @version   
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath*:loxia-hibernate-context.xml",
		"classpath*:loxia-service-context.xml",
		"classpath*:spring.xml" })
@ActiveProfiles("dev")
public class BreadcrumbManagerTest {
	
	private static final Logger log = LoggerFactory.getLogger(BreadcrumbManagerTest.class);
	
	@Autowired
	private BreadcrumbManager breadcrumbManager;
	
	//测试时在spring-manager.xml里加
	//<bean id="facetFilterHelper"
	//	class="com.baozun.nebula.search.FacetFilterHelperImpl">
	//</bean>
	
	@Before
	public void setM(){
		ProfileConfigUtil.setMode("dev");
	}
	
	@Test
	public void test(){
		doTest(14l, 58l, null);
	}
	
	@Test
	public void test2(){
		HttpServletRequest request =getRequest(); 
		doTest(null, 84l, request);
	}
	
	public void doTest(Long navId, Long itemId,HttpServletRequest request){
		LangUtil.setCurrentLang("zh_CN");
		List<CurmbCommand> commands = null;
		try {
			commands = breadcrumbManager.findCurmbCommands(navId, itemId, request);
		} catch (IllegalItemStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		log.debug("sss:{}", JsonUtil.format(commands));
	}
	
	
	public HttpServletRequest getRequest(){
		HttpServletRequest request =new HttpServletRequest() {
			
			@Override
			public AsyncContext startAsync(ServletRequest paramServletRequest,
					ServletResponse paramServletResponse) throws IllegalStateException {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public AsyncContext startAsync() throws IllegalStateException {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public void setCharacterEncoding(String paramString)
					throws UnsupportedEncodingException {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void setAttribute(String paramString, Object paramObject) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void removeAttribute(String paramString) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean isSecure() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean isAsyncSupported() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean isAsyncStarted() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public ServletContext getServletContext() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public int getServerPort() {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public String getServerName() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getScheme() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public RequestDispatcher getRequestDispatcher(String paramString) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public int getRemotePort() {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public String getRemoteHost() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getRemoteAddr() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getRealPath(String paramString) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public BufferedReader getReader() throws IOException {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getProtocol() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String[] getParameterValues(String paramString) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Enumeration<String> getParameterNames() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Map<String, String[]> getParameterMap() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getParameter(String paramString) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Enumeration<Locale> getLocales() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Locale getLocale() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public int getLocalPort() {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public String getLocalName() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getLocalAddr() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public ServletInputStream getInputStream() throws IOException {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public DispatcherType getDispatcherType() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getContentType() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public int getContentLength() {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public String getCharacterEncoding() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Enumeration<String> getAttributeNames() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Object getAttribute(String paramString) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public AsyncContext getAsyncContext() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public void logout() throws ServletException {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void login(String paramString1, String paramString2)
					throws ServletException {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public boolean isUserInRole(String paramString) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean isRequestedSessionIdValid() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean isRequestedSessionIdFromUrl() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean isRequestedSessionIdFromURL() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean isRequestedSessionIdFromCookie() {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public Principal getUserPrincipal() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public HttpSession getSession(boolean paramBoolean) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public HttpSession getSession() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getServletPath() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getRequestedSessionId() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public StringBuffer getRequestURL() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getRequestURI() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getRemoteUser() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getQueryString() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getPathTranslated() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getPathInfo() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Collection<Part> getParts() throws IOException, ServletException {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Part getPart(String paramString) throws IOException,
					ServletException {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getMethod() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public int getIntHeader(String paramString) {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public Enumeration<String> getHeaders(String paramString) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Enumeration<String> getHeaderNames() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getHeader(String paramString) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public long getDateHeader(String paramString) {
				// TODO Auto-generated method stub
				return 0;
			}
			
			@Override
			public Cookie[] getCookies() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getContextPath() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String getAuthType() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public boolean authenticate(HttpServletResponse paramHttpServletResponse)
					throws IOException, ServletException {
				// TODO Auto-generated method stub
				return false;
			}
		};
		return request;
	}
	
	

}
