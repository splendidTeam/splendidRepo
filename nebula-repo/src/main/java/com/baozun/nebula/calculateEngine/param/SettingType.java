package com.baozun.nebula.calculateEngine.param;

/**
 * 设置类型
 * 
 * 
 * 
 * @author
 * 
 */

public class SettingType {
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

	// 判断逻辑代号
	public static final String EXP_FREESHIP = "freeship";//免运费
	public static final String EXP_CUSTOMSETTING = "cstset";//cstset(1,1)自定义条件
	public static final String EXP_ORDDISC = "orddisc";//整单优惠
	public static final String EXP_ORDRATE= "ordrate";//整单折扣
	public static final String EXP_SCPORDDISC = "scporddisc";//范围整单优惠
	public static final String EXP_SCPORDRATE = "scpordrate";//范围整单折扣
	public static final String EXP_SCPPRDDISC = "scpprddisc";//范围单品优惠
	public static final String EXP_SCPPRDRATE = "scpprdrate";//范围单品折扣

	public static final String EXP_SCPPCSDISC = "scppcsdisc";//范围单件优惠
	public static final String EXP_SCPPCSRATE = "scppcsrate";//范围单件折扣
	public static final String EXP_ORDCOUPON = "ordcoupon";//整单优惠券
	public static final String EXP_SCPCOUPON = "scpcoupon";//范围优惠券
	public static final String EXP_SCPGIFT = "scpgift";//范围礼品
	
	public static final String EXP_SCPMKDNPRICE = "scpmrkdnprice";//范围新计价
	
	public static final String ONEPIECEMARK = "1,3";
	public static final String BYPIECE = "pcs";
}
