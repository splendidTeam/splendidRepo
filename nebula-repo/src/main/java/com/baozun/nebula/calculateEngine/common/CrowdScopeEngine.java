package com.baozun.nebula.calculateEngine.common;

import java.util.List;
import java.util.Map;

import com.baozun.nebula.calculateEngine.condition.AbstractScopeConditionResult;
import com.baozun.nebula.calculateEngine.utils.ConvertParamToCrowdEngine;

public class CrowdScopeEngine implements Engine {

	private Map<String, AbstractScopeConditionResult> crowdScopeMap;

	@Override
	public <T> void buildEngine(List<T> scopeList) {
		crowdScopeMap = new ConvertParamToCrowdEngine().convertItemSocpeList(scopeList);
	}

	public Map<String, AbstractScopeConditionResult> getCrowdScopeMap() {
		return crowdScopeMap;
	}

	public void setCrowdScopeMap(Map<String, AbstractScopeConditionResult> crowdScopeMap) {
		this.crowdScopeMap = crowdScopeMap;
	}

}
