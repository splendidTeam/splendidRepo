package com.baozun.nebula.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baozun.nebula.utilities.common.ResourceUtil;

public class EmailPropertiesUtil {
	
	private static final Logger logger = LoggerFactory
			.getLogger(EmailPropertiesUtil.class);
	
	private static class SingletonHolder {
		public final static EmailPropertiesUtil instance = new EmailPropertiesUtil();
	}

	public static EmailPropertiesUtil getInstance() {
		return SingletonHolder.instance;
	}

	public Properties initEmailProperties(String filePath, Class<?> className) {
		InputStream is = ResourceUtil.getResourceAsStream(filePath,
				className);
		Properties properties = new Properties();
		if (is != null) {
			try {
				properties.load(is);
			} catch (IOException e) {
				e.printStackTrace();
				logger.error("Error occurs when loading {}", filePath);
			}
		} else {
			logger.error("Could not find {}", filePath);
		}
		return properties;
	}

}
