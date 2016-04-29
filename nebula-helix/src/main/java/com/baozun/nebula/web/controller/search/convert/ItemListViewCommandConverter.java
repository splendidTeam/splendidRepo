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
import com.baozun.nebula.solr.command.ItemForSolrI18nCommand;
import com.baozun.nebula.web.controller.BaseConverter;
import com.baozun.nebula.web.controller.UnsupportDataTypeException;
import com.baozun.nebula.web.controller.search.viewcommand.ItemListViewCommand;

/**
 * 商品列表模型转换
 * 
 * @author 冯明雷
 * @version 1.0
 * @time 2016年4月28日 下午6:49:24
 */
public class ItemListViewCommandConverter extends BaseConverter<ItemListViewCommand>{

	private static final long serialVersionUID = -8622427014008080905L;

	public ItemListViewCommand convert(Object data){
		if (null == data) {
			return null;
		}
		if (data instanceof SearchResultPage) {
			ItemListViewCommand itemListViewCommand = new ItemListViewCommand();
			try{
				SearchResultPage<ItemForSolrI18nCommand> searchResultPage = (SearchResultPage<ItemForSolrI18nCommand>) data;
				itemListViewCommand.setItemForSolrI18nCommands(searchResultPage.getItems());
				return itemListViewCommand;
			}catch (Exception e){
				e.printStackTrace();
			}
		}else{
			throw new UnsupportDataTypeException(data.getClass() + " cannot convert to " + ItemListViewCommandConverter.class + "yet.");
		}
		return null;
	}
}
