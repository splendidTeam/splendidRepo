package com.baozun.nebula.calculateEngine.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.baozun.nebula.calculateEngine.condition.AbstractScopeConditionResult;
import com.baozun.nebula.calculateEngine.parser.CrowdSocpeParser;
import com.baozun.nebula.command.rule.MemberTagRuleCommand;

public class ConvertParamToCrowdEngine {

	/**
	 * 将scopeList中的信息转换成ID+表达式的方式
	 * 
	 * @param <T>
	 */
	@SuppressWarnings("unchecked")
	public <T> Map<String, AbstractScopeConditionResult> convertItemSocpeList(List<T> scopeList) {
		Map<String, AbstractScopeConditionResult> crowdScopeMap = new HashMap<String, AbstractScopeConditionResult>();
		CrowdSocpeParser.parserScope((List<MemberTagRuleCommand>) scopeList, crowdScopeMap);
		return crowdScopeMap;
	}
}
