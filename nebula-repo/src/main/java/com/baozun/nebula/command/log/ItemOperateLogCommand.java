package com.baozun.nebula.command.log;

import com.baozun.nebula.model.product.ItemOperateLog;

public class ItemOperateLogCommand extends ItemOperateLog{


	private static final long serialVersionUID = 4345357693892694517L;
	
	

	/**
	 * 商品编码
	 */
	private String code;
	
	/**
	 * 商品名称
	 */
	private String	title;
	
	

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
