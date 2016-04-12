/**
 * 
 */
package com.baozun.nebula.sdk.manager.product;

import java.util.List;

import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.command.product.PropertyValueCommand;
import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.product.PropertyValue;
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
	 * 根据属性值组id查询他自己
	 * 
	 * @param groupId
	 * @return
	 */
	PropertyValueGroup findProValueGroupById(Long groupId);

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

	/**
	 * 查询已经加入到属性值组的PropertyValue
	 * 
	 * @param proValGroupId
	 *            属性值组的Id
	 * @return
	 */
	List<PropertyValue> findBoundGroupPropertyValue(Long proValGroupId);

	/**
	 * 查询还没有加入到属性值组的PropertyValue
	 * 
	 * @param propertyId
	 *            对应那个属性
	 * @param proValGroupId
	 *            属性值组的Id
	 * @return
	 */
	List<PropertyValue> findFreeGroupPropertyValue(Long propertyId,Long proValGroupId);

	/**
	 * 分页查询属性值（i18n）【propertyId 和 proValueGroupId 必须有一个不为空】
	 * 
	 * @param page
	 * @param sorts
	 * @param propertyId
	 *            属性id
	 * @param proValueGroupId
	 *            属性值组id
	 * @return
	 */
	Pagination<PropertyValueCommand> findPropertyValueByPage(Page page,Sort[] sorts,Long propertyId,Long proValueGroupId);

	/**
	 * 添加或修改属性值
	 * 
	 * @param propertyId
	 *            属性id
	 * @param propertyValues
	 *            属性值对象
	 * @return
	 */
	void addOrUpdatePropertyValue(Long propertyId,PropertyValueCommand propertyValues);
}
