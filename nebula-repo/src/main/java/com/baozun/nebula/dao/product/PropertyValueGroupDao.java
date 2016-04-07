/**
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
}
