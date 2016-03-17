package com.baozun.nebula.model.rule;

public interface TagRule {

	/** 类型-组合 */
	public static final Integer TYPE_COMBO = 4;
	
	/** 表达式-组合 */
	public static final String EXP_COMBO = "cmbid:in(?)";
	
	/** 表达式前缀-组合 */
	public static final String EXP_PREFIX_COMBO = "cmbid";
	
	/** 正则-或 */
	public static final String REGEX_OR = "\\|";
	/** 正则-与 */
	public static final String REGEX_AND = "&";
	/** 正则-非 */
	public static final String REGEX_NOT = "!";
	/** 正则-分隔符（逗号） */
	public static final String REGEX_DELIMITER = ",";
	/** 正则-分隔符（分号） */
	public static final String REGEX_SEMICOLON = ";";
	/** 正则-排除连接符 */
	public static final String REGEX_EXCLUDE_CONNECT = "&!";
	/** 正则-占位符 */
	public static final String REGEX_PLACEHOLDER = "?";
	/** 正则-小括号中的数值 */
	public static final String REGEX_IN_BRACKETS = "\\(([0-9,]*)\\)";	
	/** 正则-表达式*/
	public static final String REGEX_EXPRESSION = "\\(([0-9,]+)\\)";
	
	/** 解析相关-包含列表的key */
	public static final String ANALYSIS_KEY_IN = "in";
	/** 解析相关-排除列表的key */
	public static final String ANALYSIS_KEY_OUT = "out";
	
}
