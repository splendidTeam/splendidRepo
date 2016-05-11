package com.baozun.nebula.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import loxia.support.json.JSONArray;
import loxia.support.json.JSONException;
import loxia.support.json.JSONObject;

/**
 * 用来 快速的获得 json, 交由开发人员自己控制 log 输出级别
 * 
 * @author <a href="mailto:venusdrogon@163.com">金鑫</a>
 * @version 1.0 Sep 23, 2012 3:33:42 PM
 * @deprecated 不再维护,pls use {@link com.feilong.tools.jsonlib.JsonUtil} by feilong
 */
@Deprecated
public final class JsonFormatUtil{

	private static final Logger	log						= LoggerFactory.getLogger(JsonFormatUtil.class);

	/**
	 * 默认的
	 */
	public static final String	defaultPropFilterStr	= "**,-class";

	// ********************************************Map*************************************************************************
	/**
	 * 获得 map (4,4) 格式化的 log 字符串 用于自定义级别的输出<br>
	 * 默认 String propFilterStr = "**,-class";
	 * 
	 * @param map
	 * @return
	 */
	public static String format(Map<String, ? extends Object> map){
		return format(map, defaultPropFilterStr);
	}

	/**
	 * 获得 map (4,4) 格式化的 log 字符串 用于自定义级别的输出
	 * 
	 * @param map
	 *            map
	 * @param propFilterStr
	 *            propFilterStr 如果 NullOrEmpty 设置为""
	 * @return jsonObject.toString(4, 4) 之后的 json 字符串 <br>
	 *         如果 JSONException 返回null
	 * @throws JSONException
	 */
	public static String format(Map<String, ? extends Object> map,String propFilterStr){
		if (Validator.isNullOrEmpty(propFilterStr)){
			propFilterStr = defaultPropFilterStr;
		}
		JSONObject jsonObject = new JSONObject(map, propFilterStr);// , propFilterStr
		try{
			// indent 最顶层的缩进数
			// indentFactor 每一级的缩进数
			String logString = jsonObject.toString(4, 4);
			return logString;
		}catch (JSONException e){
			e.printStackTrace();
		}
		return null;
	}

	// **************************************Collection*******************************************************************************

	/**
	 * 获得List 格式化的 log 字符串 用于自定义级别的输出<br>
	 * 默认 String propFilterStr = "**,-class";
	 * 
	 * @param list
	 *            collection
	 * @return jsonArray.toString(4, 4) 之后的 json 字符串 <br>
	 *         如果 JSONException 返回null
	 */
	public static String format(Collection<? extends Object> collection){
		return format(collection, defaultPropFilterStr);
	}

	/**
	 * 获得List 格式化的 log 字符串 用于自定义级别的输出
	 * 
	 * @param collection
	 * @return jsonArray.toString(4, 4) 之后的 json 字符串 <br>
	 *         如果 JSONException 返回null
	 */
	public static String format(Collection<? extends Object> collection,String propFilterStr){
		if (Validator.isNotNullOrEmpty(collection)){

			log.debug("list size is :{}", collection.size());
			JSONArray jsonArray = new JSONArray(collection, propFilterStr);

			try{
				// indent 最顶层的缩进数
				// indentFactor 每一级的缩进数
				String logString = jsonArray.toString(4, 4);
				return logString;
			}catch (JSONException e){
				e.printStackTrace();
			}
		}
		return null;
	}

	// ********************************************Object********************************************************************
	/**
	 * 输出JsonObject (4,4) 格式化<br>
	 * 默认 String propFilterStr = "**,-class";
	 * 
	 * @param object
	 *            如果 object object instanceof Properties 则 转成map 调用 map format<br>
	 *            如果 object object instanceof Object[] 则format(Arrays.asList(objects))
	 */
	public static String format(Object object){
		if (object instanceof Properties){
			Properties properties = (Properties) object;
			Map<String, String> map = new HashMap<String, String>((Map) properties);
			return format(map);
		}
		// 数组
		if (object instanceof Object[]){
			Object[] objects = (Object[]) object;
			return format(Arrays.asList(objects));
		}
		String propFilterStr = defaultPropFilterStr;

		if (object instanceof Number){
			propFilterStr = "*";
		}
		return format(object, propFilterStr);
	}

	public static String format(Object object,String propFilterStr){
		if (Validator.isNullOrEmpty(propFilterStr)){
			propFilterStr = "";
		}
		JSONObject jsonObject = new JSONObject(object, propFilterStr);
		try{
			// indent 最顶层的缩进数
			// indentFactor 每一级的缩进数
			String logString = jsonObject.toString(4, 4);
			return logString;
		}catch (JSONException e){
			e.printStackTrace();
		}
		return null;
	}
}
