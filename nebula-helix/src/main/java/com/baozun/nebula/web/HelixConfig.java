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
package com.baozun.nebula.web;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Helix的基础配置类，使用该类可以获得相关基础配置。
 * 默认配置文件在 nebula/helix-default.properties 处，里面有目前框架的默认值。
 * 如果需要重载，可以在项目中创建 helix.properties 文件并重载相应配置
 * 
 * @author Benjamin.Liu
 *
 */
public class HelixConfig {
	private static final Logger LOG = LoggerFactory.getLogger(HelixConfig.class);
	
	private static HelixConfig instance;
	private static final String[] CONFIGS = new String[]{"helix","nebula/helix-default"};
	
	private List<Properties> props = new ArrayList<Properties>(); 
	
	private HelixConfig(){
		for(String config: CONFIGS){
			InputStream is = getResourceAsStream(
					config + ".properties", HelixConfig.class);
			if(is != null){
				Properties prop = new Properties();
				try {
					prop.load(is);
					props.add(prop);
				} catch (IOException e) {
					LOG.warn("Error occurs when loading {}.properties", config);
				}
			}else{
				LOG.warn("Could not find {}.properties", config);
			}
		}
	}
	
	public static HelixConfig getInstance(){
		if(instance == null) instance = new HelixConfig();		
		return instance;
	}
	
	/**
	 * 读取某个配置的值
	 * 
	 * @param name
	 * @return
	 */
	public String get(String name){
		String result = null;
		for(Properties prop: props){
			result = prop.getProperty(name);
			if(result != null) break;
		}
		return result;
	}
	
	/**
	 * 判断某个配置是否存在
	 * @param name
	 * @return
	 */
	public boolean exists(String name){
		for(Properties prop: props){
			if(prop.containsKey(name))
				return true;
		}
		return false;
	}
	
	private InputStream getResourceAsStream(String resourceName, Class<?> callingClass) {
        URL url = getResource(resourceName, callingClass);
        try {
            return (url != null) ? url.openStream() : null;
        } catch (IOException e) {
            return null;
        }
    }
		
	private URL getResource(String resourceName, Class<?> callingClass) {
        URL url = null;
        url = Thread.currentThread().getContextClassLoader().getResource(resourceName);
        if (url == null) {
            url = HelixConfig.class.getClassLoader().getResource(resourceName);
        }
        if (url == null) {
            url = callingClass.getClassLoader().getResource(resourceName);
        }
        return url;
    }
}
