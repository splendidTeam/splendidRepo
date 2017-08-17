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
package com.baozun.nebula.api.returnapplication;

import org.springframework.beans.factory.annotation.Autowired;

import com.baozun.nebula.command.ReturnApplicationCommand;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.manager.order.OrderManager;
import com.feilong.core.Validator;

/**
 * 退换货订单默认订单号生成器
 * R+原订单号+当前时间戳
 * 
 * @author yaohua.wang@baozun.com
 *
 */
public class DefaultReturnApplicationCreatorManager implements ReturnApplicationCodeCreatorManager{

    @Autowired
    private OrderManager orderManager;
    
    @Override
    public String createReturnApplicationCodeBySource(ReturnApplicationCommand returnApplicationCommand){
        SalesOrderCommand saleOrder = orderManager.findOrderByCode(returnApplicationCommand.getReturnApplication().getSoOrderCode(), null);
        //默认实现以Return简写R开头
        StringBuilder sb = new StringBuilder("R");
        if(Validator.isNotNullOrEmpty(saleOrder)&&Validator.isNotNullOrEmpty(saleOrder.getCode())){
            //加原订单号
            sb.append(saleOrder.getCode());
        }
        //为了防止多次退换，加上创建时间戳
        Long random = System.currentTimeMillis();
        sb.append(random);
        String code = sb.toString();
        return code;
    }

}
