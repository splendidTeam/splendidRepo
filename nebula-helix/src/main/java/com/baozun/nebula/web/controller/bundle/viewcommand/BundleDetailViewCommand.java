/**
 
* Copyright (c) 2014 Baozun All Rights Reserved.
 
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
package com.baozun.nebula.web.controller.bundle.viewcommand;

import java.util.List;

import com.baozun.nebula.web.controller.product.viewcommand.ItemBaseInfoViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ItemExtraViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ItemImageViewCommand;
import com.baozun.nebula.web.controller.product.viewcommand.ItemReviewViewCommand;


/**
 * 捆绑商品视图层对象(bundle本身Item)
 * 
 * @see com.baozun.nebula.web.controller.bundle.viewcommand.BundleViewCommand
 */
public class BundleDetailViewCommand extends BundleViewCommand {
	
	private static final long serialVersionUID = -2719089477406692090L;
	
	/**
	 * 捆绑商品的扩展信息
	 */
	private ItemBaseInfoViewCommand itemBaseInfoViewCommand;
	
	/**
	 * 捆绑商品图片信息
	 */
	private List<ItemImageViewCommand> itemImageViewCommands;
	
	/**
	 * 捆绑商品的拓展信息
	 */
	private ItemExtraViewCommand itemExtraViewCommand;
	
	/**
	 * 捆绑商品的评论
	 */
	private ItemReviewViewCommand itemReviewViewCommand;

	public ItemBaseInfoViewCommand getItemBaseInfoViewCommand() {
		return itemBaseInfoViewCommand;
	}

	public void setItemBaseInfoViewCommand(
			ItemBaseInfoViewCommand itemBaseInfoViewCommand) {
		this.itemBaseInfoViewCommand = itemBaseInfoViewCommand;
	}

	public ItemExtraViewCommand getItemExtraViewCommand() {
		return itemExtraViewCommand;
	}

	public void setItemExtraViewCommand(ItemExtraViewCommand itemExtraViewCommand) {
		this.itemExtraViewCommand = itemExtraViewCommand;
	}

	public ItemReviewViewCommand getItemReviewViewCommand() {
		return itemReviewViewCommand;
	}

	public void setItemReviewViewCommand(ItemReviewViewCommand itemReviewViewCommand) {
		this.itemReviewViewCommand = itemReviewViewCommand;
	}

	public List<ItemImageViewCommand> getItemImageViewCommands() {
		return itemImageViewCommands;
	}

	public void setItemImageViewCommands(
			List<ItemImageViewCommand> itemImageViewCommands) {
		this.itemImageViewCommands = itemImageViewCommands;
	}
	
}
