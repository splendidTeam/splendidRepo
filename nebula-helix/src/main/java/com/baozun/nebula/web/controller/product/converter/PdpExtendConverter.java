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
package com.baozun.nebula.web.controller.product.converter;

import java.util.ArrayList;
import java.util.List;

import loxia.dao.Pagination;
import loxia.utils.PropListCopyable;
import loxia.utils.PropertyUtil;

import com.baozun.nebula.web.controller.BaseConverter;
import com.baozun.nebula.web.controller.BaseViewCommand;

/**
 * pdp的扩展抽象converter，以便于新增需要的方法
 * 其他功能模块需要自己extend类
 * @author chengchao
 * @param <T>
 *
 */
public abstract class PdpExtendConverter<T> extends
		BaseConverter<BaseViewCommand> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5629671264033579846L;

	public abstract T convertFromTwoObjects(Object obj1, Object obj2);
	
	/**
	 * 转换列表的默认转换方法，其中单元转换方法使用的是转换器默认的转换方法
	 * 
	 * @param listData
	 * @return
	 */
	public List<T> convertFromTwoObjects(List<? extends Object> obj1,Object obj2){
		if(obj1 == null) return null;
		
		List<T> resultList = new ArrayList<T>();
		for(Object obj: obj1){
			resultList.add(convertFromTwoObjects(obj,obj2));
		}
		return resultList;
	}
	
	/**
	 * 转换分页对象的默认转换方法，其中单元转换方法使用的是转换器默认的转换方法
	 * 
	 * @param pageData
	 * @return
	 */
	public Pagination<T> convertFromTwoObjects(Pagination<? extends Object> obj1,Object obj2){
		if(obj1 == null) return null;
		
		Pagination<T> resultPageData = new Pagination<T>();
		try {
			PropertyUtil.copyProperties(obj1, resultPageData, 
					new PropListCopyable("count","currentPage","totalPages","start","size","sortStr"));
		} catch (Exception e) {
			//TODO should not occur
			e.printStackTrace();
		}
		resultPageData.setItems(convertFromTwoObjects(obj1.getItems(),obj2));
		return resultPageData;
	}
}
