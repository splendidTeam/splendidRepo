package com.baozun.nebula.calculateEngine.common;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.baozun.nebula.calculateEngine.condition.AbstractScopeConditionResult;
import com.baozun.nebula.calculateEngine.utils.ConvertParamToEngine;

@Component
public class ItemScopeEngine implements Engine {

	private Map<String, AbstractScopeConditionResult> itemScopeMap;

	public Map<String, AbstractScopeConditionResult> getItemScopeMap() {
		return itemScopeMap;
	}

	public void setItemScopeMap(Map<String, AbstractScopeConditionResult> itemScopeMap) {
		this.itemScopeMap = itemScopeMap;
	}

	@Override
	public <T> void buildEngine(List<T> scopeList) {
		itemScopeMap = new ConvertParamToEngine().convertItemSocpeList(scopeList);
	}

}
