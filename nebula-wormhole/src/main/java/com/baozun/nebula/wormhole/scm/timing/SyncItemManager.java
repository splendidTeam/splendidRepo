package com.baozun.nebula.wormhole.scm.timing;

/**
 * 定时取出MsgReceiveContent表商品信息接口相关未处理的数据
 * 进行处理
 * @author Justin Hu
 *
 */
public interface SyncItemManager {

	/**
	 * 同步商品基础信息
	 * scm推送商品信息到商城
	 */
	public void syncBaseInfo();
	
	/**
	 * 同步商品价格
	 * SCM推送商品价格到商城
	 */
	public void syncItemPrice();
}
