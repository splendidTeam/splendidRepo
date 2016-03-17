package com.baozun.nebula.solr.utils;

import com.baozun.nebula.solr.Param.SkuItemParam;

public class FilterUtil {
	
	/**
	 * 不能用于时间范围
	 * @param start
	 * @param end
	 * @return
	 */
	 public static String paramConverToArea(String start,String end){
		 String param = SkuItemParam.pattern_area;
		 if(Validator.isNotNullOrEmpty(start)){
			 param = param.replace("startWord", start);
		 }else{
			 param = param.replace("startWord", "0");
		 }
		 
		 if(Validator.isNotNullOrEmpty(end)){
			 param = param.replace("endWord", end);
		 }else{
			 param = param.replace("endWord", "*");
		 }
		 return param;
	 }
	
}
