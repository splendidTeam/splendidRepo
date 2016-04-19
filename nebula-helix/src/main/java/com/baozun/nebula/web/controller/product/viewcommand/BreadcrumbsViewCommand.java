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
package com.baozun.nebula.web.controller.product.viewcommand;

import com.baozun.nebula.web.controller.BaseViewCommand;

/**
 * 面包屑
 * <p>这里只定义了面包屑的每一项，一个完整的面包屑应该是该类的列表，而且这个列表应该是有序的。</p>
 * 
 * @author yimin.qiao
 *
 */
public class BreadcrumbsViewCommand extends BaseViewCommand {

	private static final long serialVersionUID = -9009176806400593148L;

	/**
	 * 面包屑每一项的显示名称
	 */
	private String title;
	
	/**
	 * 面包屑每一项的路径
	 */
	private String path;
	
	/**
	 * 
	 */
	public BreadcrumbsViewCommand() {
		super();
	}

	public BreadcrumbsViewCommand(String title, String path) {
		super();
		this.title = title;
		this.path = path;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
