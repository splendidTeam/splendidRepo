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

import com.baozun.nebula.model.bundle.Bundle;
import com.baozun.nebula.model.bundle.BundleSku;

import loxia.annotation.NativeQuery;
import loxia.dao.GenericEntityDao;

/**
 * BundleSku Dao
 * 
 * @author yue.ch
 *
 */
public interface BundleSkuDao extends GenericEntityDao<BundleSku, Long> {
	
	/**
	 * 根据主卖品的商品ID查询捆绑类商品信息
	 * @param itemId 商品ID
	 * @param lifecycle 捆绑类商品状态，如果为空，则忽略该条件字段
	 * @return
	 */
	@NativeQuery(model = Bundle.class)
	List<Bundle> findBundlesByItemId(Long itemId, Integer lifecycle);
	
	/**
	 * 根据主卖品的商品款号查询捆绑类商品信息
	 * @param style 商品款号
	 * @param lifecycle 捆绑类商品状态，如果为空，则忽略该条件字段
	 * @return
	 */
	@NativeQuery(model = Bundle.class)
	List<Bundle> findBundlesByStyle(String style, Integer lifecycle);

}
