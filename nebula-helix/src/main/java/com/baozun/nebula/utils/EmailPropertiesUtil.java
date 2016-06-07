package com.baozun.nebula.utils;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baozun.nebula.utilities.common.ProfileConfigUtil;

public class EmailPropertiesUtil {
	
	private static final Logger logger = LoggerFactory
			.getLogger(EmailPropertiesUtil.class);
	
	private static class SingletonHolder {
		public final static EmailPropertiesUtil instance = new EmailPropertiesUtil();
	}

	public static EmailPropertiesUtil getInstance() {
		return SingletonHolder.instance;
	}

	/**
	 * @deprecated 推荐使用注入的方式获取属性文件的值，如：@Value("#{meta['order.subOrdinate.head']}")
	 * @param filePath
	 * @param className
	 * @return
	 */
	@Deprecated
	public Properties initEmailProperties(String filePath, Class<?> className) {
		return ProfileConfigUtil.findPro(filePath);
	}

}
