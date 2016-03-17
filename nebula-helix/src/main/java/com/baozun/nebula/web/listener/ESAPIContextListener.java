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

package com.baozun.nebula.web.listener;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 专门给esapi 设置的监听器
 * 
 * @author <a href="mailto:venusdrogon@163.com">金鑫</a>
 * @version 1.0 Nov 9, 2012 5:16:58 PM
 */
public class ESAPIContextListener implements ServletContextListener{

	private static final Logger	log	= LoggerFactory.getLogger(ESAPIContextListener.class);

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent sce){

		ServletContext servletContext = sce.getServletContext();

		try{
			
			ClassLoader classLoader = ESAPIContextListener.class.getClassLoader();
			// 查找具有给定名称的资源。
			URL url = classLoader.getResource("esapi");
			// 取到classes/esapi 绝对地址,
			// 此处不能直接使用ClassLoaderUtil.getResource("esapi") 做org.owasp.esapi.resources 参数值
			// 否则 后面拼接的路径 会将 file:/ 纯粹当 文件路径一部分拼接
			File esapiDirectory = new File(url.toURI());
			String customDirectory = esapiDirectory.getAbsolutePath();

			File file = new File(customDirectory, "ESAPI.properties");

			servletContext.log("ESAPI.properties getAbsolutePath():" + file.getAbsolutePath());
			servletContext.log("customDirectory != null && file.canRead():" + (customDirectory != null && file.canRead()) + "");

			// 设置值 以便esapi使用
			System.setProperty("org.owasp.esapi.resources", customDirectory);
			servletContext.log("set setProperty org.owasp.esapi.resources:" + customDirectory);
		}catch (URISyntaxException e){
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * @see javax.servlet.ServletContextListener#contextDestroyed(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextDestroyed(ServletContextEvent sce){
		// TODO Auto-generated method stub
	}
}
