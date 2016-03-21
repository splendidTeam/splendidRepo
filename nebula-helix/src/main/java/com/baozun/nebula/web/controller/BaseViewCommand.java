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
import java.util.HashMap;
import java.util.Map;

/**
 * 视图层模型的基类
 * 
 * @author Benjamin.Liu
 *
 */
public class BaseViewCommand implements Serializable {
	
	private static final long serialVersionUID = -3993085606710273959L;
	
	/**
	 * 自定义数据，未来某些简单或者过于定制的数据扩展可以直接在这里处理。
	 */
	private Map<String, Object> extraData = new HashMap<String, Object>(0);
	public Map<String, Object> getExtraData() {
		return extraData;
	}
	public void addExtraData(String key, Object value) {
		this.getExtraData().put(key, value);
	}
	

}