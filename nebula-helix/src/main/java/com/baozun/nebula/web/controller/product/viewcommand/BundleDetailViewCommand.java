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
package com.baozun.nebula.web.controller.product.viewcommand;

import java.util.List;


/**
 * 捆绑商品视图层对象(bundle本身Item)
 * 
 * @see com.baozun.nebula.web.controller.bundle.viewcommand.BundleViewCommand
 */
public class BundleDetailViewCommand extends BundleViewCommand {
	
	private static final long serialVersionUID = -2719089477406692090L;
	
	/**
	 * 捆绑商品的基本信息
	 */
	private ItemBaseInfoViewCommand baseInfo;
	
	/**
	 * 捆绑商品图片信息
	 */
	private List<ItemImageViewCommand> images;
	
	/**
	 * 捆绑商品的拓展信息
	 */
	private ItemExtraViewCommand extra;
	
	/**
	 * bundleItem的属性，包括销售和功能属性
	 */
	private ItemPropertyViewCommand property;
	
	public ItemBaseInfoViewCommand getBaseInfo() {
		return baseInfo;
	}

	public void setBaseInfo(ItemBaseInfoViewCommand baseInfo) {
		this.baseInfo = baseInfo;
	}

	public List<ItemImageViewCommand> getImages() {
		return images;
	}

	public void setImages(List<ItemImageViewCommand> images) {
		this.images = images;
	}

	public ItemExtraViewCommand getExtra() {
		return extra;
	}

	public void setExtra(ItemExtraViewCommand extra) {
		this.extra = extra;
	}

	public ItemPropertyViewCommand getProperty() {
		return property;
	}

	public void setProperty(ItemPropertyViewCommand property) {
		this.property = property;
	}

}
