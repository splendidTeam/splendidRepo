package com.baozun.nebula.wormhole.scm.timing;

/**
 * 非同步相关的定时调用
 * @author Justin Hu
 *
 */
public interface CommonTimingManager {

	/**
	 * 用于添加在售商品补偿记录的定时器
	 * 一般为每天一次
	 */
	void addOnSalesProductRecord();
}
