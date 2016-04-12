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
import java.util.Map;

import loxia.annotation.NativeQuery;
import loxia.annotation.NativeUpdate;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;
import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.command.PropertyCommand;
import com.baozun.nebula.model.product.Property;
import com.baozun.nebula.model.product.PropertyLang;
import com.baozun.nebula.model.product.PropertyValue;

/**
 * 商品属性Dao
 * 
 * @author lin.liu
 */
public interface PropertyDao extends GenericEntityDao<Property, Long>{

	/**
	 * 获取所有Property列表
	 * 
	 * @return
	 */
	@NativeQuery(model = Property.class)
	List<Property> findAllPropertyList();

	/**
	 * 通过ids获取Property列表
	 * 
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = Property.class)
	List<Property> findPropertyListByIds(@QueryParam("ids") List<Long> ids,Sort[] sorts);

	@NativeQuery(model = Property.class)
	List<Property> findPropertyListByIdsI18n(@QueryParam("ids") List<Long> ids,Sort[] sorts,@QueryParam("lang") String lang);

	/**
	 * 通过参数map获取Property列表
	 * 
	 * @param paraMap
	 * @return
	 */
	@NativeQuery(model = Property.class)
	List<Property> findPropertyListByQueryMap(@QueryParam Map<String, Object> paraMap);

	/**
	 * 分页获取Property列表
	 * 
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts
	 * @return
	 */
	@NativeQuery(model = PropertyCommand.class)
	Pagination<PropertyCommand> findPropertyListByQueryMapWithPage(Page page,Sort[] sorts,@QueryParam Map<String, Object> paraMap);

	/**
	 * 通过ids批量启用或禁用Property 设置lifecycle =0 或 1
	 * 
	 * @param ids
	 * @return
	 */
	@NativeUpdate
	Integer enableOrDisablePropertyByIds(@QueryParam("ids") List<Long> ids,@QueryParam("state") Integer state);

	/**
	 * 通过ids批量删除Property 设置lifecycle =2
	 * 
	 * @param ids
	 * @return
	 */
	@NativeUpdate
	Integer removePropertyByIds(@QueryParam("ids") List<Long> ids);

	/**
	 * 获取有效的Property列表 lifecycle =1
	 * 
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = Property.class)
	List<Property> findAllEffectPropertyList();

	/**
	 * 通过参数map获取有效的Property列表 强制加上lifecycle =1
	 * 
	 * @param paraMap
	 * @return
	 */
	@NativeQuery(model = Property.class)
	List<Property> findEffectPropertyListByQueryMap(@QueryParam Map<String, Object> paraMap);

	/**
	 * 分页获取有效的Property列表 强制加上lifecycle =1
	 * 
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts
	 * @return
	 */
	@NativeQuery(model = Property.class)
	Pagination<Property> findEffectPropertyListByQueryMapWithPage(Page page,Sort[] sorts,@QueryParam Map<String, Object> paraMap);

	/**
	 * 根据id查询属性
	 * 
	 * @param id
	 * @return
	 */
	@NativeQuery(model = Property.class)
	Property findPropertyById(@QueryParam("propertyId") Long id);

	@NativeQuery(model = Property.class)
	Property findPropertyByIdI18n(@QueryParam("propertyId") Long id,@QueryParam("lang") String lang);

	
	/**
	 * 根据id查询属性
	 *  不关联t_common_property
	 * @param id
	 * @return
	 */
	
	@NativeQuery(model = Property.class)
	Property findByIdWithoutCommonProperty(@QueryParam("propertyId") Long id);
	
	
	/**
	 * 根据name查询属性
	 * 
	 * @param id
	 * @return
	 */
	@NativeQuery(model = Property.class)
	Property findPropertyByName(@QueryParam("name") String name);

	/**
	 * 根据id更新属性
	 * 
	 * @param id
	 *            属性id
	 * @param paraMap
	 *            要跟新的值
	 * @return
	 */
	@NativeUpdate
	Integer updatePropertyById(@QueryParam("id") Long id,@QueryParam Map<String, Object> paraMap);

	@NativeQuery(model = PropertyValue.class)
	PropertyValue findPropertyListById(@QueryParam("propertyId") Long propertyId);

	/**
	 * 根据行业id查询 属性
	 * 
	 * @param industryId
	 *            行业id
	 */
	@NativeQuery(model = Property.class)
	List<Property> findPropertyListByIndustryId(@QueryParam("industryId") Long industryId);

	/**
	 * 根据分类ID和行业Id查询 属性
	 * 
	 * @param categoryId
	 * @param industryId
	 * @return
	 */
	@NativeQuery(model = Property.class)
	List<Property> findPropertyByCategoryIdAndIndustryId(@QueryParam("categoryId") Long categoryId,@QueryParam("industryId") Long industryId);

	/**
	 * 根据行业id查询 属性
	 * 
	 * @param id
	 *            属性id
	 * @param sortNo
	 *            排序号
	 */
	@NativeUpdate
	Integer updateSortById(@QueryParam("propertyId") Long propertyId,@QueryParam("sortNo") Integer sortNo);

	/**
	 * @author 何波
	 * @Description:修改属性国际化信息
	 * @param params
	 * @return int
	 * @throws
	 */
	@NativeUpdate
	int updatePropertyLang(@QueryParam Map<String, Object> params);

	@NativeQuery(model = PropertyLang.class)
	List<PropertyLang> findPropertyLangByPids(@QueryParam("pids") List<Long> pids,@QueryParam("langs") List<String> langs);

	@NativeQuery(model = PropertyLang.class)
	PropertyLang findPropertyLang(@QueryParam("id") Long id,@QueryParam("lang") String lang);

	/**
	 * 验证属性名称是否重复
	 * 
	 * @param industryId
	 * @param commonPropertyId
	 * @param propertyId
	 * @param proname
	 */
	@NativeQuery(alias = "num",clazzes = Long.class)
	Long validatePropertyname(
			@QueryParam("industryId") Long industryId,
			@QueryParam("propertyId") Long propertyId,
			@QueryParam("proname") String proname);

	/**
	 * 验证其关联的公共属性是否重复
	 * 
	 * @param industryId
	 * @param commonPropertyId
	 * @param propertyId
	 * @param proname
	 */
	@NativeQuery(alias = "num",clazzes = Long.class)
	Long validatecommonProperty(
			@QueryParam("industryId") Long industryId,
			@QueryParam("propertyId") Long propertyId,
			@QueryParam("commonPropertyId") Long commonPropertyId);
	
	/**
	 * 根据propertyId查询Property表
	 * @return Property
	 * @param propertyId
	 * @author 冯明雷
	 * @time 2016年4月8日下午5:39:20
	 */
	@NativeQuery(model = Property.class)
	Property findPropertyByPropertyId(@QueryParam("propertyId") Long propertyId);
	
	
	/**
	 * 根据propertyId查询Property国际化数据
	 * @return List<PropertyValueLang>
	 * @param propertyId
	 * @author 冯明雷
	 * @time 2016年4月8日下午5:46:01
	 */
	@NativeQuery(model = PropertyLang.class)
	List<PropertyLang> findPropertyLongByPropertyId(@QueryParam("propertyId") Long propertyId);
}
