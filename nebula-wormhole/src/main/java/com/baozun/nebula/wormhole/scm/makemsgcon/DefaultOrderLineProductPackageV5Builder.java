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
package com.baozun.nebula.wormhole.scm.makemsgcon;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.sdk.command.OrderLineCommand;
import com.baozun.nebula.sdk.command.OrderLinePackageInfoCommand;
import com.baozun.nebula.wormhole.mq.entity.order.ProductPackageV5;

import static com.feilong.core.Validator.isNullOrEmpty;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.13
 */
@Transactional
@Service("orderLineProductPackageV5Builder")
public class DefaultOrderLineProductPackageV5Builder implements OrderLineProductPackageV5Builder{

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultOrderLineProductPackageV5Builder.class);

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.wormhole.scm.makemsgcon.OrderLineProductPackageV5Builder#buildProductPackageV5List(com.baozun.nebula.sdk.command.OrderLineCommand)
     */
    @Override
    public List<ProductPackageV5> buildProductPackageV5List(OrderLineCommand orderLineCommand){
        List<OrderLinePackageInfoCommand> orderLinePackageInfoCommandList = orderLineCommand.getOrderLinePackageInfoCommandList();
        if (isNullOrEmpty(orderLinePackageInfoCommandList)){
            return null;
        }

        List<ProductPackageV5> productPackageV5List = new ArrayList<>();
        for (OrderLinePackageInfoCommand orderLinePackageInfoCommand : orderLinePackageInfoCommandList){
            productPackageV5List.add(toProductPackageV5(orderLinePackageInfoCommand, orderLineCommand));
        }

        return productPackageV5List;
    }

    /**
     * @param orderLinePackageInfoCommand
     * @return
     * @since 5.3.2.13
     */
    protected ProductPackageV5 toProductPackageV5(OrderLinePackageInfoCommand orderLinePackageInfoCommand,OrderLineCommand orderLineCommand){
        ProductPackageV5 productPackageV5 = new ProductPackageV5();
        productPackageV5.setRemark(orderLinePackageInfoCommand.getFeatureInfo());//reebok 约定
        productPackageV5.setTotal(buildTotal(orderLinePackageInfoCommand, orderLineCommand));
        productPackageV5.setType(orderLinePackageInfoCommand.getType());
        return productPackageV5;
    }

    /**
     * @param orderLinePackageInfoCommand
     * @param orderLineCommand
     * @return
     * @since 5.3.2.13
     */
    protected BigDecimal buildTotal(OrderLinePackageInfoCommand orderLinePackageInfoCommand,OrderLineCommand orderLineCommand){
        return orderLinePackageInfoCommand.getTotal();
        //return NumberUtil.getDivideValue(orderLinePackageInfoCommand.getTotal(), orderLineCommand.getCount(), 2);
    }
}
