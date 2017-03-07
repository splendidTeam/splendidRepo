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
package com.baozun.nebula.sdk.manager.payment;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLinePackageInfoCommand;
import com.feilong.core.lang.NumberUtil;
import com.feilong.core.util.CollectionsUtil;

import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.util.AggregateUtil.sum;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.11-Personalise
 */
@Component("payMoneyBuilder")
public class DefaultPayMoneyBuilder implements PayMoneyBuilder{

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultPayMoneyBuilder.class);

    /** The Constant SEPARATOR_FLAG. */
    private static final String SEPARATOR_FLAG = "\\|\\|";

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.payment.PayMoneyBuilder#build(com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartCommand)
     */
    @Override
    public BigDecimal build(SalesOrderCommand salesOrderCommand,ShoppingCartCommand shoppingCartCommand){

        //目前的逻辑是 shoppingCartCommand.getCurrentPayAmount() (实付金额) 减去salesOrderCommand.getSoPayMentDetails() 相关项支付的金额
        BigDecimal paySum = shoppingCartCommand.getCurrentPayAmount();

        List<String> soPayMentDetails = salesOrderCommand.getSoPayMentDetails();
        if (soPayMentDetails != null){
            for (String soPayMentDetail : soPayMentDetails){
                // 支付方式 String格式：shopId||payMentType||金额
                String[] strs = soPayMentDetail.split(SEPARATOR_FLAG);

                BigDecimal payMoney = new BigDecimal(strs[2]);

                paySum = paySum.subtract(payMoney);//减去其他方式支付的金额
            }
        }
        return NumberUtil.getAddValue(paySum, getTotalPackInfoFee(shoppingCartCommand));
    }

    /**
     * 获得包装费用的总计金额
     * 
     * @param shoppingCartCommand
     * @return
     */
    private Number getTotalPackInfoFee(ShoppingCartCommand shoppingCartCommand){
        List<ShoppingCartLineCommand> shoppingCartLineCommands = shoppingCartCommand.getShoppingCartLineCommands();
        List<List<ShoppingCartLinePackageInfoCommand>> shoppingCartLinePackageInfoCommandListList = CollectionsUtil.getPropertyValueList(shoppingCartLineCommands, "shoppingCartLinePackageInfoCommandList");

        if (isNullOrEmpty(shoppingCartLinePackageInfoCommandListList)){
            return BigDecimal.ZERO;
        }
        //平铺
        Iterable<ShoppingCartLinePackageInfoCommand> shoppingCartLinePackageInfoCommandList = com.google.common.collect.Iterables.concat(shoppingCartLinePackageInfoCommandListList);
        return sum(shoppingCartLinePackageInfoCommandList, "total");
    }
}
