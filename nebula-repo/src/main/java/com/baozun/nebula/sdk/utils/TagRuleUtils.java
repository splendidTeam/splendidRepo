package com.baozun.nebula.sdk.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.baozun.nebula.command.rule.MiniTagRuleCommand;
import com.baozun.nebula.exception.BusinessException;
import com.baozun.nebula.model.rule.ItemTagRule;
import com.baozun.nebula.model.rule.MemberTagRule;
import com.baozun.nebula.sdk.constants.Constants;

/**
 * 促销相关表达式解析工具类
 * 
 * @author 项硕
 */
public class TagRuleUtils {

	/**
	 * 解析会员表达式，获取其中数字所对应的集合<br>
	 * 返回的map中包含两个key：<br>
	 * 1."in"表示包含列表<br>
	 * 2."out"表示排除列表<br>
	 * @param exp
	 * @return
	 */
	public static Map<String, Set<MiniTagRuleCommand>> member(String exp) {
		Map<String, Set<MiniTagRuleCommand>> rs = new HashMap<String, Set<MiniTagRuleCommand>>();
		Set<MiniTagRuleCommand> inSet = new HashSet<MiniTagRuleCommand>();
		Set<MiniTagRuleCommand> outSet = new HashSet<MiniTagRuleCommand>();
		rs.put(MemberTagRule.ANALYSIS_KEY_IN, inSet);
		rs.put(MemberTagRule.ANALYSIS_KEY_OUT, outSet);
		
		Pattern pattern = Pattern.compile(MemberTagRule.REGEX_IN_BRACKETS);
		String[] expArr = exp.split(MemberTagRule.REGEX_AND);
		for (int i = 0; i < expArr.length; i++) {
			String s = expArr[i];
			if (MemberTagRule.EXP_ALLMEMBER.equals(s)) continue;	// 会员全体,表达式内无ID，直接跳过
			
			boolean isOut = s.startsWith(MemberTagRule.REGEX_NOT);	// 是否排除
			int type = getMemberType(isOut ? s.substring(1) : s);	// 去除感叹号
			Matcher matcher = pattern.matcher(s);
			if (matcher.find()) {
				String ids = matcher.group(1);
				Set<MiniTagRuleCommand> temp = isOut ? outSet : inSet;
				for (String id : Arrays.asList(ids.split(MemberTagRule.REGEX_DELIMITER))) {
					MiniTagRuleCommand cmd = new MiniTagRuleCommand();
					cmd.setId(new Long(id));
					cmd.setType(type);
					temp.add(cmd);
				}
			}
		}
		return rs;
	}

	/**
	 * 解析商品表达式，获取其中数字所对应的集合<br>
	 * 返回的map中包含两个key：<br>
	 * 1."in"表示包含列表<br>
	 * 2."out"表示排除列表<br>
	 * @param exp
	 * @return
	 */
	public static Map<String, Set<MiniTagRuleCommand>> product(String exp) {
		Map<String, Set<MiniTagRuleCommand>> rs = new HashMap<String, Set<MiniTagRuleCommand>>();
		Set<MiniTagRuleCommand> inSet = new HashSet<MiniTagRuleCommand>();
		Set<MiniTagRuleCommand> outSet = new HashSet<MiniTagRuleCommand>();
		rs.put(MemberTagRule.ANALYSIS_KEY_IN, inSet);
		rs.put(MemberTagRule.ANALYSIS_KEY_OUT, outSet);
		
		Pattern pattern = Pattern.compile(ItemTagRule.REGEX_IN_BRACKETS);
		String[] expArr = exp.split(ItemTagRule.REGEX_AND);
		for (int i = 0; i < expArr.length; i++) {
			String s = expArr[i];
			if (ItemTagRule.EXP_ALLPRODUCT.equals(s)) continue;	// 商品全场,表达式内无ID，直接跳过
			
			boolean isOut = s.startsWith(ItemTagRule.REGEX_NOT);	// 是否排除
			int type = getItemType(isOut ? s.substring(1) : s);	// 去除感叹号
			Matcher matcher = pattern.matcher(s);
			if (matcher.find()) {
				String ids = matcher.group(1);
				Set<MiniTagRuleCommand> temp = isOut ? outSet : inSet;
				for (String id : Arrays.asList(ids.split(ItemTagRule.REGEX_DELIMITER))) {
					MiniTagRuleCommand cmd = new MiniTagRuleCommand();
					cmd.setId(new Long(id));
					cmd.setType(type);
					temp.add(cmd);
				}
			}
		}
		return rs;
	}
	
	/**
	 * 根据会员表达式，获取其对应的类型
	 * @param exp
	 * @return
	 */
	public static int getMemberType(String exp) {
		if (exp.startsWith(MemberTagRule.EXP_PREFIX_MEMBER)) {
			return MemberTagRule.TYPE_MEMBER;
		} else if (exp.startsWith(MemberTagRule.EXP_PREFIX_MEMBER_GROUP) ||
				exp.startsWith(MemberTagRule.EXP_PREFIX_ALLMEMBER)) {
			return MemberTagRule.TYPE_GROUP;
		} else if (exp.startsWith(MemberTagRule.EXP_PREFIX_CUSTOM)) {
			return MemberTagRule.TYPE_CUSTOM;
		} else if (exp.startsWith(MemberTagRule.EXP_PREFIX_COMBO)) {
			return MemberTagRule.TYPE_COMBO;
		} else {
			throw new BusinessException(Constants.MEMBER_CUSTOM_GROUP_ERROR_EXPRESSION);
		}
	}

	/**
	 * 根据商品表达式，获取其对应的类型
	 * @param exp
	 * @return
	 */
	public static int getItemType(String exp) {
		if (exp.startsWith(ItemTagRule.EXP_PREFIX_PRODUCT)) {
			return ItemTagRule.TYPE_PRODUCT;
		} else if (exp.startsWith(ItemTagRule.EXP_PREFIX_CATEGORY) ||
				exp.startsWith(ItemTagRule.EXP_PREFIX_ALLPRODUCT)) {
			return ItemTagRule.TYPE_CATEGORY;
		} else if (exp.startsWith(ItemTagRule.EXP_PREFIX_CUSTOM)) {
			return ItemTagRule.TYPE_CUSTOM;
		} else if (exp.startsWith(ItemTagRule.EXP_PREFIX_COMBO)) {
			return ItemTagRule.TYPE_COMBO;
		} else {
			throw new BusinessException(Constants.PRODUCT_FILTER_ERROR);
		}
	}
}
