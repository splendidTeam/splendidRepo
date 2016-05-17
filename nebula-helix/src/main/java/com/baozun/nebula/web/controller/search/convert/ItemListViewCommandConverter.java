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
import com.feilong.core.Validator;

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
		if (data == null) {
			return null;
		}

		if (data instanceof SearchResultPage) {
			ItemListViewCommand result = new ItemListViewCommand();

			SearchResultPage<ItemForSolrCommand> pageData = (SearchResultPage<ItemForSolrCommand>) data;			
			//分组
			if (Validator.isNotNullOrEmpty(pageData.getItemsListWithGroup())
					&& Validator.isNotNullOrEmpty(pageData.getItemsListWithGroup().getItems())) {
				result.setItemsListWithGroup(pageData.getItemsListWithGroup().getItems());
				result.setCount(pageData.getItemsListWithGroup().getCount());
				result.setCurrentPage(pageData.getItemsListWithGroup().getCurrentPage());
				result.setSize(pageData.getItemsListWithGroup().getSize());
				result.setStart(pageData.getItemsListWithGroup().getStart());
				result.setTotalPages(pageData.getItemsListWithGroup().getTotalPages());
			}else if (Validator.isNotNullOrEmpty(pageData.getItemsListWithOutGroup())
					&& Validator.isNotNullOrEmpty(pageData.getItemsListWithOutGroup().getItems())) {
				//不分组
				result.setItemsListWithOutGroup(pageData.getItemsListWithOutGroup().getItems());
				result.setCount(pageData.getItemsListWithOutGroup().getCount());
				result.setCurrentPage(pageData.getItemsListWithOutGroup().getCurrentPage());
				result.setSize(pageData.getItemsListWithOutGroup().getSize());
				result.setStart(pageData.getItemsListWithOutGroup().getStart());
				result.setTotalPages(pageData.getItemsListWithOutGroup().getTotalPages());
			}
			
			result.setFacetGroups(pageData.getFacetGroups());
			
			return result;

		}
		return null;
	}
}
