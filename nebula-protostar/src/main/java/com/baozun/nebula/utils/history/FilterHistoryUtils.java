package com.baozun.nebula.utils.history;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import com.baozun.nebula.utils.query.bean.QueryPara;

/**
 * 将用户
 * @author Justin Hu
 *
 */
public class FilterHistoryUtils {

	/**
	 * session中的key前辍
	 */
	public static String PARAM_KEY_STAR_WITH="paramKeyStarWith";
	
	/**
	 * sessionkey中页面特征的参数名
	 */
	public static String KEY_NAME="filterKeyName";
	
	/**
	 * 保存筛选条件到session中
	 * 前辍+uri地址
	 * 
	 * @param queryParaMap
	 */
	public static void saveFilterParam(HttpServletRequest request){

		
		String result="";
		String page="";
		
		for(Object keyObject:request.getParameterMap().keySet()){
			String key = keyObject.toString();
			
			String value = request.getParameter(key);
				//若值为null不做处理
			if(StringUtils.isBlank(value)){
				continue;
			}
				
			if(KEY_NAME.equals(key)){
										
				page=value;
				continue;
					
			}
				
			value=value.trim();
				
			String cur=key+"="+value;

			result+=cur+";";
			
			
		}
		
		request.getSession().setAttribute(PARAM_KEY_STAR_WITH+page, result);

		
	}
	
	/**
	 * 清除筛选条件
	 * @param request
	 */
	public static void clearFilterParam(HttpServletRequest request){

		String page=request.getParameter(KEY_NAME);
		
		request.getSession().removeAttribute(PARAM_KEY_STAR_WITH+page);
	}
	
	/**
	 * 从session中读取筛选条件
	 * @param request
	 * @return
	 */
	public static Map<String, String> readFilterParamMap(HttpServletRequest request){
		
		Map<String, String> paraMap = new HashMap<String,String>();
		
		String page=request.getParameter(KEY_NAME);
		
		String result=(String)request.getSession().getAttribute(PARAM_KEY_STAR_WITH+page);
		
		String[] strArray=result.split(";");
		
		for(String str:strArray){
			String[] strs=str.split("=");
			paraMap.put(strs[0], strs[1]);
		}
		
		return paraMap;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
