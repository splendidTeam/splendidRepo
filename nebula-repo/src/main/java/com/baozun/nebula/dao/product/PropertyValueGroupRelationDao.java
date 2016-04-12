/**
 * 
 */
package com.baozun.nebula.dao.product;

import loxia.annotation.NativeUpdate;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;

import com.baozun.nebula.model.product.PropertyValueGroupRelation;

/**
 * 属性值分组 和属性值关系Dao
 * 
 * @author viktor huang
 * @date Apr 8, 2016 2:32:16 PM
 */
public interface PropertyValueGroupRelationDao extends GenericEntityDao<PropertyValueGroupRelation, Long>{

	/**
	 * 给【属性值】和【属性值组】解绑 （物理删除）
	 * 
	 * @param proValueGroupId
	 *            属性值组id
	 * @return
	 */
	@NativeUpdate
	Integer deleteRelationByProValueGroupId(@QueryParam("proValueGroupId") Long proValueGroupId);

}
