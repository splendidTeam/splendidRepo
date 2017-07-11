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

import java.math.BigDecimal;

/**
 * 订单的基本信息.
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 5.3.1 2016年5月12日 下午3:16:25
 * @since 5.3.1
 */
public class OrderBaseInfoSubViewCommand extends AbstractOrderViewCommand{

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -6400008677521495969L;

    /** 应付运费 */
    private BigDecimal payableFreight;

    /**
     * 整单折扣 整单折扣-sum（行折扣）= 由于整单促销/商城积分形成的未分摊到行上的折扣总额
     */
    private BigDecimal discount;
    
    public BigDecimal getPayableFreight(){
        return payableFreight;
    }

    public void setPayableFreight(BigDecimal payableFreight){
        this.payableFreight = payableFreight;
    }

    public BigDecimal getDiscount(){
        return discount;
    }

    public void setDiscount(BigDecimal discount){
        this.discount = discount;
    }

}
