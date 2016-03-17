package com.baozun.nebula.calculateEngine.param;

/**
 * Normal常规Step阶梯Choice选购NormalStep常规加阶梯NormalChoice常规加选购
 * @author
 * 
 */
//UI用户选择的条件类型
public class ConditionMasterType {
	
	/* 条件类型（区分大小写） */
	public static final String EXP_NORMAL = "Normal";//nolmt
	public static final String EXP_STEP = "Step";//ordamt(200)整单金额大于等于200
	public static final String EXP_CHOICE = "Choice";//ordpcs(5)整单件数大于等于5件
	public static final String EXP_NORMALSTEP = "NormalStep";//scpordamt(500,cid:188)男鞋整单金额大于等于500
	public static final String EXP_NORMALCHOICE = "NormalChoice";//scpordpcs(3,cid:188)男鞋整单件数大于等于3
	
	/* 促销优先级 */
	public static final Integer PRIORITY_DEFAULT = 10;
	public static final Integer PRIORITY_NORMAL = 5;
	public static final Integer PRIORITY_STEP = 2;
	public static final Integer PRIORITY_CHOICE = 1;
	public static final Integer PRIORITY_NORMAL_STEP = 4;
	public static final Integer PRIORITY_NORMAL_CHOICE = 3;
	
}
