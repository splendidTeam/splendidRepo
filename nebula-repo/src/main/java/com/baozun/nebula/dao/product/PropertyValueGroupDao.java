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
 *
 */
package com.baozun.nebula.dao.product;

import java.util.List;

import com.baozun.nebula.model.product.PropertyValueGroup;

import loxia.annotation.NativeQuery;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;

/**
 * 商品属性Dao
 * 
 * @author 黄大辉
 */
public interface PropertyValueGroupDao extends GenericEntityDao<PropertyValueGroup, Long>{

	/**
	 * 通过属性ID获取PropertyValueGroup列表
	 * 
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = PropertyValueGroup.class)
	List<PropertyValueGroup> findByPropertyId(@QueryParam("propertyId") Long propertyId);

}
