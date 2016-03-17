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
package com.baozun.nebula.command.product;

import com.baozun.nebula.command.Command;
import com.baozun.nebula.command.ItemCommand;

/**
 * 商品咨询command 包含了 商品（Item）和 具体咨询内容（Consultant）
 * @author Tianlong.Zhang
 *
 */
public class ItemConsultantCommand implements Command{

	private static final long serialVersionUID = -7380980475286527863L;

	private ItemCommand itemCommand;
	
	private ConsultantCommand consultantCommand;

	/**
	 * @return the itemCommand
	 */
	public ItemCommand getItemCommand() {
		return itemCommand;
	}

	/**
	 * @param itemCommand the itemCommand to set
	 */
	public void setItemCommand(ItemCommand itemCommand) {
		this.itemCommand = itemCommand;
	}

	/**
	 * @return the consultantCommand
	 */
	public ConsultantCommand getConsultantCommand() {
		return consultantCommand;
	}

	/**
	 * @param consultantCommand the consultantCommand to set
	 */
	public void setConsultantCommand(ConsultantCommand consultantCommand) {
		this.consultantCommand = consultantCommand;
	}
	
}
