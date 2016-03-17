/**
 * 
 */
package com.baozun.nebula.utils.property;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang.StringUtils;

import com.baozun.nebula.exception.BusinessException;


/**
 * @author xianze.zhang
 *@creattime 2013-7-8
 */
public class PropertyUtil{
	private static PropertyUtilsBean propertyUtils = new PropertyUtilsBean();
	/**
	 * 根据属性名获取bean中的属性值
	 * @param bean
	 * @param propertyName
	 * @return
	 */
	public static Object getProperty(Object bean, String propertyName){
		try{
			return propertyUtils.getProperty(bean, propertyName);
		}catch (IllegalAccessException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (InvocationTargetException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (NoSuchMethodException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 	
	* @author 何波
	* @Description: 封装 商品详情页面url
	* @return   
	* String   
	* @throws
	 */
	public  static String getPdsBasesUrl(String pdp_base_url) {
		if (StringUtils.isEmpty(pdp_base_url)) {
			throw new BusinessException("pdp_base_url is null");
		}
		String  url = pdp_base_url.trim();
		if (!url.startsWith("http://")) {
			url = "http://" + url;
		}
		if(!pdp_base_url.endsWith("code") && !pdp_base_url.endsWith("itemId")){
			throw new BusinessException("pdp_base_url format error , end is bycode or byitemId ");
		}
		return url;
	}
}
