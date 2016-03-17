package com.baozun.nebula.utilities.common;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public class RequestMapUtil {

	@SuppressWarnings("unchecked")
	public static void requestConvert(HttpServletRequest request,
			Map<String, String> responseMap) {
		Map<Object,Object> parameterMap = request.getParameterMap();
		for (Object key : parameterMap.keySet()) {
			responseMap.put(key.toString(), Arrays.toString((String[])parameterMap.get(key)).replace("[", "").replace("]", ""));
		}
	}

}
