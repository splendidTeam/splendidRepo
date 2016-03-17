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
package com.baozun.nebula.dao.product;

import java.util.List;
import java.util.Map;

import loxia.annotation.NativeQuery;
import loxia.annotation.NativeUpdate;
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;

import com.baozun.nebula.model.product.CommonProperty;

/**
 * 商品属性Dao(扩展用)
 *
 * @author xueshan.li
 * @date 2015年11月11日 上午10:22:24
 */
public interface CommonPropertyDao extends GenericEntityDao<CommonProperty, Long>{

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
	Integer updateCommonPropertyById(@QueryParam("id") Long id,@QueryParam Map<String, Object> paraMap);

	/**
	 * 查询所有通用属性表信息
	 * 
	 * @return
	 */
	@NativeQuery(model = CommonProperty.class)
	List<CommonProperty> findAllCommonProperty();

	/**
	 * 根据行业ID查找通用属性表中没有被该行业属性关联过的且其通用属性名与该行业的属性名没有发生过重复的记录
	 * 
	 * @param industryId
	 * @return
	 */
	@NativeQuery(model = CommonProperty.class)
	List<CommonProperty> findAllCommonPropertyByindustryId(@QueryParam("industryId") Long industryId);

	/**
	 * 验证通用属性名是否存在
	 * 
	 * @param commonPropertyName
	 * @return
	 */
	@NativeQuery(alias = "num",clazzes = Long.class)
	Long  validatecommonPropertyName(@QueryParam("commonPropertyName") String commonPropertyName);
}
