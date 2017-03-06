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

package com.baozun.nebula.manager.salesorder;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.Transformer;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.model.salesorder.OrderLine;
import com.baozun.nebula.sdk.command.OrderLineCommand;
import com.baozun.nebula.sdk.command.OrderLinePackageInfoCommand;
import com.baozun.nebula.sdk.manager.SdkSkuManager;
import com.baozun.nebula.sdk.manager.order.SdkOrderLineManager;
import com.baozun.nebula.sdk.manager.order.SdkOrderLinePackInfoManager;
import com.baozun.nebula.web.controller.order.viewcommand.OrderLinePackageInfoViewCommand;
import com.baozun.nebula.web.controller.order.viewcommand.SimpleOrderLineSubViewCommand;
import com.feilong.core.bean.BeanUtil;
import com.feilong.core.bean.PropertyUtil;
import com.feilong.core.lang.reflect.ConstructorUtil;
import com.feilong.core.util.CollectionsUtil;

/**
 * @author - 项硕
 */
@Transactional
@Service("OrderLineManager")
public class OrderLineManagerImpl implements OrderLineManager{

    @Autowired
    private SdkOrderLineManager sdkOrderLineManager;

    @Autowired
    private SdkSkuManager sdkSkuManager;

    @Autowired
    private SdkOrderLinePackInfoManager sdkOrderLinePackInfoManager;

    @Override
    public OrderLine findByPk(Long id){
        return sdkOrderLineManager.findByPk(id);
    }

    @Override
    public List<SimpleOrderLineSubViewCommand> findByOrderID(Long orderId){
        List<OrderLineCommand> orderLineCommandList = sdkOrderLineManager.findOrderLinesByOrderId(orderId);
        orderLineCommandList = sdkOrderLinePackInfoManager.packOrderLinesPackageInfo(orderLineCommandList);

        List<SimpleOrderLineSubViewCommand> simpleOrderLineSubViewCommandList = new ArrayList<>();
        for (OrderLineCommand orderLineCommand : orderLineCommandList){
            simpleOrderLineSubViewCommandList.add(toSimpleOrderLineSubViewCommand(orderLineCommand));
        }
        return simpleOrderLineSubViewCommandList;
    }

    /**
     * @param orderLineCommand
     * @return
     * @since 5.3.2.11-Personalise
     */
    private SimpleOrderLineSubViewCommand toSimpleOrderLineSubViewCommand(OrderLineCommand orderLineCommand){
        String properties = orderLineCommand.getSaleProperty();
        orderLineCommand.setSkuPropertys(sdkSkuManager.getSkuPros(properties));

        SimpleOrderLineSubViewCommand simpleOrderLineSubViewCommand = new SimpleOrderLineSubViewCommand();
        BeanUtil.copyProperties(simpleOrderLineSubViewCommand, orderLineCommand);

        simpleOrderLineSubViewCommand.setListPrice(orderLineCommand.getMSRP());
        simpleOrderLineSubViewCommand.setQuantity(orderLineCommand.getCount());
        simpleOrderLineSubViewCommand.setSubTotalAmt(orderLineCommand.getSubtotal());
        simpleOrderLineSubViewCommand.setItemCode(orderLineCommand.getProductCode());

        List<OrderLinePackageInfoViewCommand> orderLinePackageInfoViewCommandList = toOrderLinePackageInfoViewCommandList(orderLineCommand.getOrderLinePackageInfoCommandList());
        simpleOrderLineSubViewCommand.setOrderLinePackageInfoViewCommandList(orderLinePackageInfoViewCommandList);
        return simpleOrderLineSubViewCommand;
    }

    /**
     * @param orderLinePackageInfoCommandList
     * @return
     * @since 5.3.2.11-Personalise
     */
    private List<OrderLinePackageInfoViewCommand> toOrderLinePackageInfoViewCommandList(List<OrderLinePackageInfoCommand> orderLinePackageInfoCommandList){
        return CollectionsUtil.collect(orderLinePackageInfoCommandList, transformer(OrderLinePackageInfoViewCommand.class));
    }

    /**
     * 
     *
     * @param <I>
     * @param <O>
     * @param type
     * @param includePropertyNames
     * @return
     * @since 5.3.2.11-Personalise
     */
    private static <I, O> Transformer<I, O> transformer(final Class<O> type,final String...includePropertyNames){
        return new Transformer<I, O>(){

            @Override
            public O transform(I inputBean){
                Validate.notNull(inputBean, "inputBean can't be null!");

                O outBean = ConstructorUtil.newInstance(type);

                PropertyUtil.copyProperties(outBean, inputBean, includePropertyNames);
                return outBean;
            }
        };
    }

}
