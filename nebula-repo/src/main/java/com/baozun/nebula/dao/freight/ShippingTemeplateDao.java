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

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import loxia.annotation.NativeQuery;
import loxia.annotation.NativeUpdate;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;
import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.model.freight.ShippingTemeplate;

/**
 * @author Tianlong.Zhang
 * 
 */
public interface ShippingTemeplateDao extends
		GenericEntityDao<ShippingTemeplate, Long> {

	/**
	 * 修改运费基本信息（其中店铺Id 和 模板类型（店铺，系统） 不能修改）
	 * 
	 * @param id 模板id
	 * @param name
	 *            运费模板名称
	 * @param calculationType
	 *            计算方式
	 * @param isDefault
	 *            是否默认
	 * @return
	 */
	@NativeUpdate
	public Integer updateShippingTemeplate( @QueryParam("id") Long id,
											@QueryParam("name") String name,
											@QueryParam("calculationType") String calculationType,
											@QueryParam("isDefault") boolean isDefault,
											@QueryParam("defaultFee") BigDecimal defaultFee);
	
	@NativeQuery(model=ShippingTemeplate.class)
	public List<ShippingTemeplate> findShippingTemeplate(@QueryParam("type") String type,@QueryParam("shopId") Long shopId);

	@NativeQuery(model=ShippingTemeplate.class)
	public Pagination<ShippingTemeplate> findShippingTemeplateList(Page page,Sort[] sorts,@QueryParam Map<String, Object> paraMap,
			@QueryParam("shopId") Long shopId);

	@NativeQuery(model=ShippingTemeplate.class)
	public List<ShippingTemeplate> findAllShippingTemeplate();
	
	@NativeUpdate
	Integer updateShippingTemeplateByShopId(@QueryParam("shopId")Long shopId,@QueryParam("isDefault")boolean isDefault);
	
	@NativeUpdate
	Integer updateShippingTemeplateById(@QueryParam("id")Long id,@QueryParam("isDefault")boolean isDefault);

}
