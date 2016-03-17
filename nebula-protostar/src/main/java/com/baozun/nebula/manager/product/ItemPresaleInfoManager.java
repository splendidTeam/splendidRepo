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
package com.baozun.nebula.manager.product;

import com.baozun.nebula.command.ItemPresalseInfoCommand;
import com.baozun.nebula.manager.BaseManager;

/**
 * 商品预售信息处理接口
 * 
 * @author jinbao.ji
 * @date 2016年2月3日 上午9:56:53
 */
public interface ItemPresaleInfoManager extends BaseManager{

	/**
	 * 根据itemId获取商品预售信息
	 * 
	 * @param itemId
	 */
	ItemPresalseInfoCommand getItemPresalseInfoCommand(Long itemId);

	/**
	 * 保存或更新预售信息
	 * 
	 * @param itemPresalseInfoCommand
	 */
	void updateOrSaveItemPresalseInfo(ItemPresalseInfoCommand itemPresalseInfoCommand);

	/**
	 * 验证前端提交的参数的合法性
	 */
	boolean validateitemPresalseInfoCommand(ItemPresalseInfoCommand itemPresalseInfoCommand);

}
