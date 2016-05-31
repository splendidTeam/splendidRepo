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
 *
 */
package com.baozun.nebula.web.command;

import java.util.List;
import java.util.Map;

import com.baozun.nebula.command.Command;
import com.baozun.nebula.model.product.Item;

/**
 * 商品关联分类 结果 command.
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 1.0 Jun 28, 2013 3:16:50 PM
 */
public class ItemCategoryResultCommand implements Command{

	/** The Constant serialVersionUID. */
	private static final long		serialVersionUID	= -9071701751952208064L;

	/**
	 * 关联 分类 失败的商品 <br>
	 * key 为 分类名称, value 为item.
	 */
	private Map<String, List<Item>>	failMap;

	/**
	 * 无须重复关联 商品<br>
	 * key 为 分类名称, value 为item.
	 */
	private Map<String, List<Item>>	repeatMap;

	/**
	 * 关联成功 商品<br>
	 * key 为 分类名称, value 为item.
	 */
	private Map<String, List<Item>>	successMap;

	/**
	 * Gets the 关联 分类 失败的商品 <br>
	 * key 为 分类名称, value 为item.
	 * 
	 * @return the failMap
	 */
	public Map<String, List<Item>> getFailMap(){
		return failMap;
	}

	/**
	 * Gets the 无须重复关联 商品<br>
	 * key 为 分类名称, value 为item.
	 * 
	 * @return the repeatMap
	 */
	public Map<String, List<Item>> getRepeatMap(){
		return repeatMap;
	}

	/**
	 * Gets the 关联成功 商品<br>
	 * key 为 分类名称, value 为item.
	 * 
	 * @return the successMap
	 */
	public Map<String, List<Item>> getSuccessMap(){
		return successMap;
	}

	/**
	 * Sets the 关联 分类 失败的商品 <br>
	 * key 为 分类名称, value 为item.
	 * 
	 * @param failMap
	 *            the failMap to set
	 */
	public void setFailMap(Map<String, List<Item>> failMap){
		this.failMap = failMap;
	}

	/**
	 * Sets the 无须重复关联 商品<br>
	 * key 为 分类名称, value 为item.
	 * 
	 * @param repeatMap
	 *            the repeatMap to set
	 */
	public void setRepeatMap(Map<String, List<Item>> repeatMap){
		this.repeatMap = repeatMap;
	}

	/**
	 * Sets the 关联成功 商品<br>
	 * key 为 分类名称, value 为item.
	 * 
	 * @param successMap
	 *            the successMap to set
	 */
	public void setSuccessMap(Map<String, List<Item>> successMap){
		this.successMap = successMap;
	}

}
