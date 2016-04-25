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

import loxia.annotation.NativeQuery;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;

import com.baozun.nebula.model.product.PropertyValueGroup;

/**
 * 属性值分组dao
 * 
 * @author viktor huang
 * @date Apr 7, 2016 3:44:43 PM
 */
public interface PropertyValueGroupDao extends GenericEntityDao<PropertyValueGroup, Long>{

	/**
	 * 根据属性id查询它下面的所有属性值组，order by createTime
	 * 
	 * @param propertyId
	 *            属性id
	 * @return
	 */
	@NativeQuery(model = PropertyValueGroup.class)
	List<PropertyValueGroup> findPropertyValueGroupByPropertyId(@QueryParam("propertyId") Long propertyId);
	

	/**
	 * 通过属性ID获取PropertyValueGroup列表
	 * 
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = PropertyValueGroup.class)
	List<PropertyValueGroup> findByPropertyId(@QueryParam("propertyId") Long propertyId);
}
