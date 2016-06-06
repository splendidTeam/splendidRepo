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
package com.baozun.nebula.sdk.manager.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baozun.nebula.sdk.command.logistics.ItemFreightInfoCommand;
import com.baozun.nebula.sdk.command.shoppingcart.CalcFreightCommand;
import com.baozun.nebula.sdk.command.shoppingcart.ShoppingCartLineCommand;
import com.baozun.nebula.sdk.manager.LogisticsManager;
import com.baozun.nebula.sdk.manager.SdkFreightFeeManager;

/**
 *
 * @author <a href="http://feitianbenyue.iteye.com/">feilong</a>
 * @version 5.3.1 2016年5月23日 下午10:44:12
 * @since 5.3.1
 */
@Transactional
@Service("sdkFreightFeeManager")
public class SdkFreightFeeManagerImpl implements SdkFreightFeeManager{

    private static final Logger LOGGER = LoggerFactory.getLogger(SdkFreightFeeManagerImpl.class);

    /** The logistics manager. */
    @Autowired
    private LogisticsManager    logisticsManager;

    /**
     * 无促销情况下计算运费.
     * 
     * @param shopId
     *            the shop id
     * @param calcFreightCommand
     *            the calc freight command
     * @param validLines
     *            the valid lines
     *
     * @return the freight fee
     */
    //XXX feilong add javadoc and flow image
    @Override
    public BigDecimal getFreightFee(Long shopId,CalcFreightCommand calcFreightCommand,List<ShoppingCartLineCommand> validLines){
        if (null == calcFreightCommand){
            return BigDecimal.ZERO;
        }

        Boolean flag = logisticsManager.hasDistributionMode(calcFreightCommand, shopId);

        if (!flag){
            return BigDecimal.ZERO;
        }

        List<ItemFreightInfoCommand> itemList = toItemFreightInfoCommandList(validLines);
        return logisticsManager.findFreight(
                        itemList,
                        calcFreightCommand.getDistributionModeId(),
                        shopId,
                        calcFreightCommand.getProvienceId(),
                        calcFreightCommand.getCityId(),
                        calcFreightCommand.getCountyId(),
                        calcFreightCommand.getTownId());
    }

    /**
     * To item freight info command list.
     *
     * @param validShoppingCartLineCommandList
     *            the valid lines
     * @return the list< item freight info command>
     */
    private List<ItemFreightInfoCommand> toItemFreightInfoCommandList(List<ShoppingCartLineCommand> validShoppingCartLineCommandList){
        // 无促销情况下统计金额小计
        List<ItemFreightInfoCommand> itemFreightInfoCommandList = new ArrayList<ItemFreightInfoCommand>();
        for (ShoppingCartLineCommand shoppingCartLineCommand : validShoppingCartLineCommandList){
            ItemFreightInfoCommand itemFreightInfoCommand = toItemFreightInfoCommand(shoppingCartLineCommand);
            itemFreightInfoCommandList.add(itemFreightInfoCommand);
        }
        return itemFreightInfoCommandList;
    }

    /**
     * @param shoppingCartLineCommand
     * @return
     */
    private ItemFreightInfoCommand toItemFreightInfoCommand(ShoppingCartLineCommand shoppingCartLineCommand){
        ItemFreightInfoCommand itemFreightInfoCommand = new ItemFreightInfoCommand();
        itemFreightInfoCommand.setItemId(shoppingCartLineCommand.getItemId());
        itemFreightInfoCommand.setCount(shoppingCartLineCommand.getQuantity());
        return itemFreightInfoCommand;
    }

}
