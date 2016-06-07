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
package com.baozun.nebula.utilities.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 配置读取帮助类，可以一次读取多个配置文件，排名靠前的文件优先级更高，即当多个配置文件中存在同一配置时，优先级高的（排前面的）配置被使用。
 * @author liuliu
 *
 */
public class ConfigurationUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(ConfigurationUtil.class);

	private static final String[] CONFIGS = new String[]{"nebula-utilities","nebula/utilities"};
	
	public static final String ENCRYPT_ALGORITHM_KEY = "nebula.encrypt.algorithm";
	public static final String SIGNATURE_ALGORITHM_KEY = "nebula.signature.algorithm";
	public static final String RANDOM_ALGORITHM_KEY = "nebula.random.algorithm";
	public static final String HASH_ALGORITHM_KEY = "nebula.hash.algorithm";
	public static final String DIGEST_ALGORITHM_KEY = "nebula.digest.algorithm";
	
	public static final String DEFAULT_ENCODING = "UTF-8";
	
	private List<Properties> properties;
	
	private static ConfigurationUtil instance = null;
	private ConfigurationUtil(){
		properties = ConfigurationUtil.loadConfiguration(CONFIGS);
	}
	
	public static ConfigurationUtil getInstance(){
		if(instance == null)
			instance = new ConfigurationUtil();
		return instance;
	}
	
	public String getNebulaUtilityConfiguration(String propertyName){
		return ConfigurationUtil.getConfigurationValue(propertyName, properties);
	}
	
	/**
	 * 读取配置
	 * @param configs
	 * @return
	 */
	public static List<Properties> loadConfiguration(String... configs){

		List<Properties> props = new ArrayList<Properties>();
		for(String config: configs){
			Properties prop = ProfileConfigUtil.findCommonPro(config + ".properties");
			props.add(prop);
		}
		return props;
	}
	
	/**
	 * 查找配置值
	 * @param propertyName
	 * @param properties
	 * @return
	 */
	public static String getConfigurationValue(String propertyName, List<Properties> properties){
		if(properties == null || properties.size() == 0) return null;
		String result = null;
		for(Properties prop: properties){
			result = prop.getProperty(propertyName);
			if(result != null) break;
		}
		return result;
	}
}
