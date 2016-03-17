/**
 * Copyright (c) 2013 Baozun All Rights Reserved.
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
package com.baozun.nebula.dao.freight;

import loxia.annotation.NativeQuery;
import loxia.annotation.NativeUpdate;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;

import com.baozun.nebula.model.freight.ProductShippingTemeplate;
import com.baozun.nebula.model.freight.ShippingFeeConfig;

/**
 * @author Tianlong.Zhang
 *
 */
public interface ProductShippingTemeplateDao  extends GenericEntityDao<ProductShippingTemeplate, Long> {
	@NativeQuery(clazzes=Integer.class,alias="total")
	Integer findUsedCountByTemeplateId(@QueryParam("temeplateId")Long temeplateId);
	
	@NativeUpdate
	Integer removeAllShippingFeeConfigsByTemeplateId(@QueryParam("temeplateId")Long temeplateId);

	@NativeQuery(model=ProductShippingTemeplate.class)
	ProductShippingTemeplate findProductShippingTemeplate(@QueryParam("itemId")Long itemId,@QueryParam("temeplateId")Long temeplateId);

	@NativeUpdate
	Integer updateProductShippingTemeplate(@QueryParam("itemId")Long itemId,@QueryParam("temeplateId")Long temeplateId);

	@NativeQuery(model = ShippingFeeConfig.class)
	ShippingFeeConfig findProductShippingTemeplateByItemIdCityId(@QueryParam("itemId")Long itemId,
			@QueryParam("destId")String destId,
			@QueryParam("shippingProviderId")Integer shippingProviderId);
	
	@NativeQuery(model = ShippingFeeConfig.class)
	ShippingFeeConfig findProductShippingTemeplateByItemIdProvienceId(@QueryParam("itemId")Long itemId,
			@QueryParam("destId")String destId,
			@QueryParam("shippingProviderId")Integer shippingProviderId);
	
	@NativeUpdate
	Integer removeItemShippingTemeplate(@QueryParam("itemId")Long itemId);
	
}
