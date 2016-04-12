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
 */
package com.baozun.nebula.manager.baseinfo;

import java.util.List;
import java.util.Map;

import loxia.dao.Sort;

import com.baozun.nebula.command.ShopCommand;
import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.auth.Organization;
import com.baozun.nebula.model.baseinfo.Shop;
import com.baozun.nebula.model.product.Industry;
import com.baozun.nebula.model.product.Property;
import com.baozun.nebula.model.product.PropertyValue;
import com.baozun.nebula.model.product.ShopProperty;
import com.baozun.nebula.web.UserDetails;

/**
 * 店铺管理业务逻辑
 * 
 * @author yi.huang
 * @date 2013-7-2 上午10:15:38
 */
public interface ShopManager extends BaseManager{

	Shop createOrUpdateShop(Shop shop ,Organization organization,ShopProperty[] shopProperties);

	/**
	 * 根据id查询属性
	 * 
	 * @param propertyId
	 *            属性id
	 * @return
	 */
	Property findPropertyById(Long propertyId);

	/**
	 * 通过ids批量删除Property 设置lifecycle =2,并删除对应关系表中的数据
	 * 
	 * @param propertyIds
	 *            属性ids
	 * @param shopId
	 *            店铺id
	 * @param industryId
	 *            行业id
	 * @return
	 */
	boolean removePropertyByIds(List<Long> propertyIds,Long shopId,Long industryId);

	/**
	 * 通过ids批量启用或禁用Property 设置lifecycle =0 或 1
	 * 
	 * @param ids
	 * @return
	 */
	boolean enableOrDisablePropertyByIds(List<Long> ids,Integer state);

	/**
	 * 根据行业id和店铺id查询 属性
	 * 
	 * @param industryId
	 *            行业id
	 * @param shopId
	 *            店铺id
	 * @return
	 */
	List<Property> findPropertyListByIndustryIdAndShopId(Long industryId,Long shopId,Sort[] sorts);

	/**
	 * 店铺列表
	 * 
	 * @param orgaTypeId
	 * @return
	 */
	List<ShopCommand> findShopListByQueryMap(Map<String, Object> paraMap, Sort[] sorts);

	/**
	 * 启用或禁用店铺 ：‘1’表示启用；‘0’表示禁用。
	 * 
	 * @param shopIds
	 *            店铺ID数组
	 * @param type
	 *            启用或禁用
	 */
	Integer enableOrDisableShopByIds(Long[] shopIds,Integer type);

	/**
	 * 逻辑删除店铺 ：‘2’表示删除
	 * 
	 * @param shopIds
	 *            店铺ID数组
	 */
	Integer removeShopByIds(Long[] shopIds);

	/**
	 * 新增店铺时默认显示的相关行业
	 * 
	 * @param sorts
	 *            caihong.wu
	 * @return
	 */
	List<Industry> findAllIndustryList(Sort[] sorts);


	/**
	 * 根据店铺id查询店铺
	 * 
	 * @param id
	 *            店铺ID
	 * @return
	 */
	ShopCommand findShopById(Long id);
	
	/**
	 * 根据orgid查询店铺
	 * 
	 * @param id
	 *            组织ID
	 * @return
	 */
	ShopCommand findShopByOrgId(Long id);


	/**
	 * 根据propertyId查出店铺属性
	 * 
	 * @param propertyId
	 * @return
	 */
	ShopProperty findShopPropertyByPropertyId(Long propertyId);

	/**
	 * 根据PropertyId查找属性值列表
	 * 
	 * @param propertyId
	 * @return
	 */
	List<PropertyValue> findPropertyValueList(Long propertyId);

	/**
	 * @param propertyValues
	 * @return
	 */
	void createOrUpdatePropertyValueByList(PropertyValue[] propertyValues,Long propertyId);
	

	/**
	 * 在新增店铺属性的时候给出默认的排序
	 * 
	 * @param shopId
	 *            店铺id
	 * @param industryId
	 *            行业id
	 * @return
	 */
	Integer findCreatePropertyDefaultSortNo(Long shopId,Long industryId);


	/**
	 * 验证属性名(同行业不能重复)
	 * 
	 * @param propertyName
	 *            属性名
	 * @param industryId
	 *            行业id
	 * @return
	 */
	boolean validatePropertyName(String propertyName,Integer industryId);
	
	/**
	 * 验证属性名(同行业不能重复)
	 * 
	 * @param propertyName
	 *            属性名
	 * @param industryId
	 *            行业id
	 * @return
	 */
	boolean validatePropertyName(String name,Integer propertyId, String lang);
	
	/**
	 * 根据属性名和语言、属性id验证，如果语言为空只验证属性名并排除当前的属性id
	 * @return boolean
	 * @param name
	 * @param lang
	 * @author 冯明雷
	 * @time 2016年4月8日下午4:03:11
	 */
	boolean validatePropertyName(Long propertyId,String name,String lang);

	/**
	 * 查询是否有相同的组织名称
	 * 
	 * @param name
	 * @return
	 */
	Integer validateShopCode(String code);

	/**
	 * 查询某一店铺下的所有行业
	 * 
	 * @param shopId
	 *            店铺ID
	 * @return
	 */
	List<Industry> findIndustryListByShopId(Long shopId);

	/**
	 * 保存或更新属性
	 * 
	 * @param property
	 *            实体
	 * @param shopId
	 *            店铺id
	 * @param type
	 *            类型 1：保存；2：新建
	 * @return
	 */
	boolean createOrUpdateProperty(Property property,Long shopId,Integer type);
	/**
	 * 根据shopId查找相关的行业
	 * @param shopId
	 * @return
	 */
	List<ShopProperty> findShopPropertyByshopId(Long shopId);
	
	/**
	 * 获取店铺ID
	 * @param userDetails
	 * @return
	 */
	Long getShopId(UserDetails userDetails);

	/**
	 * 根据店铺ID查询商品所在的行业(可用行业)
	 * @param shopId
	 * @return
	 */
	List<Industry> findAllEnabledIndustryByShopId(Long shopId);
	
}
