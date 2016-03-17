package com.baozun.nebula.command.product;

import com.baozun.nebula.command.Command;

public class ProductInventoryCommand implements Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1887077263900440358L;
	/**
	 * 商品编号
	 */
	private Long itemId;
	/**
	 * 当前库存数
	 */
	private Long leftInvt;

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public Long getLeftInvt() {
		return leftInvt;
	}

	public void setLeftInvt(Long leftInvt) {
		this.leftInvt = leftInvt;
	}
}
