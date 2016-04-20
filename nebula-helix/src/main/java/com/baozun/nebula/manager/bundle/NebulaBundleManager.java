/**
 * Copyright (c) 2012 Baozun All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Baozun.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Baozun.
 *
 * BAOZUN MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. BAOZUN SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 */
package com.baozun.nebula.manager.bundle;

import java.util.List;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.command.bundle.BundleCommand;
import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.web.command.BundleValidateResult;


public interface NebulaBundleManager extends BaseManager {

	/**
	 * <h3>查询bundle信息</h3>
	 * <ul>
	 *    <li>pdp入口 ： 可能会有多条记录</li>
	 * </ul>
	 * 
	 * @param itemId : bundle捆绑类商品中主卖品的商品Id
	 * @return
	 */
	public List<BundleCommand> findBundleCommandByItemId(Long itemId);
	/**
	 * 
	 * <h3>查询bundle信息</h3>
	 * <ul>
	 *    <li>bundle列表入口 ： 只会查询出一条记录</li>
	 * </ul>
	 * @param boundleId
	 * @return
	 */
	public BundleCommand findBundleCommandByBundleId(Long boundleId);
	
	/**
	 * <h3>bundle信息分页查询</h3>
	 * @param page
	 * @param sorts
	 * @return
	 */
	public Pagination<BundleCommand> findBundleCommandByPage(Page page, Sort[] sorts);
	
	/**
	 * <h3>下单 加入购物车时验证bundle的信息</h3>
	 * <ul>
	 *    <ol>
	 *    	 <li>bundle是否存在</li>
	 *    	 <li>bundle是否是可销售状态</li>
	 *    	 <li>bundle是否库存足够 （预售库存　和　是否需要同步库存）</li>
	 *    	 <li>bundle所包含的所有商品是否是可销售的</li>
	 *    	 <li>bundle所包含的商品是否库存足够</li>
	 *    </ol>
	 * </ul> 
	 * @param bundleId : bundle的PK
	 * @param skuIds : bundle中选中的一组sku
	 * @param quantity ：购买bundle的数量
	 * @return ：返回值 . 参考{@link com.baozun.nebula.command.bundle.BundleCommand.BundleStatus}}
	 * 
	 */
	public BundleValidateResult validateBundle(Long bundleId,List<Long> skuIds,int quantity);
	
}
