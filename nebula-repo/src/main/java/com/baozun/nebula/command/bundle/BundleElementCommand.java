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
package com.baozun.nebula.command.bundle;
import java.util.ArrayList;
import java.util.List;

import com.baozun.nebula.model.bundle.BundleElement;

/**
 　  * 
  * @ClassName: BundleElementCommand
  * @author chainyu
  * @date 2016年4月15日 上午10:14:39
  * {@link com.baozun.nebula.model.bundle.BundleElement}
 */
public class BundleElementCommand extends BundleElement{

	private static final long serialVersionUID = -8462331832705645692L;
	
	/**
	 * <ul>
	 * 	  <li>BundleItemCommand中需要包含商品的基本信息，以及一个ITEM参加bundle的所有sku</li>
	 * 	  <li>sku需要其一些基本信息，以及对应的属性Map<propertyId,propertyValueName></li>
	 * </ul>
	 */
	private List<BundleItemCommand> items = new ArrayList<BundleItemCommand>();

	public List<BundleItemCommand> getItems() {
		return items;
	}

	public void setItems(List<BundleItemCommand> items) {
		this.items = items;
	}
	
}
