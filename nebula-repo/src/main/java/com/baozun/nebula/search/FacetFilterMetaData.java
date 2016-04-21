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
package com.baozun.nebula.search;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * facet结果显示使用的元数据
 * 
 * @author D.C
 */
public class FacetFilterMetaData implements Serializable{

	private static final long	serialVersionUID	= 1337274838133771487L;

	/**
	 * key 一般是facet结果的key部分 value 一般是关于key的明细对象
	 */
	private Map<String, Object> 	metaMap	= new HashMap<String, Object>();

	public Map<String, Object> getMetaMap(){
		return metaMap;
	}

	public void add(String key,Object value){
		metaMap.put(key, value);
	}

	public void clear(){
		metaMap.clear();
	}

}
