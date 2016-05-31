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
package com.baozun.nebula.web.controller.order.viewcommand;

import com.baozun.nebula.web.controller.BaseViewCommand;

/**
 * 订单里面的支付信息.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 5.3.1 2016年5月12日 下午3:16:25
 * @see com.baozun.nebula.model.salesorder.Consignee
 * @see com.baozun.nebula.sdk.command.ConsigneeCommand
 * @since 5.3.1
 */
public class PaymentInfoSubViewCommand extends BaseViewCommand{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -6400008677521495969L;

    /** 支付方式. */
    private Integer           payment;
    
    /** 支付流水号 **/
    private String           subOrdinate;

    /**
     * 获得 支付方式.
     *
     * @return the payment
     */
    public Integer getPayment(){
        return payment;
    }

    /**
     * 设置 支付方式.
     *
     * @param payment
     *            the payment to set
     */
    public void setPayment(Integer payment){
        this.payment = payment;
    }

    public String getSubOrdinate() {
        return subOrdinate;
    }

    public void setSubOrdinate(String subOrdinate) {
        this.subOrdinate = subOrdinate;
    }
    
    
}
