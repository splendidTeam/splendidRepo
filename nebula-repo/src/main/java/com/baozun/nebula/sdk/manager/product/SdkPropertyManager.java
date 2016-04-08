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

	/**
	 * 新建或者修改“属性值组”
	 * 
	 * @param propertyId
	 *            此【属性值组】隶属的 【属性】 id
	 * @param groupId
	 *            属性值组Id
	 * @param groupName
	 *            属性值组名称
	 * @return
	 */
	PropertyValueGroup savePropertyValueGroup(Long propertyId,Long groupId,String groupName);

	/**
	 * 给【属性值】和【属性值组】 建立绑定
	 * 
	 * @param proValueIds
	 *            属性值 ids
	 * @param propertyValueGroupId
	 *            属性值组id
	 * @return
	 */
	boolean bindPropertyValueAndProValueGroup(List<Long> proValueIds,Long propertyValueGroupId);
}
