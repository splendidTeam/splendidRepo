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
package com.baozun.nebula.manager.baseinfo;

import java.util.List;

import com.baozun.nebula.command.ShopCommand;
import com.baozun.nebula.manager.BaseManager;

/**
 * 店铺管理业务逻辑
 * 
 * @author yi.huang
 * @date 2013-7-2 上午10:15:38
 */
public interface ShopManager extends BaseManager{


	
	/**
	 * 根据orgid查询店铺
	 * 
	 * @param id
	 *            组织ID
	 * @return
	 */
	ShopCommand findShopByOrgId(Long id);

	/**
	 * 根据shopIds查询店铺信息
	 * 
	 * @param shopIds
	 * @return List<ShopCommand>
	 */
	List<ShopCommand> findByShopIds(List<Long> shopIds);
}
