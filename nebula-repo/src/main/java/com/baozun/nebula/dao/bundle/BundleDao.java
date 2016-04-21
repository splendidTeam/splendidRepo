/**
 
* Copyright (c) 2014 Baozun All Rights Reserved.
 
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
 
*
 
*/
package com.baozun.nebula.dao.bundle;

import java.util.List;

import loxia.annotation.NativeQuery;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;
import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.command.bundle.BundleCommand;
import com.baozun.nebula.model.bundle.Bundle;

/**
 * Bundle Dao
 * 
 * @author yue.ch
 *
 */
public interface BundleDao extends GenericEntityDao<Bundle, Long> {
	
	/**
	 * 根据主卖品的商品ID查询捆绑类商品信息
	 * @param itemId 主卖品的商品ID
	 * @param lifecycle 捆绑类商品状态，如果为空，则忽略该条件字段
	 * @return
	 */
	@NativeQuery(model = BundleCommand.class)
	List<BundleCommand> findBundlesByItemId(@QueryParam("itemId")Long itemId, @QueryParam("lifecycle")Integer lifecycle);
	
	/**
	 * 根据主卖品的商品款号查询捆绑类商品信息
	 * @param style 主卖品的商品款号
	 * @param lifecycle 捆绑类商品状态，如果为空，则忽略该条件字段
	 * @return
	 */
	@NativeQuery(model = BundleCommand.class)
	List<BundleCommand> findBundlesByStyle(@QueryParam("style")String style, @QueryParam("lifecycle")Integer lifecycle);
	
	@NativeQuery(model = BundleCommand.class)
	BundleCommand findBundlesById(@QueryParam("id")Long id, @QueryParam("lifecycle")Integer lifecycle);
	
	@NativeQuery(model = BundleCommand.class)
	Pagination<BundleCommand> findBundlesByPage(Page page ,Sort[] sort);
	
	@NativeQuery(model = BundleCommand.class)
	BundleCommand findBundleById(Long id);

}
