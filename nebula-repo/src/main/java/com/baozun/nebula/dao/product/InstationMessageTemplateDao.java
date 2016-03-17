/**
 * Copyright (c) 2010 Jumbomart All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Jumbomart.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Jumbo.
 *
 * JUMBOMART MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. JUMBOMART SHALL NOT BE LIABLE FOR ANY DAMAGES
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

import com.baozun.nebula.model.system.InstationMessageTemplate;

/**
 * @ClassName: InstationMessageTemplateDao
 * @Description:(站内信息DAO)
 * @author GEWEI.LU
 * @date 2016年1月15日 下午3:16:25
 */
public interface InstationMessageTemplateDao extends GenericEntityDao<InstationMessageTemplate, Long> {
	/**
	 * @Title: findTempletByQueryMapWithPage
	 * @Description:(根据分页信息查找模板)
	 * @param @param page
	 * @param @param sorts
	 * @param @param paraMap
	 * @param @return 设定文件
	 * @return Pagination<InstationMessageTemplate> 返回类型
	 * @throws
	 * @date 2016年1月15日 下午4:02:54
	 * @author GEWEI.LU
	 */
	@NativeQuery(model = InstationMessageTemplate.class)
	Pagination<InstationMessageTemplate> findTempletByQueryMapWithPage(Page page, Sort[] sorts, @QueryParam Map<String, Object> paraMap);

	/**
	 * @Title: updateinstationMessageTemplate
	 * @Description:(修改)
	 * @param @param itemId
	 * @param @return 设定文件
	 * @return Integer 返回类型
	 * @throws
	 * @date 2016年1月15日 下午4:10:39
	 * @author GEWEI.LU
	 */
	@NativeUpdate
	Integer updateinstationMessageTemplate(@QueryParam("type") Long type,@QueryParam("id") Long itemId);
	
	
	/** 
	* @Title: finInstationMessageTemplatelist 
	* @Description:(查询有用的站信息模板) 
	* @param @return    设定文件 
	* @return List<InstationMessageTemplate>    返回类型 
	* @throws 
	* @date 2016年1月15日 下午7:24:30 
	* @author GEWEI.LU   
	*/
	@NativeQuery(model = InstationMessageTemplate.class)
	List<InstationMessageTemplate> finInstationMessageTemplatelist();
	
	
	
	/** 
	* @Title: finInstationMessageTemplatelist 
	* @Description:(查询站信息模板) 
	* @param @return    设定文件 
	* @return List<InstationMessageTemplate>    返回类型 
	* @throws 
	* @date 2016年1月15日 下午7:24:30 
	* @author GEWEI.LU   
	*/
	@NativeQuery(model = InstationMessageTemplate.class)
	InstationMessageTemplate findTempletByid(@QueryParam("id") Long id);
}
