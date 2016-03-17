package com.baozun.nebula.solr.utils;

/**
 * 
 * @author Dean Lu
 * @Description 该类提供排除方法的KEY值的转换，类型引用自SkuItemParam方法中的常量
 *
 */
public class ConvertExcludeList {
	
	public static String convertExcludeKey(String type,Long id){
		if(Validator.isNotNullOrEmpty(type)){
			return type+String.valueOf(id);
		}else{
			return null;
		}
	}

}
