package com.baozun.nebula.calculateEngine.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.baozun.nebula.calculateEngine.action.AtomicAction;
import com.baozun.nebula.calculateEngine.condition.AbstractScopeConditionResult;
import com.baozun.nebula.calculateEngine.condition.ItemScopeConditionResult;
import com.baozun.nebula.calculateEngine.param.ConditionType;
import com.baozun.nebula.command.rule.ItemTagRuleCommand;
import com.baozun.nebula.model.rule.ItemTagRule;
import com.baozun.nebula.model.rule.MemberTagRule;

/**
 * 用于将组装好的范围放入Map中 解析范围表达式 目前定义了4中表达式，但先实现1,2,4
 * 
 * @author jumbo
 * 
 */
public class ItemSocpeParser {

	/**
	 * 解析范围
	 * 
	 * @param scopeList
	 * @param itemSocpeMap
	 */
	public static void parserScope(List<ItemTagRuleCommand> scopeList, Map<String, AbstractScopeConditionResult> itemSocpeMap) {
		for (ItemTagRuleCommand itemTagRuleCommand : scopeList) {
			if (ItemTagRule.TYPE_PRODUCT.equals(itemTagRuleCommand.getType())) {
				parserForPidScope(itemTagRuleCommand, itemSocpeMap);
			} else if (ItemTagRule.TYPE_CATEGORY.equals(itemTagRuleCommand.getType())) {
				parserForCidScope(itemTagRuleCommand, itemSocpeMap);
			} else if (ItemTagRule.TYPE_CUSTOM.equals(itemTagRuleCommand.getType())) {
				parserForCustomScope(itemTagRuleCommand, itemSocpeMap);
			} else if (ItemTagRule.TYPE_COMBO.equals(itemTagRuleCommand.getType())) {
				parserForCmbScope(itemTagRuleCommand, itemSocpeMap);
			}
		}
	}

	/**
	 * 解析类型为商品
	 * 
	 * @param itemTagRuleCommand
	 *            原始行数据
	 * @param itemSocpeMap
	 *            转换后数据
	 */
	private static void parserForPidScope(ItemTagRuleCommand itemTagRuleCommand, Map<String, AbstractScopeConditionResult> itemSocpeMap) {
		List<String> expressionList = new ArrayList<String>();// 规则列表
		List<AtomicAction> atomicActionList = new ArrayList<AtomicAction>();// 表达式解析集合
		AbstractScopeConditionResult atomicForScopeCondition = new ItemScopeConditionResult();// 解析对象
		String expression = itemTagRuleCommand.getExpression();// 表达式
		Matcher matcherForCondition = SocpeParserUtils.PATTERN_FOR_CONDITION.matcher(expression);
		/**
		 * 将pid:in(1,3,31)|pid:in(2,4,5) 解析成pid:in(1,3,31),pid:in(2,4,5)
		 */
		while (matcherForCondition.find()) {
			expressionList.add(matcherForCondition.group().toString());
			Pattern pattern = Pattern.compile(ConditionType.REGEX_VALUE);
			Matcher matcher = pattern.matcher(matcherForCondition.group());
			/**
			 * 将pid:in(1,3,31) 解析成1,3,31
			 */
			AtomicAction atomicAction = new AtomicAction();
			while (matcher.find()) {
				String scope = matcher.group().toString();
				atomicAction.setScopeAction(scope.split(","));
				atomicAction.setType(ItemTagRule.EXP_PREFIX_PRODUCT);
				atomicActionList.add(atomicAction);
			}
			if (null == atomicAction.getType()) {
				atomicAction.setScopeAction(null);
				atomicAction.setType(ItemTagRule.EXP_PREFIX_CATEGORY);
				atomicActionList.add(atomicAction);
			}
		}
		atomicForScopeCondition.setAtomicActionList(atomicActionList);
		atomicForScopeCondition.setOperList(SocpeParserUtils.getOperList(expressionList, expression));
		itemSocpeMap.put(String.valueOf(itemTagRuleCommand.getId()), atomicForScopeCondition);
	}

	/**
	 * 解析类型为分类
	 * 
	 * @param itemTagRuleCommand
	 *            原始行数据
	 * @param itemSocpeMap
	 *            转换后数据
	 */
	private static void parserForCidScope(ItemTagRuleCommand itemTagRuleCommand, Map<String, AbstractScopeConditionResult> itemSocpeMap) {
		List<String> expressionList = new ArrayList<String>();// 规则列表
		List<AtomicAction> atomicActionList = new ArrayList<AtomicAction>();// 表达式解析集合
		AbstractScopeConditionResult atomicForScopeCondition = new ItemScopeConditionResult();// 解析对象
		String expression = itemTagRuleCommand.getExpression();// 表达式
		Matcher matcherForCondition = SocpeParserUtils.PATTERN_FOR_CONDITION.matcher(expression);
		
		/**
		 * 将pid:in(1,3,31)|cid:in(2,4,5) 解析成pid:in(1,3,31),cid:in(2,4,5)
		 */
		while (matcherForCondition.find()) {
			String scopeRule = matcherForCondition.group().toString();
			expressionList.add(scopeRule);
			Pattern pattern = Pattern.compile(ConditionType.REGEX_VALUE);
			Matcher matcher = pattern.matcher(matcherForCondition.group());
			/**
			 * 将pid:in(1,3,31) 解析成1,3,31
			 */
			String type = "";
			if (scopeRule.startsWith(ItemTagRule.EXP_PREFIX_PRODUCT)) {
				type = ItemTagRule.EXP_PREFIX_PRODUCT;
			}
			if (scopeRule.startsWith(ItemTagRule.EXP_PREFIX_CATEGORY)) {
				type = ItemTagRule.EXP_PREFIX_CATEGORY;
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
				atomicAction.setType(ItemTagRule.EXP_PREFIX_CATEGORY);
				atomicActionList.add(atomicAction);
			}
		}
		atomicForScopeCondition.setAtomicActionList(atomicActionList);
		atomicForScopeCondition.setOperList(SocpeParserUtils.getOperList(expressionList, expression));
		itemSocpeMap.put(String.valueOf(itemTagRuleCommand.getId()), atomicForScopeCondition);
	}

	/**
	 * 解析类型为组合
	 * 
	 * @param itemTagRuleCommand
	 *            原始行数据
	 * @param itemSocpeMap
	 *            转换后数据
	 */
	private static void parserForCmbScope(ItemTagRuleCommand itemTagRuleCommand, Map<String, AbstractScopeConditionResult> itemSocpeMap) {
		List<String> expressionList = new ArrayList<String>();// 规则列表
		List<AbstractScopeConditionResult> atomicActionList = null;// 表达式解析集合
		Map<String, List<AbstractScopeConditionResult>> itemScopeConditionResultMap = new HashMap<String, List<AbstractScopeConditionResult>>();
		AbstractScopeConditionResult atomicForScopeCondition = new ItemScopeConditionResult();// 解析对象
		String expression = itemTagRuleCommand.getExpression();// 表达式
		Matcher matcherForCondition = SocpeParserUtils.PATTERN_FOR_CONDITION.matcher(expression);
		/**
		 * 将cmb:in(1,3,31)|cmb:in(2,4,5) 解析成cmb:in(1,3,31),cmb:in(2,4,5)
		 */
		int j = 0;
		while (matcherForCondition.find()) {
			j++;
			String str = matcherForCondition.group().toString();
			expressionList.add(str);
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
					atomicActionList.add(itemSocpeMap.get(ids[i]));
				}
			}
			itemScopeConditionResultMap.put(String.valueOf(j), atomicActionList);
		}
		atomicForScopeCondition.setScopeConditionResultMap(itemScopeConditionResultMap);
		atomicForScopeCondition.setOperList(SocpeParserUtils.getOperList(expressionList, expression));
		itemSocpeMap.put(String.valueOf(itemTagRuleCommand.getId()), atomicForScopeCondition);
	}
	/**
	 * 解析类型为自定义商品范围
	 * 
	 * @param itemTagRuleCommand
	 *            原始行数据
	 * @param itemSocpeMap
	 *            转换后数据
	 */
	private static void parserForCustomScope(ItemTagRuleCommand itemTagRuleCommand, Map<String, AbstractScopeConditionResult> itemSocpeMap) {
		List<String> expressionList = new ArrayList<String>();// 规则列表
		List<AtomicAction> atomicActionList = new ArrayList<AtomicAction>();// 表达式解析集合
		AbstractScopeConditionResult atomicForScopeCondition = new ItemScopeConditionResult();// 解析对象
		String expression = itemTagRuleCommand.getExpression();// 表达式
		Matcher matcherForCondition = SocpeParserUtils.PATTERN_FOR_CONDITION.matcher(expression);
		/**
		 * 将cctg:in(1,3,31)|cctg:in(2,4,5) 解析成cctg:in(1,3,31),cctg:in(2,4,5)
		 */
		while (matcherForCondition.find()) {
			expressionList.add(matcherForCondition.group().toString());
			Pattern pattern = Pattern.compile(ConditionType.REGEX_VALUE);
			Matcher matcher = pattern.matcher(matcherForCondition.group());
			/**
			 * 将cctg:in(1,3,31) 解析成1,3,31
			 */
			AtomicAction atomicAction = new AtomicAction();
			while (matcher.find()) {
				String scope = matcher.group().toString();
				atomicAction.setScopeAction(scope.split(","));
				atomicAction.setType(ItemTagRule.EXP_PREFIX_CUSTOM);
				atomicActionList.add(atomicAction);
			}
			if (null == atomicAction.getType()) {
				atomicAction.setScopeAction(null);
				atomicAction.setType(ItemTagRule.EXP_PREFIX_CUSTOM);
				atomicActionList.add(atomicAction);
			}
		}
		atomicForScopeCondition.setAtomicActionList(atomicActionList);
		atomicForScopeCondition.setOperList(SocpeParserUtils.getOperList(expressionList, expression));
		itemSocpeMap.put(String.valueOf(itemTagRuleCommand.getId()), atomicForScopeCondition);
	}
}
