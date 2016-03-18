package com.baozun.nebula.utilities.common;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class RequestMapUtil{

    public static void requestConvert(HttpServletRequest request,Map<String, String> responseMap){
        Map<String, String[]> map = request.getParameterMap();
        for (Object key : map.keySet()){
            responseMap.put(key.toString(), Arrays.toString((String[]) map.get(key)).replace("[", "").replace("]", ""));
        }
    }

}
