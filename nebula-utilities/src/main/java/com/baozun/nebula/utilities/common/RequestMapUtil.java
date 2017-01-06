package com.baozun.nebula.utilities.common;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

public class RequestMapUtil{

    public static void requestConvert(HttpServletRequest request,Map<String, String> responseMap){
        Map<String, String[]> map = request.getParameterMap();
        for (Object key : map.keySet()){
            responseMap.put(key.toString(), Arrays.toString((String[]) map.get(key)).replace("[", "").replace("]", ""));
        }
    }
    
    public static Map<String, String> convertToStringParamsMap(Map<String, Object> params) {
		Map<String, String> res = new HashMap<String, String>();
		Set<String> keySet = params.keySet();
		for(String key : keySet){
			Object val = params.get(key);
			if(null!=val){
				String paramVal = val + "";
				res.put(key, paramVal);
			}
		}
		return res;
	}

}
