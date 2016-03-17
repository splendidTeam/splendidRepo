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
package com.baozun.nebula.dao.product;

import java.util.List;

import loxia.annotation.NativeQuery;
import loxia.annotation.NativeUpdate;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;

import com.baozun.nebula.model.product.ShopProperty;

/**
 *店铺、行业、属性的三叉关系表dao
 * 
 * @author yi.huang
 * @date 2013-7-4 下午12:22:33
 */
public interface ShopPropertyDao extends GenericEntityDao<ShopProperty, Long>{

	/**
	 * 添加一个店铺、行业、属性的关系
	 * 
	 * @param industryId
	 *            行业id
	 * @param propertyId
	 *            属性id
	 * @param shopId
	 *            店铺id
	 * @return
	 */
	@NativeUpdate
	Integer addShopProperty(
			@QueryParam("industryId") Long industryId,
			@QueryParam("propertyId") Long propertyId,
			@QueryParam("shopId") Long shopId);

	/**
	 * @param propertyId
	 * @return
	 */
	@NativeQuery(model = ShopProperty.class)
	ShopProperty findShopPertyByPropertyId(@QueryParam("propertyId") Long propertyId);

	/**
	 * 在此三叉表中删除一个对应关系
	 * 
	 * @param propertyIds
	 *            属性ids
	 * @param shopId
	 *            店铺id
	 * @param industryId
	 *            行业id
	 * @return
	 */
	@NativeUpdate
	Integer removeShopProperty(
			@QueryParam("propertyIds") List<Long> propertyIds,
			@QueryParam("shopId") Long shopId,
			@QueryParam("industryId") Long industryId);
	
	@NativeQuery(model = ShopProperty.class)
	List<ShopProperty> findShopPropertyByshopId(@QueryParam("shopId")Long shopId);
}
