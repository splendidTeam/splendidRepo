package com.baozun.nebula.calculateEngine.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.baozun.nebula.calculateEngine.action.AtomicAction;
import com.baozun.nebula.calculateEngine.condition.AbstractScopeConditionResult;
import com.baozun.nebula.calculateEngine.condition.CrowdScopeConditionResult;
import com.baozun.nebula.calculateEngine.condition.ItemScopeConditionResult;
import com.baozun.nebula.calculateEngine.param.ConditionType;
import com.baozun.nebula.command.rule.MemberTagRuleCommand;
import com.baozun.nebula.model.rule.MemberTagRule;

/**
 * 用于将组装好的范围放入Map中 解析范围表达式 目前定义了4中表达式，但先实现1,2,4
 * 
 * @author jumbo
 * 
 */
public class CrowdSocpeParser {

	/**
	 * 解析范围
	 * 
	 * @param originalsocpeMap
	 * @param itemSocpeMap
	 */
	public static void parserScope(List<MemberTagRuleCommand> scopeList, Map<String, AbstractScopeConditionResult> itemSocpeMap) {
		for (MemberTagRuleCommand memberTagRuleCommand : scopeList) {
			if (MemberTagRule.TYPE_MEMBER.equals(memberTagRuleCommand.getType())) {
				parserForMidScope(memberTagRuleCommand, itemSocpeMap);
			} else if (MemberTagRule.TYPE_GROUP.equals(memberTagRuleCommand.getType())) {
				parserForGrpidScope(memberTagRuleCommand, itemSocpeMap);
			} else if (MemberTagRule.TYPE_CUSTOM.equals(memberTagRuleCommand.getType())) {
				parserForCustomScope(memberTagRuleCommand, itemSocpeMap);
			} else if (MemberTagRule.TYPE_COMBO.equals(memberTagRuleCommand.getType())) {
				parserForCmbScope(memberTagRuleCommand, itemSocpeMap);
			}
		}
	}

	/**
	 * 解析类型为会员
	 * 
	 * @param memberTagRuleCommand
	 *            原始行数据
	 * @param crowdSocpeMap
	 *            转换后数据
	 */
	private static void parserForMidScope(MemberTagRuleCommand memberTagRuleCommand, Map<String, AbstractScopeConditionResult> crowdSocpeMap) {
		List<String> expressionList = new ArrayList<String>();
		List<AtomicAction> atomicActionList = new ArrayList<AtomicAction>();
		AbstractScopeConditionResult atomicForScopeCondition = new CrowdScopeConditionResult();
		String expression = memberTagRuleCommand.getExpression();
		Matcher matcherForCondition = SocpeParserUtils.PATTERN_FOR_CONDITION.matcher(expression);
		/**
		 * 将mid:in(1,3,31)|mid:in(2,4,5) 解析成mid:in(1,3,31),mid:in(2,4,5)
		 */
		while (matcherForCondition.find()) {
			expressionList.add(matcherForCondition.group().toString());
			Pattern pattern = Pattern.compile(ConditionType.REGEX_VALUE);
			Matcher matcher = pattern.matcher(matcherForCondition.group());
			/**
			 * 将mid:in(1,3,31) 解析成1,3,31
			 */
			AtomicAction atomicAction = new AtomicAction();
			while (matcher.find()) {
				String scope = matcher.group().toString();
				String[] scopeList = scope.split(",");
				atomicAction.setScopeAction(scopeList);
				atomicAction.setType(MemberTagRule.EXP_PREFIX_MEMBER);
				atomicActionList.add(atomicAction);
			}
			if (null == atomicAction.getType()) {
				atomicAction.setScopeAction(null);
				atomicAction.setType(MemberTagRule.EXP_PREFIX_MEMBER_GROUP);
				atomicActionList.add(atomicAction);
			}
		}
		atomicForScopeCondition.setAtomicActionList(atomicActionList);
		atomicForScopeCondition.setOperList(SocpeParserUtils.getOperList(expressionList, expression));
		crowdSocpeMap.put(String.valueOf(memberTagRuleCommand.getId()), atomicForScopeCondition);
	}

	/**
	 * 解析类型为会员分组
	 * 
	 * @param memberTagRuleCommand
	 *            原始行数据
	 * @param crowdSocpeMap
	 *            转换后数据
	 */
	private static void parserForGrpidScope(MemberTagRuleCommand memberTagRuleCommand, Map<String, AbstractScopeConditionResult> crowdSocpeMap) {
		List<String> expressionList = new ArrayList<String>();
		List<AtomicAction> atomicActionList = new ArrayList<AtomicAction>();
		AbstractScopeConditionResult atomicForScopeCondition = new CrowdScopeConditionResult();
		String expression = memberTagRuleCommand.getExpression();
		Matcher matcherForCondition = SocpeParserUtils.PATTERN_FOR_CONDITION.matcher(expression);
		/**
		 * 将mid:in(1,3,31)|grpid:in(2,4,5) 解析成mid:in(1,3,31),grpid:in(2,4,5)
		 */
		while (matcherForCondition.find()) {
			String scopeRule = matcherForCondition.group().toString();
			expressionList.add(scopeRule);
			Pattern pattern = Pattern.compile(ConditionType.REGEX_VALUE);
			Matcher matcher = pattern.matcher(matcherForCondition.group());
			/**
			 * 将mid:in(1,3,31) 解析成1,3,31
			 */
			String type = "";
			if (scopeRule.startsWith(MemberTagRule.EXP_PREFIX_MEMBER)) {
				type = MemberTagRule.EXP_PREFIX_MEMBER;
			}
			if (scopeRule.startsWith(MemberTagRule.EXP_PREFIX_MEMBER_GROUP)) {
				type = MemberTagRule.EXP_PREFIX_MEMBER_GROUP;
			}
			AtomicAction atomicAction = new AtomicAction();
			while (matcher.find()) {
				String scope = matcher.group().toString();
				String[] scopeList = scope.split(",");
				atomicAction.setScopeAction(scopeList);
				atomicAction.setType(type);
				atomicActionList.add(atomicAction);
			}
			if (null == atomicAction.getType()) {
				atomicAction.setScopeAction(null);
				atomicAction.setType(MemberTagRule.EXP_PREFIX_MEMBER_GROUP);
				atomicActionList.add(atomicAction);
			}
		}
		atomicForScopeCondition.setAtomicActionList(atomicActionList);
		atomicForScopeCondition.setOperList(SocpeParserUtils.getOperList(expressionList, expression));
		crowdSocpeMap.put(String.valueOf(memberTagRuleCommand.getId()), atomicForScopeCondition);
	}

	/**
	 * 解析类型为组合
	 * 
	 * @param memberTagRuleCommand
	 *            原始行数据
	 * @param crowdSocpeMap
	 *            转换后数据
	 */
	private static void parserForCmbScope(MemberTagRuleCommand memberTagRuleCommand, Map<String, AbstractScopeConditionResult> crowdSocpeMap) {
		List<String> expressionList = new ArrayList<String>();// 规则列表
		List<AbstractScopeConditionResult> atomicActionList = null;// 表达式解析集合
		Map<String, List<AbstractScopeConditionResult>> crowdScopeConditionResultMap = new HashMap<String, List<AbstractScopeConditionResult>>();
		AbstractScopeConditionResult atomicForScopeCondition = new CrowdScopeConditionResult();// 解析对象
		String expression = memberTagRuleCommand.getExpression();// 表达式
		Matcher matcherForCondition = SocpeParserUtils.PATTERN_FOR_CONDITION.matcher(expression);
		/**
		 * 将cmb:in(1,3,31)|cmb:in(2,4,5) 解析成cmb:in(1,3,31),cmb:in(2,4,5)
		 */
		int j = 0;
		while (matcherForCondition.find()) {
			j++;
			expressionList.add(matcherForCondition.group().toString());
			Pattern pattern = Pattern.compile(ConditionType.REGEX_VALUE);
			Matcher matcher = pattern.matcher(matcherForCondition.group());
			/**
			 * 将cmb:in(1,3,31) 解析成1,3,31
			 */
			while (matcher.find()) {
				atomicActionList = new ArrayList<AbstractScopeConditionResult>();
				String id = matcher.group().toString();
				String[] ids = id.split(",");
				for (int i = 0; i < ids.length; i++) {
					atomicActionList.add(crowdSocpeMap.get(ids[i]));
				}
			}
			crowdScopeConditionResultMap.put(String.valueOf(j), atomicActionList);
		}
		atomicForScopeCondition.setScopeConditionResultMap(crowdScopeConditionResultMap);
		atomicForScopeCondition.setOperList(SocpeParserUtils.getOperList(expressionList, expression));
		crowdSocpeMap.put(String.valueOf(memberTagRuleCommand.getId()), atomicForScopeCondition);
	}
	
	/**
	 * 解析类型为自定义会员范围
	 * 
	 * @param memberTagRuleCommand
	 *            原始行数据
	 * @param crowdSocpeMap
	 *            转换后数据
	 */
	private static void parserForCustomScope(MemberTagRuleCommand memberTagRuleCommand, Map<String, AbstractScopeConditionResult> crowdSocpeMap) {
		List<String> expressionList = new ArrayList<String>();
		List<AtomicAction> atomicActionList = new ArrayList<AtomicAction>();
		AbstractScopeConditionResult atomicForScopeCondition = new CrowdScopeConditionResult();
		String expression = memberTagRuleCommand.getExpression();
		Matcher matcherForCondition = SocpeParserUtils.PATTERN_FOR_CONDITION.matcher(expression);
		/**
		 * 将cgrpid:in(1,3,31)|cgrpid:in(2,4,5) 解析成cgrpid:in(1,3,31),cgrpid:in(2,4,5)
		 */
		while (matcherForCondition.find()) {
			expressionList.add(matcherForCondition.group().toString());
			Pattern pattern = Pattern.compile(ConditionType.REGEX_VALUE);
			Matcher matcher = pattern.matcher(matcherForCondition.group());
			/**
			 * 将cgrpid:in(1,3,31) 解析成1,3,31
			 */
			AtomicAction atomicAction = new AtomicAction();
			while (matcher.find()) {
				String scope = matcher.group().toString();
				String[] scopeList = scope.split(",");
				atomicAction.setScopeAction(scopeList);
				atomicAction.setType(MemberTagRule.EXP_PREFIX_CUSTOM);
				atomicActionList.add(atomicAction);
			}
			if (null == atomicAction.getType()) {
				atomicAction.setScopeAction(null);
				atomicAction.setType(MemberTagRule.EXP_PREFIX_CUSTOM);
				atomicActionList.add(atomicAction);
			}
		}
		atomicForScopeCondition.setAtomicActionList(atomicActionList);
		atomicForScopeCondition.setOperList(SocpeParserUtils.getOperList(expressionList, expression));
		crowdSocpeMap.put(String.valueOf(memberTagRuleCommand.getId()), atomicForScopeCondition);
	}
}
