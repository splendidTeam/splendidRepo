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
import loxia.dao.Pagination;
import loxia.dao.Sort;
import loxia.dao.Page;
import com.baozun.nebula.model.product.Industry;

/**
 * IndustryDao
 * 
 * @author chen.kf
 */
public interface IndustryDao extends GenericEntityDao<Industry, Long>{

	/**
	 * 在父级分类.父节点下面添加新的分类
	 * 
	 * @param parentId
	 *            父级分类.父节点Id
	 * @param lifecycle
	 *            生命周期
	 * @param name
	 *            分类名称
	 */
	@NativeUpdate
	Integer insertIndustry(@QueryParam("parentId") Long parentId,@QueryParam("lifecycle") Integer lifecycle,@QueryParam("name") String name);

	/**
	 * 获取所有Industry列表
	 * 
	 * @return
	 */
	@NativeQuery(model = Industry.class)
	List<Industry> findAllIndustryList();

	/**
	 * 通过ids获取Industry列表
	 * 
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = Industry.class)
	List<Industry> findIndustryListByIds(@QueryParam("ids") List<Long> ids);

	/**
	 * 通过id获取Industry
	 * 
	 * @param id
	 * @return
	 */
	@NativeQuery(model = Industry.class)
	Industry findIndustryById(@QueryParam("id") Long id);

	/**
	 * 通过参数map获取Industry列表
	 * 
	 * @param paraMap
	 * @return
	 */
	@NativeQuery(model = Industry.class)
	List<Industry> findIndustryListByQueryMap(@QueryParam Map<String, Object> paraMap);

	/**
	 * 分页获取Industry列表
	 * 
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts
	 * @return
	 */
	@NativeQuery(model = Industry.class)
	Pagination<Industry> findIndustryListByQueryMapWithPage(Page page,Sort[] sorts,@QueryParam Map<String, Object> paraMap);

	/**
	 * 通过ids批量启用或禁用Industry 设置lifecycle =0 或 1
	 * 
	 * @param ids
	 * @return
	 */
	@NativeUpdate
	void enableOrDisableIndustryByIds(@QueryParam("ids") List<Long> ids,@QueryParam("state") Integer state);

	/**
	 * 通过ids批量删除Industry 设置lifecycle =2
	 * 
	 * @param ids
	 * @return
	 */
	@NativeUpdate
	Integer removeIndustryByIds(@QueryParam("ids") List<Long> ids);

	/**
	 * 通过id删除Industry 设置lifecycle =2
	 * 
	 * @param ids
	 * @return
	 */
	@NativeUpdate
	Integer removeIndustryById(@QueryParam("id") Long id);

	/**
	 * 通过id删除Industry 设置lifecycle =2
	 * 
	 * @param ids
	 * @return
	 */
	@NativeUpdate
	Integer updateIndustryById(@QueryParam("id") Long id,@QueryParam("lifecycle") Integer lifecycle,@QueryParam("name") String name);

	/**
	 * 获取有效的Industry列表 lifecycle =1
	 * 
	 * @param ids
	 * @return
	 */
	@NativeQuery(model = Industry.class)
	List<Industry> findAllEffectIndustryList();

	/**
	 * 通过参数map获取有效的Industry列表 强制加上lifecycle =1
	 * 
	 * @param paraMap
	 * @return
	 */
	@NativeQuery(model = Industry.class)
	List<Industry> findEffectIndustryListByQueryMap(@QueryParam Map<String, Object> paraMap);

	/**
	 * 分页获取有效的Industry列表 强制加上lifecycle =1
	 * 
	 * @param start
	 * @param size
	 * @param paraMap
	 * @param sorts
	 * @return
	 */
	@NativeQuery(model = Industry.class)
	Pagination<Industry> findEffectIndustryListByQueryMapWithPage(Page page,Sort[] sorts,@QueryParam Map<String, Object> paraMap);

	/**
	 * 查询某一店铺下的所有行业
	 * 
	 * @param shopId
	 *            店铺ID
	 * @return
	 */
	@NativeQuery(model = Industry.class)
	List<Industry> findIndustryListByShopId(@QueryParam("shopId") Long shopId);

	/**
	 * 更具名称父级ID查询行业数
	 * 
	 * @param name
	 *            名称
	 * @param parentId
	 *            父级ID
	 * @return
	 */
	@NativeQuery(alias = "CNT",clazzes = Integer.class)
	Integer findIndustryCount(@QueryParam("name") String name,@QueryParam("parentId") Long parentId);

	/**
	 * 根据分类ID查询行业
	 * 
	 * @param categoryId
	 * @return
	 */
	@NativeQuery(model = Industry.class)
	List<Industry> findIndustryByCategoryId(@QueryParam("categoryId") Long categoryId);

	/**
	 * 根据行业名称去查询行业信息(lifecycle = 1)
	 * 
	 * @param industryNames
	 * @return
	 */
	@NativeQuery(model = Industry.class)
	List<Industry> findIndustryListByNames(@QueryParam("industryNames") List<String> industryNames);

	/**
	 * 根据商品属性扩展表(CommonProperty)的id查询使用了该属性的所有行业信息
	 * 
	 * @param industryNames
	 * @return
	 */
	@NativeQuery(model = Industry.class)
	List<Industry> findIndustryListByCommonPropertyId(@QueryParam("commonPropertyId") Long commonPropertyId);
}
