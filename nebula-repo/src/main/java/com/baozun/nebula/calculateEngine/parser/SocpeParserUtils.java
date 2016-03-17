package com.baozun.nebula.calculateEngine.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.baozun.nebula.calculateEngine.param.ConditionType;

public class SocpeParserUtils {

	/**
	 * 正则验证"[A-Za-z]+\\:[A-Za-z]+\\(+[\\,(0-9)]+\\)"
	 */
	public static Pattern PATTERN_FOR_CONDITION = Pattern.compile(ConditionType.REGEX_SOCPE);
	

	/**
	 * 返回逻辑符号集合
	 * 
	 * @param expressionList
	 *            表达式集合以逻辑符号分割
	 * @param expression
	 *            表达式
	 * @return
	 */
	public static List<String> getOperList(List<String> expressionList, String expression) {
		String newExpression = expression;
		List<String> operList = new ArrayList<String>();// 逻辑符号列表
		for (String exp : expressionList) {
			newExpression = replaceFirst(newExpression,exp,"");
			if (newExpression.startsWith(ConditionType.OR)) {
				operList.add(ConditionType.OR);
				newExpression = replaceFirst(newExpression,ConditionType.OR,"");
			} else if (newExpression.startsWith(ConditionType.NOT_AND)) {
				operList.add(ConditionType.NOT_AND);
				newExpression = replaceFirst(newExpression,ConditionType.NOT_AND,"");
			} else if (newExpression.startsWith(ConditionType.AND)) {
				operList.add(ConditionType.AND);
				newExpression = replaceFirst(newExpression,ConditionType.AND,"");
			}
		}
		return operList;
	}
	
	static public String replaceFirst(String str, String oldStr, String newStr)
	   {
	     int i = str.indexOf(oldStr);
	     if (i == -1) return str;
	     str = str.substring(0, i) + newStr + str.substring(i + oldStr.length());
	     return str;
	   }
}
