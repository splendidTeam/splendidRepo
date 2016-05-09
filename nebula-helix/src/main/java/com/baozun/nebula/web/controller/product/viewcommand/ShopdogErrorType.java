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

import java.io.Serializable;

/**
 * Shopdog command的基类
 * 
 */
public class ShopdogErrorType implements Serializable {

	private static final long serialVersionUID = -5230237621269913529L;
	
	// 通用异常
	/** 通用异常, 900000:系统异常 */
	public static final ShopdogErrorType COMMON_SYSTEM_ERROR = new ShopdogErrorType("900000", "系统异常");
	
	/** 通用异常, 900001:非法的参数 */
	public static final ShopdogErrorType COMMON_PARAMETER_ERROR = new ShopdogErrorType("900001", "非法的参数");
	
	// 商品状态异常
	/** 商品状态异常, 100000:商品状态异常 */
	public static final ShopdogErrorType ITEM_ILLEGAL_STATE = new ShopdogErrorType("100000", "商品状态异常");
	
	/** 商品状态异常, 100001:商品不存在 */
	public static final ShopdogErrorType ITEM_NOT_EXISTS = new ShopdogErrorType("100001", "商品不存在");
	
	/** 商品状态异常, 100002:商品已下架 */
	public static final ShopdogErrorType ITEM_LIFECYCLE_OFFSALE = new ShopdogErrorType("100002", "商品已下架");
	
	/** 商品状态异常, 100003:商品已删除 */
	public static final ShopdogErrorType ITEM_LIFECYCLE_LOGICAL_DELETED = new ShopdogErrorType("100003", "商品已删除");
	
	/** 商品状态异常, 100004:商品未上架 */
	public static final ShopdogErrorType ITEM_LIFECYCLE_NEW = new ShopdogErrorType("100004", "商品未上架");
	
	/** 商品状态异常, 100005:商品未到上架时间 */
	public static final ShopdogErrorType ITEM_BEFORE_ACTIVE_TIME = new ShopdogErrorType("100005", "商品未到上架时间");
	
	private String errorCode;
	
	private String message;

	public ShopdogErrorType(final String errorCode, final String message) {
		super();
		this.errorCode = errorCode;
		this.message = message;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
