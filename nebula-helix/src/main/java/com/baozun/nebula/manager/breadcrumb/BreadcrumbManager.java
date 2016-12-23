/**
 * Copyright (c) 2015 Baozun All Rights Reserved.
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
package com.baozun.nebula.manager.breadcrumb;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.baozun.nebula.exception.IllegalItemStateException;
import com.baozun.nebula.manager.BaseManager;
import com.baozun.nebula.model.product.ItemCollection;
import com.baozun.nebula.sdk.command.CurmbCommand;
import com.baozun.nebula.search.ItemCollectionContext;

/**   
 * 面包屑
 * @Description 
 * @author dongliang ma
 * @date 2016年5月16日 下午4:23:17 
 * @version   
 */
public interface BreadcrumbManager extends BaseManager{
	
	/**
	 * 加载所有导航商品集合关系(key:导航id;value:ItemCollection)
	 * @return
	 */
	Map<Long, ItemCollection> 	loadNavItemCollectionMap();
	
	/**
	 * 获取PDP页面商品的面包屑
	 * @param navId 导航ID
	 * @param itemId 商品ID
	 * @param request
	 * @return
	 * @throws IllegalItemStateException 
	 */
	List<CurmbCommand> findCurmbCommands(Long navId, Long itemId, HttpServletRequest request) throws IllegalItemStateException;
	
	
	/**
	 * 根据导航ID构造面包屑树
	 * @param navId
	 * @return
	 */
	List<CurmbCommand> createCurmbCommandsByNavId(Long navId);
	
	
	/**
	 * 根据itemId构造出ItemCollectionContext
	 * @param itemId
	 * @return
	 * @throws IllegalItemStateException 
	 */
	ItemCollectionContext constructItemCollectionContextByItemId(Long itemId) throws IllegalItemStateException;
}
