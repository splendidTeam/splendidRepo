package com.baozun.nebula.model.rule;

public class CustomScopeType {
	/**
	 * 自定义实现类返回值代表的业务意义：1是商品ID
	 */
	public static final Integer	ITEM		= 1;
	/**
	 * 自定义实现类返回值代表的业务意义：2是会员ID
	 */
	public static final Integer	MEMBER		= 2;
	/**
	 * 自定义实现类返回值代表的业务意义：3条件的倍增因子
	 */
	public static final Integer	CONDITION	= 3;
	
	/**
	 * 自定义实现类返回值代表的业务意义：PromotionSettingDetail，
	 * 根据外挂接口，返回自定义逻辑得到的优惠设置对象
	 */
	public static final Integer	SETTING	= 4;	
}
