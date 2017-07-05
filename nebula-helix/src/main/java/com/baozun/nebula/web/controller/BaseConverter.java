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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import loxia.dao.Pagination;
import loxia.utils.PropListCopyable;
import loxia.utils.PropertyUtil;

/**
 * 基础转换器，所有ViewCommand的转换器的基类。
 * 每个实现类中实现一个单元转换方法后，默认拥有列表转换和分页转换两个方法。如果需要其他转换方法，请在扩展类中新增，如
 * 
 * public T convertFromTwoObjects(Object obj1, Object obj2){
 * }
 * 
 * @author Benjamin.Liu
 *
 * @param <T> 每个最终转换器都需显式定义其最终转换的数据模型类型。
 */
public abstract class BaseConverter<T extends BaseViewCommand> implements Serializable{

/** The Constant log. */
private static final Logger LOGGER = LoggerFactory.getLogger(BaseConverter.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 2549552226190173876L;

	/**
	 * 转换器默认的转换方法，可以根据入参类型不同进行数据的转换。示例代码如下：
	 * 
	 * 	if(data == null) return null;
	 *	
	 *	if(data instanceof MemberCommand){
	 *		MemberCommand memberCommand = (MemberCommand) data;
	 *		//TODO 
	 *		//完成转换
	 *	}else if(data instance of Member){
	 *		Member member = (Member) data;
	 *		//TODO
	 *		//完成转换
	 *	}else{
	 *		throw new UnsupportDataTypeException(data.getClass() + " cannot convert to " + MemberViewCommand.class + "yet.");
	 *	}
	 *
	 * @param data
	 * @return
	 */
	public abstract T convert(Object data);
	
	/**
	 * 转换列表的默认转换方法，其中单元转换方法使用的是转换器默认的转换方法
	 * 
	 * @param listData
	 * @return
	 */
	public List<T> convert(List<? extends Object> listData){
		if(listData == null) return null;
		
		List<T> resultList = new ArrayList<T>();
		for(Object obj: listData){
			resultList.add(convert(obj));
		}
		return resultList;
	}
	
	/**
	 * 转换分页对象的默认转换方法，其中单元转换方法使用的是转换器默认的转换方法
	 * 
	 * @param pageData
	 * @return
	 */
	public Pagination<T> convert(Pagination<? extends Object> pageData){
		if(pageData == null) return null;
		
		Pagination<T> resultPageData = new Pagination<T>();
		try {
			PropertyUtil.copyProperties(pageData, resultPageData, 
					new PropListCopyable("count","currentPage","totalPages","start","size","sortStr"));
		} catch (Exception e) {
			//TODO should not occur
			e.printStackTrace();
		}
		resultPageData.setItems(convert(pageData.getItems()));
		return resultPageData;
	}
}
