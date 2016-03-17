package com.baozun.nebula.calculateEngine.param;

/**
 * 判断逻辑
 * 
 * 
 * 
 * @author
 * 
 */
//条件表达式相关类型
public class PurchaseLimitConditionType {
	// 与或分隔符	
	public static final String OR = "|";
	public static final String AND = "&";
	public static final String AND_OR = "&|";
	public static final String NOT_AND = "&!";
	
	/** 换行符：表达式中文文本所用分隔符 */
	public static final String NEW_LINE = "\n";

	// 逻辑分隔符
	public static final String REGEX_BRACKETS = "[\\(\\)]";
	public static final String REGEX_PARAMS_DELIMITER = ",";
	
	//根据取值标准获取括号中的值
	public static final String REGEX_VALUE = "(?<=\\()(.+?)(?=\\))";
	//获取范围表达式
	public static final String REGEX_SOCPE = "([A-Za-z]+\\:[A-Za-z]+\\(+[\\,(0-9)]+\\))|([A-Za-z]+\\(+\\))";

	// 判断逻辑代号
	public static final String EXP_ORDSKUQTY = "ordskuqty";//订单内单品件数
	public static final String EXP_ORDITEMQTY = "orditemqty";//订单内商品数
	public static final String EXP_ORDQTY = "ordqty";//订单内件数
	public static final String EXP_HISORDSKUQTY = "hisordskuqty";//历史购买件数
	public static final String EXP_HISORDITEMQTY = "hisorditemqty";//历史购买商品数
	public static final String EXP_HISORDQTY = "histordqty";//历史购买订单数

	public static final String[] CONDITION_EXPRESSION_ARRAY = 
			new String[]{"ordskuqty","orditemqty","ordqty","hisordskuqty","hisorditemqty","histordqty"};
}
