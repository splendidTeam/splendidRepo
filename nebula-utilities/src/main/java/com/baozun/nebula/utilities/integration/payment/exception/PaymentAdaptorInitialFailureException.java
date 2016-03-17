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
package com.baozun.nebula.utilities.integration.payment.exception;

/**
 * PaymentAdaptor 初始化 失败
 */
public class PaymentAdaptorInitialFailureException extends PaymentException{

	private static final long	serialVersionUID	= 7402976801896652285L;

	public PaymentAdaptorInitialFailureException(){}

	public PaymentAdaptorInitialFailureException(String arg0){
		super(arg0);
	}

	public PaymentAdaptorInitialFailureException(Throwable arg0){
		super(arg0);
	}

	public PaymentAdaptorInitialFailureException(String arg0, Throwable arg1){
		super(arg0, arg1);
	}
}
