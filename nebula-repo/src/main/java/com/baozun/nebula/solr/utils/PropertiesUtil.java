package com.baozun.nebula.solr.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;





/**
 * @deprecated 推荐使用注入的方式获取属性文件的值，如：@Value("#{meta['order.subOrdinate.head']}")
 * 操作properties配置文件
 * 
  * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 1.0 2010-3-22 上午10:05:19
 * @since 1.0
 */
@Deprecated
public class PropertiesUtil {

	private static final Logger	log	= LoggerFactory.getLogger(PropertiesUtil.class);

	/****************************************************************************************/
	/**
	 * 读取方式
	 * 
	 * @author 金鑫 2010-4-20 下午04:16:34
	 */
	public enum ReadType{
		/**
		 * ClassLoader.getSystemResourceAsStream(propertiesPath)
		 */
		byClassLoaderGetSystemResourceAsStream,
		/**
		 * clz.getClassLoader().getResourceAsStream(propertiesPath)
		 */
		byClassLoaderGetResourceAsStream
	}

	/****************************************************************************************/
	/**
	 * 获取Properties配置文件键值
	 * 
	 * @param clz
	 *            当前类
	 * @param propertiesPath
	 *            Properties文件路径 如"/WEB-INF/classes/user.properties"
	 * @param key
	 *            键
	 * @return 获取Properties配置文件键值
	 */
	public static String getPropertiesValue(Class<?> clz,String propertiesPath,String key){
		Properties properties = getProperties(clz, propertiesPath);
		return properties.getProperty(key);
	}

	/**
	 * 转换成map
	 * 
	 * @param properties
	 * @return
	 */
	public static Map<String, String> toMap(Properties properties){
		// Create a new HashMap and pass an instance of Properties.
		// Properties is an implementation of a Map which keys and values stored as in a string.
		@SuppressWarnings({ "unchecked", "rawtypes" })
		Map<String, String> map = new HashMap<String, String>((Map) properties);
		return map;
	}



	/****************************************************************************************/
	/**
	 * 获得Properties对象
	 * 
	 * @param readType
	 *            读取方式
	 * @param propertiesPath
	 *            Properties路径
	 * @return 获取Properties配置文件键值
	 */
	public static Properties getProperties(ReadType readType,String propertiesPath){
		InputStream inputStream = null;
		switch (readType) {
			case byClassLoaderGetSystemResourceAsStream:
				inputStream = ClassLoader.getSystemResourceAsStream(propertiesPath);
				break;
			default:
				break;
		}
		return getProperties(inputStream);
	}



	/**
	 * 获取Properties
	 * 
	 * @param clz
	 *            当前类
	 * @param propertiesPath
	 *            Properties文件路径 如"/WEB-INF/classes/user.properties"
	 * @return 获取Properties
	 */
	public static Properties getProperties(Class<?> clz,String propertiesPath){
		InputStream inputStream = clz.getResourceAsStream(propertiesPath);
		return getProperties(inputStream);
	}

	/**
	 * 获取Properties
	 * 
	 * @param inputStream
	 *            inputStream
	 * @return 获取Properties
	 */
	public static Properties getProperties(InputStream inputStream){
		Properties properties = null;
		if (null != inputStream){
			properties = new Properties();
			try{
				properties.load(inputStream);
			}catch (IOException e){
				e.printStackTrace();
			}
		}else{
			log.warn("the inputStream is null,can'nt load properties");
		}
		return properties;
	}
	
}
