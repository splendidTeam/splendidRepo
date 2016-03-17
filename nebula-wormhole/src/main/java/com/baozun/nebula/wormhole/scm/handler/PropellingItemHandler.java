package com.baozun.nebula.wormhole.scm.handler;

import java.util.List;

import com.baozun.nebula.wormhole.mq.entity.sku.OnSaleSkuV5;

/**
 * 商品推送相关的handler
 * @author Justin Hu
 *
 */
public interface PropellingItemHandler extends HandlerBase {

	/**
	 * 
	 * @param msr
	 * @return
	 */
	public List<OnSaleSkuV5> propellingOnSalesItems(List<OnSaleSkuV5> ssList);
}
