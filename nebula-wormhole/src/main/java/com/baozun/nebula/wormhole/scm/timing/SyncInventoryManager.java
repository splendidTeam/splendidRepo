package com.baozun.nebula.wormhole.scm.timing;

/**
 * 定时取出MsgReceiveContent表库存信息接口相关未处理的数据
 * 进行处理
 * @author Justin Hu
 *
 */
public interface SyncInventoryManager {

	
	/**
	 * 同步库存(增量)
	 * scm推送库存信息到商城
	 */
	public void syncIncrementInventory();
	
	
	/**
	 * 同步库存(全量)
	 * scm推送库存信息到商城
	 */
	public void syncFullInventory();
}
