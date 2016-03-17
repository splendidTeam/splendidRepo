package com.baozun.nebula.wormhole.scm.handler;

import java.util.List;

import com.baozun.nebula.wormhole.mq.entity.SkuInventoryV5;

/**
 * 库存相关的handler
 * @author Justin Hu
 *
 */
public interface SyncInventoryHandler extends HandlerBase {

	/**
	 * 同步库存的扩展点(增量)
	 */
	public void syncIncrementInventory(SkuInventoryV5 si);
	
	
	/**
	 * 同步库存的扩展点(全量)
	 */
	public void syncFullInventory(List<SkuInventoryV5> siList);
}
