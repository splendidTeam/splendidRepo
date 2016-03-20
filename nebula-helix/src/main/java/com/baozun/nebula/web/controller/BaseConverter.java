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
package com.baozun.nebula.web.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import loxia.dao.Pagination;
import loxia.utils.PropListCopyable;
import loxia.utils.PropertyUtil;

public abstract class BaseConverter<T extends BaseViewCommand> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2549552226190173876L;

	public abstract T convert(Object data);
	
	public List<T> convert(List<? extends Object> listData){
		if(listData == null) return null;
		
		List<T> resultList = new ArrayList<T>();
		for(Object obj: listData){
			resultList.add(convert(obj));
		}
		return resultList;
	}
	
	public Pagination<T> convert(Pagination<? extends Object> pageData){
		if(pageData == null) return null;
		
		Pagination<T> resultPageData = new Pagination<T>();
		try {
			PropertyUtil.copyProperties(pageData, resultPageData, 
					new PropListCopyable("count","currentPage","totalPages","start","size","sortStr"));
		} catch (Exception e) {
			//should not occur
			e.printStackTrace();
		}
		resultPageData.setItems(convert(pageData.getItems()));
		return resultPageData;
	}
}
