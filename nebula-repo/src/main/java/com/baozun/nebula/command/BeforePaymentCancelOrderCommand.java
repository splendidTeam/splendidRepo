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
package com.baozun.nebula.command;

import com.baozun.nebula.model.salesorder.SalesOrder;
import com.baozun.nebula.sdk.command.SalesOrderCommand;

/**
 * 取消订单
 * @author lxy
 * @date  
 * @version
 */
public class BeforePaymentCancelOrderCommand  implements Command{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 7200082301772753354L;

	/** 订单号 */
	private String	code;
	
	/**订单Id**/
	private Long orderId;
	
	/**
	 * 取消类型
	 * 9  用户取消
	 * 10 商城取消
	 */
	private Integer cancelType;
	
	/**ip***/
    private String clientIp;
    
    /**操作人**/
    private String operatorName;
    
    /**
     * 商城取消中
     * 是否是OMS 还是自动取消
     */
    private Boolean isOMS = false;
    
	public Boolean getIsOMS() {
		return isOMS;
	}

	public void setIsOMS(Boolean isOMS) {
		this.isOMS = isOMS;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public String getClientIp() {
		return clientIp;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Integer getCancelType() {
		return cancelType;
	}

	public void setCancelType(Integer cancelType) {
		this.cancelType = cancelType;
	}
	
    /**
     * 取消订单定时任务使用.
     *
     * @param salesOrderCommand
     *            the sales order command
     * @return the before payment cancel order command
     */
	public static BeforePaymentCancelOrderCommand buildForCancelOrderTask(SalesOrderCommand salesOrderCommand){
        BeforePaymentCancelOrderCommand beforePaymentCancelOrderCommand = new BeforePaymentCancelOrderCommand();

        beforePaymentCancelOrderCommand.setOrderId(salesOrderCommand.getId());
        beforePaymentCancelOrderCommand.setCode(salesOrderCommand.getCode());
        beforePaymentCancelOrderCommand.setIsOMS(false);
        beforePaymentCancelOrderCommand.setCancelType(SalesOrder.SALES_ORDER_STATUS_SYS_CANCELED);

        beforePaymentCancelOrderCommand.setClientIp(null);
        beforePaymentCancelOrderCommand.setOperatorName(null);
        return beforePaymentCancelOrderCommand;
    }

    /**
     * oms同步订单状态到商城端时候.
     *
     * @param salesOrderCommand
     *            the sales order command
     * @return the before payment cancel order command
     */
    public static BeforePaymentCancelOrderCommand buildForCancelOrderByOMS(SalesOrderCommand salesOrderCommand){
        BeforePaymentCancelOrderCommand beforePaymentCancelOrderCommand = new BeforePaymentCancelOrderCommand();

        beforePaymentCancelOrderCommand.setOrderId(salesOrderCommand.getId());
        beforePaymentCancelOrderCommand.setCode(salesOrderCommand.getCode());
        beforePaymentCancelOrderCommand.setIsOMS(true);
        beforePaymentCancelOrderCommand.setCancelType(SalesOrder.SALES_ORDER_STATUS_SYS_CANCELED);

        beforePaymentCancelOrderCommand.setClientIp(null);
        beforePaymentCancelOrderCommand.setOperatorName(null);
        return beforePaymentCancelOrderCommand;
    }
}
