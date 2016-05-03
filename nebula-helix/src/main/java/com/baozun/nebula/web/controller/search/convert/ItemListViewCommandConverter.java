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
package com.baozun.nebula.web.controller.search.convert;

import com.baozun.nebula.search.command.SearchResultPage;
import com.baozun.nebula.solr.command.ItemForSolrCommand;
import com.baozun.nebula.web.controller.BaseConverter;
import com.baozun.nebula.web.controller.search.viewcommand.ItemListViewCommand;

import loxia.utils.PropertyUtil;

/**
 * 商品列表模型转换
 * 
 * @author 冯明雷
 * @version 1.0
 * @time 2016年4月28日 下午6:49:24
 */
public class ItemListViewCommandConverter extends BaseConverter<ItemListViewCommand>{

	private static final long serialVersionUID = -8622427014008080905L;

	/**
	 * 这个方法暂时没有用
	 */
	public ItemListViewCommand convert(Object data){
		return null;
	}

	/**
	 * 转换viewCommand
	 * 
	 * @return ItemListViewCommand
	 * @param searchResultPage
	 * @author 冯明雷
	 * @time 2016年5月3日下午2:29:01
	 */
	public ItemListViewCommand convertViewCommand(SearchResultPage<ItemForSolrCommand> pageData){
		if (pageData == null)
			return null;

		ItemListViewCommand result = new ItemListViewCommand();
		try{
			PropertyUtil.copyProperties(result, pageData);
			result.setItemForSolrCommands(pageData.getItems());
			result.setFacetGroups(pageData.getFacetGroups());
		}catch (Exception e){
			e.printStackTrace();
		}

		return result;
	}
}
