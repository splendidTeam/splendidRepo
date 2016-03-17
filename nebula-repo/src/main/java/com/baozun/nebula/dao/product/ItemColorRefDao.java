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
import loxia.dao.Page;
import loxia.dao.Pagination;
import loxia.dao.Sort;

import com.baozun.nebula.model.product.ItemColorValueRef;


/** 
* @ClassName: ItemColorRefDao 
* @Description: (查询商品颜色对照表数据) 
* @author gewei.lu <gewei.lu@baozun.cn> 
* @date 2016年1月7日 下午2:13:27 
*  
*/
public interface ItemColorRefDao extends GenericEntityDao<ItemColorValueRef, Long>{
	
	
	/** 
	* @Title: findItemColorRefList 
	* @Description:(查询商品颜色对照表全部数据  带参数) 
	* @param @param page
	* @param @param sorts
	* @param @param paraMap
	* @param @return    设定文件 
	* @return Pagination<ItemColorValueRef>    返回类型 
	* @throws 
	*/
	@NativeQuery(model = ItemColorValueRef.class,withGroupby=true)
	public Pagination<ItemColorValueRef> findItemColorRefList(Page page, Sort[] sorts,@QueryParam("paraMap") Map<String, Object> paraMap);
	
	
	
	/** 
	* @Title: itemColorValueReflistMap 
	* @Description:(查询商品颜色对照表全部数据  带参数) 
	* @param @param page
	* @param @param sorts
	* @param @param List
	* @param @return    设定文件 
	* @return List    返回类型 
	* @throws 
	*/
	@NativeQuery(model = ItemColorValueRef.class)
	public List<ItemColorValueRef> itemColorValueReflistMap();
	
	
	
	/** 
	* @Title: deletetemColorRef 
	* @Description: (这里用一句话描述这个方法的作用) 
	* @param @param id
	* @param @return    设定文件 
	* @return int    返回类型 
	* @throws 
	*/
	@NativeUpdate
	int deletetemColorRef(@QueryParam("id") Long id);
	
	
	
	/**
	 * findAllItemCololrAndColorValue:加载所有颜色筛选色 <br/>
	 * @author ArvinChang
	 * @return
	 */
	@NativeQuery(model = ItemColorValueRef.class)
	public List<ItemColorValueRef> findAllItemCololr();
}
