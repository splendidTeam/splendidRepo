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
package com.baozun.nebula.utilities.integration.payment;

import java.io.Serializable;

import com.baozun.nebula.utilities.common.command.PaymentServiceReturnCommand;

/**
 * The Class PaymentResult.
 */
public class PaymentResult implements Serializable{

	/** The Constant serialVersionUID. */
	private static final long		serialVersionUID	= 6467848404805932238L;

	/** The payment service satus. */
	private PaymentServiceStatus	paymentServiceSatus;
	
	private String responseValue;

	/** 文本,不一定解析, 如果需要 可以用,不需要就不用 . */
	private String					message;

	/** 传递参数使用, 他们给的参数,如果需要 可以用,不需要就不用. */
	private PaymentServiceReturnCommand 	paymentStatusInformation;
	
	/**
	 * Gets the payment service satus.
	 *
	 * @return the payment service satus
	 */
	public PaymentServiceStatus getPaymentServiceSatus(){
		return paymentServiceSatus;
	}

	/**
	 * Sets the payment service satus.
	 *
	 * @param paymentServiceSatus the new payment service satus
	 */
	public void setPaymentServiceSatus(PaymentServiceStatus paymentServiceSatus){
		this.paymentServiceSatus = paymentServiceSatus;
	}

	/**
	 * Gets the 文本,不一定解析, 如果需要 可以用,不需要就不用 .
	 *
	 * @return the 文本,不一定解析, 如果需要 可以用,不需要就不用 
	 */
	public String getMessage(){
		return message;
	}

	/**
	 * Sets the 文本,不一定解析, 如果需要 可以用,不需要就不用 .
	 *
	 * @param message the new 文本,不一定解析, 如果需要 可以用,不需要就不用 
	 */
	public void setMessage(String message){
		this.message = message;
	}

	/**
	 * Gets the 传递参数使用, 他们给的参数,如果需要 可以用,不需要就不用.
	 *
	 * @return the 传递参数使用, 他们给的参数,如果需要 可以用,不需要就不用
	 */
	public PaymentServiceReturnCommand getPaymentStatusInformation(){
		return paymentStatusInformation;
	}

	/**
	 * Sets the 传递参数使用, 他们给的参数,如果需要 可以用,不需要就不用.
	 *
	 * @param paymentStatusInformation the new 传递参数使用, 他们给的参数,如果需要 可以用,不需要就不用
	 */
	public void setPaymentStatusInformation(PaymentServiceReturnCommand paymentServiceReturnCommand){
		this.paymentStatusInformation = paymentServiceReturnCommand;
	}

	public String getResponseValue() {
		return responseValue;
	}

	public void setResponseValue(String responseValue) {
		this.responseValue = responseValue;
	}


}
