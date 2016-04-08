/**
 * 
 */
package com.baozun.nebula.sdk.manager.product;

import java.util.List;

import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.product.PropertyValueGroup;

/**
 * SdkPropertyManager；处理属性，属性值，属性值分组业务逻辑
 * 
 * @author viktor huang
 * @date Apr 7, 2016 5:12:51 PM
 */
public interface SdkPropertyManager extends BaseManager{

	/**
	 * 根据属性id查询它下面的所有属性值组，order by createTime
	 * 
	 * @param propertyId
	 * @return
	 */
	List<PropertyValueGroup> findProValueGroupByPropertyId(Long propertyId);
}
