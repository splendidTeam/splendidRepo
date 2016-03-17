package com.baozun.nebula.wormhole.scm.timing;

/**
 * SCM同步物流信息到商城
 * @author Justin Hu
 *
 */
public interface SyncLogisticManager {

	/**
	 * 同步物流信息到商城
	 * 商城将物流信息保存下来
	 */
	public void syncLogisticInfo();
}
