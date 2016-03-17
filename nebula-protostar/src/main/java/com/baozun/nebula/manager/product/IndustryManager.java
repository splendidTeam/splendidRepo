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
package com.baozun.nebula.manager.product;

import java.util.List;

import loxia.dao.Sort;

import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.product.Category;
import com.baozun.nebula.model.product.Industry;
import com.baozun.nebula.web.command.TreeCommand;

/**
 * 行业管理manager
 * 
 * @author kefan.chen
 */
public interface IndustryManager extends BaseManager{

	/**
	 * 查询出所有可用的商品分类
	 * 
	 * @return
	 */
	List<Industry> findAllIndustryList();

	/**
	 * 添加或修改行业
	 * 
	 * @param Industry
	 *            行业实体
	 * @param ids
	 *            id集合
	 * @return boolean
	 */
	boolean createOrUpdateIndustry(Industry industry,String ids);

	/**
	 * 逻辑删除行业
	 * 
	 * @param ID
	 *            节点Id
	 * @return boolean
	 */
	Integer removeIndustryByIds(List<Long> ids);

	/**
	 * 根据 id查询行业
	 * @param id
	 * @return
	 */
	Industry findIndustryById(Long id);
	
	/**
	 * 验证名称唯一性
	 * @param id
	 * @return
	 */
	boolean validateIndustryName(Long pId,String name);
	
	/**
	 * 查询根据categoryId
	 * @param categoryId
	 * @return
	 */
	List<Industry> findIndustryByCategoryId(Long categoryId);
}
