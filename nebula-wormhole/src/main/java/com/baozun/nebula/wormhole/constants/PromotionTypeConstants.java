package com.baozun.nebula.wormhole.constants;

/**
 * 促销类型,并不是商城端所理解的满500减100这种促销类型
 * 而是指整单，单行的类型
 * 1为整单促销
 * 2为单行促销
 * 3为整单拆分到行的促销
 * 
 * @author lxy
 * 
 */
public class PromotionTypeConstants {


	public static final String PROMOTION_ALL = "1";

	public static final String PROMOTION_LINE = "2";
	
	public static final String PROMOTION_ALLONLINE = "3";
}
