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
public class ConditionType {
	// 与或分隔符	
	public static final String OR = "|";
	public static final String AND = "&";
	public static final String AND_OR = "&|";
	public static final String NOT_AND = "&!";
	
	// 逻辑分隔符
	public static final String REGEX_BRACKETS = "[\\(\\)]";
	public static final String REGEX_PARAMS_DELIMITER = ",";
	
	//根据取值标准获取括号中的值
	public static final String REGEX_VALUE = "(?<=\\()(.+?)(?=\\))";
	//获取范围表达式
	public static final String REGEX_SOCPE = "([A-Za-z]+\\:[A-Za-z]+\\(+[\\,(0-9)]+\\))|([A-Za-z]+\\(+\\))";
	
	// 判断逻辑代号
	public static final String EXP_NOLIMIT = "nolmt";//nolmt
	public static final String EXP_CUSTOM = "ccdtt";//ccdtt(4,2)自定义条件
	public static final String EXP_ORDAMT = "ordamt";//ordamt(200)整单金额大于等于200
	public static final String EXP_ORDPCS = "ordpcs";//ordpcs(5)整单件数大于等于5件
	public static final String EXP_ORDMARGINRATE = "ordmgnrate";//ordmgnrate(75)整单最大优惠幅度
	
	public static final String EXP_SCPORDAMT = "scpordamt";//scpordamt(500,cid:188)男鞋整单金额大于等于500
	public static final String EXP_SCPORDPCS = "scpordpcs";//scpordpcs(3,cid:188)男鞋整单件数大于等于3
	public static final String EXP_SCPPRDAMT = "scpprdamt";//scpprdamt(300,cid:188)男鞋单品金额大于等于300
	public static final String EXP_SCPPRDPCS = "scpprdpcs";//scpprdpcs(2,cid:188)男鞋单品件数大于等于2
	public static final String EXP_SCPMARGINRATE = "scpmgnrate";//scpmgnrate(75,cid:188)范围内商品最大优惠幅度
	
	public static final String EXP_ORDCOUPON = "ordcoupon";//ordcoupon(2)整单5元券
	public static final String EXP_SCPCOUPON = "scpcoupon";//scpcoupon(1,cid:188)男鞋类10元券
	
	public static final String EXP_CHOICEPRIMPRD = "prmprd";//选购中主商品
	public static final String EXP_CHOICEADDTPRD = "addtprd";//选购中选购商品
		
	public static final String[] CONDITION_EXPRESSION_ARRAY = 
			new String[]{"nolmt","ordamt","ordpcs","scpordamt","scpordpcs","scpprdamt","scpprdpcs","ordcoupon","scpcoupon","ordmgnrate","scpmgnrate","ccdtt"};
}
