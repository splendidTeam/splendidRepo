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

import com.baozun.nebula.model.product.PropertyValue;
import com.baozun.nebula.model.product.PropertyValueLang;

/**
 * @author wenxiu.ke
 */
public interface PropertyValueDao extends GenericEntityDao<PropertyValue, Long>{

	@NativeQuery(model = PropertyValue.class)
	List<PropertyValue> findPropertyValueListById(@QueryParam("propertyId") Long propertyId);

	/**
	 * @param id
	 * @param value
	 * @param showValue
	 * @param thumb
	 * @return :Integer
	 * @date 2014-2-12 下午03:26:59
	 */
	@NativeUpdate
	Integer updateById(
			@QueryParam("id") Long id,
			@QueryParam("value") String value,
			@QueryParam("thumb") String thumb,
			@QueryParam("sortNo") Integer sortNo);

	/**
	 * @param ids
	 * @return
	 */
	@NativeUpdate
	Integer deleteById(@QueryParam("ids") List<Long> ids);

	/**
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = PropertyValue.class)
	List<PropertyValue> findPropertyValueListByIds(@QueryParam("ids") List<Long> ids);

	@NativeQuery(model = PropertyValue.class)
	List<PropertyValue> findPropertyValueListByIdsI18n(@QueryParam("ids") List<Long> ids,@QueryParam("lang") String lang);

	/**
	 * 通过id查询属性值
	 * 
	 * @param id
	 * @return
	 */
	@NativeQuery(model = PropertyValue.class)
	PropertyValue findPropertyValueById(@QueryParam("id") Long id);

	/**
	 * 通过id和语言查询属性值
	 * 
	 * @param id
	 * @param lang
	 * @return
	 */
	@NativeQuery(model = PropertyValue.class)
	PropertyValue findPropertyValueByIdI18n(@QueryParam("id") Long id,@QueryParam("lang") String lang);

	@NativeQuery(model = PropertyValue.class)
	List<PropertyValue> findPropertyValueListByPropIds(@QueryParam("propertyIds") List<Long> propertyIds);

	/**
	 * 根据属性名称查询对应的属性值列表
	 * 
	 * @param propertyName
	 * @return
	 */
	@NativeQuery(model = PropertyValue.class)
	List<PropertyValue> findPropertyValueListByPropertyName(@QueryParam("propertyName") String propertyName);

	/**
	 * 根据propertyId和属性值查询
	 * 
	 * @param isSuit
	 * @return
	 */
	@NativeQuery(model = PropertyValue.class)
	PropertyValue findPropertyValueByPropertyIdAndValue(@QueryParam("propertyId") Long propertyId,@QueryParam("value") String value);

	/**
	 * 查询所有销售属性的属性值,一般用于 wormhole同步商品时候,来判断同步过来的商品 属性值是否存在于DB中
	 * 
	 * @return
	 */
	@NativeQuery(model = PropertyValue.class)
	List<PropertyValue> findSalesPropertyValues();

	/**
	 * @author 何波
	 * @Description:修改属性国际化信息
	 * @param params
	 * @return int
	 * @throws
	 */
	@NativeUpdate
	int updatePropertyValueLang(@QueryParam Map<String, Object> params);

	@NativeQuery(model = PropertyValueLang.class)
	List<PropertyValueLang> findPropertyValueLangByPvids(@QueryParam("pvIds") List<Long> pvIds,@QueryParam("langs") List<String> langs);

	@NativeQuery(model = PropertyValueLang.class)
	PropertyValueLang findPropertyValueLang(@QueryParam("id") Long id,@QueryParam("lang") String lang);

	/**
	 * @param propertyIds
	 * @return
	 */
	@NativeQuery(model = PropertyValue.class)
	List<PropertyValue> findPropertyValueListByCommonPropertyId(
			@QueryParam("propertyId") Long propertyId,
			@QueryParam("commonPropertyId") Long commonPropertyId);

	/**
	 * 根据commonPropertyId和属性值查询
	 * 
	 * @return
	 */
	@NativeQuery(model = PropertyValue.class)
	List<PropertyValue> findPropertyValueByCommonPropertyIdAndValue(
			@QueryParam("commonPropertyId") Long commonPropertyId,
			@QueryParam("value") String value);

	@NativeQuery(model = PropertyValue.class)
	List<PropertyValue> findPropertyValueByCommonPropertyIds(@QueryParam("commonProIds") List<Long> commonPropertyIds);

	/**
	 * 根据itemcod查询propervalue id
	 */
	@NativeQuery(model = PropertyValue.class)
	PropertyValue findpropervalueId(@QueryParam("mapvalue") Map<String, String> mapvalue,@QueryParam("valuecolor") String valuecolor);

	/**
	 * 通过属性值分组ID找到相对的属性值列表
	 * 
	 * @param proGroupId
	 * @return
	 */
	@NativeQuery(model = PropertyValue.class)
	List<PropertyValue> findByProGroupId(@QueryParam("proGroupId") Long proGroupId);

	/**
	 * @param propertyIds
	 * @return
	 */
	@NativeQuery(model = PropertyValue.class)
	List<PropertyValue> findPropertyValueListByCommonPropertyIdAndPropertyId(
			@QueryParam("propertyId") Long propertyId,
			@QueryParam("commonPropertyId") Long commonPropertyId);

	/**
	 * 查询已经加入到属性值组的PropertyValue
	 * 
	 * @param proValGroupId
	 *            属性值组的Id
	 * @return
	 */
	@NativeQuery(model = PropertyValue.class)
	List<PropertyValue> findBoundGroupPropertyValue(@QueryParam("proValGroupId") Long proValGroupId);

	/**
	 * 查询还没有加入到属性值组的PropertyValue
	 * 
	 * @param propertyId
	 *            对应那个属性
	 * @param proValGroupId
	 *            属性值组的Id
	 * @return
	 */
	@NativeQuery(model = PropertyValue.class)
	List<PropertyValue> findFreeGroupPropertyValue(@QueryParam("propertyId") Long propertyId,@QueryParam("proValGroupId") Long proValGroupId);

	/**
	 * 根据属性id查询属性值排序的最大值
	 * 
	 * @param propertyId
	 *            属性id
	 * @return
	 */
	@NativeQuery(alias = "CNT",clazzes = Integer.class)
	Integer findMaxSortNoByPropertyId(@QueryParam("propertyId") Long propertyId);

	/**
	 * 分页查询属性值
	 * 
	 * @param page
	 * @param sorts
	 * @param propertyId
	 *            属性id
	 * @param proValueGroupId
	 *            属性值组id
	 * @return
	 */
	@NativeQuery(model = PropertyValue.class)
	Pagination<PropertyValue> findPropertyValueWithPage(
			Page page,
			Sort[] sorts,
			@QueryParam("propertyId") Long propertyId,
			@QueryParam("proValGroupId") Long proValGroupId);

	/**
	 * 根据属性值id更新排序
	 * 
	 * @param id
	 *            属性值id
	 * @param sortNo
	 * @return
	 */
	@NativeUpdate
	Integer updatePropertyValueSortById(@QueryParam("id") Long id,@QueryParam("sortNo") Integer sortNo);
}