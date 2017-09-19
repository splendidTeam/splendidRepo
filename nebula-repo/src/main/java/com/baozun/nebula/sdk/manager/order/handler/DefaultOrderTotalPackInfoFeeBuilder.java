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
package com.baozun.nebula.sdk.manager.order.handler;

import static java.math.BigDecimal.ZERO;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.baozun.nebula.sdk.command.OrderLineCommand;
import com.baozun.nebula.sdk.command.OrderLinePackageInfoCommand;
import com.baozun.nebula.sdk.command.SalesOrderCommand;
import com.google.common.collect.Iterables;

import static com.feilong.core.Validator.isNullOrEmpty;
import static com.feilong.core.bean.ConvertUtil.toList;
import static com.feilong.core.util.AggregateUtil.sum;
import static com.feilong.core.util.CollectionsUtil.getPropertyValueList;
import static com.feilong.core.util.CollectionsUtil.removeAll;

/**
 * 
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @since 5.3.2.22
 */
@Component("orderTotalPackInfoFeeBuilder")
public class DefaultOrderTotalPackInfoFeeBuilder implements OrderTotalPackInfoFeeBuilder{

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultOrderTotalPackInfoFeeBuilder.class);

    /*
     * (non-Javadoc)
     * 
     * @see com.baozun.nebula.sdk.manager.order.handler.DisplayTotalPackInfoFeeBuilder#getTotalPackInfoFee(com.baozun.nebula.sdk.command.SalesOrderCommand)
     */
    @Override
    public BigDecimal build(SalesOrderCommand salesOrderCommand){
        List<OrderLineCommand> orderLineCommandList = salesOrderCommand.getOrderLines();
        List<List<OrderLinePackageInfoCommand>> orderLinePackageInfoCommandListList = getPropertyValueList(orderLineCommandList, "orderLinePackageInfoCommandList");

        orderLinePackageInfoCommandListList = removeAll(orderLinePackageInfoCommandListList, toList(null, Collections.<OrderLinePackageInfoCommand> emptyList()));
        if (isNullOrEmpty(orderLinePackageInfoCommandListList)){
            LOGGER.debug("no orderLinePackageInfoCommandListList need calc package fee");
            return ZERO;
        }

        //Iterables.concat 要求元素不能有null
        //平铺
        Iterable<OrderLinePackageInfoCommand> shoppingCartLinePackageInfoCommandList = Iterables.concat(orderLinePackageInfoCommandListList);
        return sum(shoppingCartLinePackageInfoCommandList, "total");
    }

}
