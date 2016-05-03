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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.command.ShopCommand;
import com.baozun.nebula.dao.product.ShopDao;

/**
 * @author yi.huang
 * @date 2013-7-2 上午10:17:43
 */
@Transactional
@Service("shopManager")
public class ShopManagerImpl implements ShopManager{

	@SuppressWarnings("unused")
	private static final Logger	log	= LoggerFactory.getLogger(ShopManagerImpl.class);

	@Autowired
	private ShopDao				shopDao;



	/* (non-Javadoc)
	 * @see com.baozun.nebula.manager.baseinfo.ShopManager#findShopByOrgId(Long id)
	 */
	@Override
	public ShopCommand findShopByOrgId(Long id) {
		ShopCommand shopCommand =shopDao.findShopByOrgId(id);
		return shopCommand;
	}
	
	/**
	 * 根据shopIds查询店铺信息
	 * 
	 * @param shopIds
	 * @return List<ShopCommand>
	 */
	public List<ShopCommand> findByShopIds(List<Long> shopIds){
		
		return shopDao.findByShopIds(shopIds);
	}
	 
	
}
