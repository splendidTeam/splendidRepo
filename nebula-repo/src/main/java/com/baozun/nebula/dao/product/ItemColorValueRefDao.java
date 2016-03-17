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
import loxia.annotation.QueryParam;
import loxia.dao.GenericEntityDao;
import com.baozun.nebula.model.product.ItemColorReference;


/** 
* @ClassName: ItemColorValueRefDao 
* @Description:(筛选色对照DAO) 
* @author GEWEI.LU 
* @date 2016年1月13日 上午10:18:45  
*/
public interface ItemColorValueRefDao extends GenericEntityDao<ItemColorReference, Long>{
	/** 
	* @Title: findItItemColorReferenceList 
	* @Description:(查询是否有一存在的数据) 
	* @param @param paraMap
	* @param @return    设定文件 
	* @return List<ItemColorReference>    返回类型 
	* @throws 
	* @date 2016年1月13日 上午10:20:36 
	* @author GEWEI.LU   
	*/
	@NativeQuery(model = ItemColorReference.class,withGroupby=true)
	public List<ItemColorReference> findItItemColorReferenceList(@QueryParam("paraMap") Map<String, Long> paraMap);
}
