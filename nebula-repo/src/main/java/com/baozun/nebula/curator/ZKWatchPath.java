package com.baozun.nebula.curator;

import java.util.Map;

import com.feilong.core.Validator;

public class ZKWatchPath {

	private Map<String,String> pathMap ;
	
	public String getZKWatchPath(Class watch){
		if(Validator.isNotNullOrEmpty(pathMap)){
			return pathMap.get(watch.getSimpleName());
		}else {
			return null;
		}
	}

	public Map<String, String> getPathMap() {
		return pathMap;
	}

	public void setPathMap(Map<String, String> pathMap) {
		this.pathMap = pathMap;
	}
	
}
