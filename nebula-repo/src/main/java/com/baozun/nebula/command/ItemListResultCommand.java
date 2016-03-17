/**
 * Copyright (c) 2013 Baozun All Rights Reserved.
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
package com.baozun.nebula.command;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Tianlong.Zhang
 *
 */
public class ItemListResultCommand implements Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3333306410972565524L;
	
	private List<ItemCommand> itemCmdList = new ArrayList<ItemCommand>();
	
	public void setItemCmdList(List<ItemCommand> itemCmdList) {
		this.itemCmdList = itemCmdList;
	}

	public List<ItemCommand> getItemCmdList() {
		return itemCmdList;
	}

}
